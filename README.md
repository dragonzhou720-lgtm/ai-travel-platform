# AI Travel Platform

基于 Spring Cloud Alibaba 的前后端分离智能旅游规划平台。项目采用微服务架构，结合 Nacos、Gateway、Redis、RabbitMQ 和 Docker Compose，完成旅游资源管理、AI 路线生成、收藏、历史记录等核心业务能力。

## 项目概述

本项目当前采用以下技术栈与架构：

- 前端：Vue3、Element Plus、Axios、Pinia、Vue Router
- 后端：Spring Boot、Spring Cloud Alibaba、OpenFeign、Spring Security、JWT
- 基础设施：Nacos、Redis、RabbitMQ、MySQL
- 部署：Docker、Docker Compose、Nginx

系统目标是实现一个可演示、可联调、可部署的 AI 旅游规划平台。

## 当前项目结构

当前仓库包含以下核心模块：

- `common-module`：公共依赖与通用能力
- `gateway-service`：统一网关
- `user-service`：用户认证与用户信息服务
- `ai-service`：AI 旅游规划服务
- `route-service`：路线生成与历史路线管理
- `attraction-service`：景点服务
- `hotel-service`：酒店服务
- `favorite-service`：收藏服务

## 当前完成情况

### 已完成

- Maven 多模块工程能够正常安装
- `user-service` 已可本地启动
- `user-service` 的 Nacos 本地启动冲突问题已处理
- `user-service` 中的循环依赖问题已处理
- `docker-compose.yaml` 已创建并通过配置校验
- 数据库建表与示例数据脚本已补充，并按微服务拆分为建表与初始化数据两份 SQL

### 当前状态说明

- `user-service` 本地调试时已可正常启动到 `8081`
- 如果本地已有实例运行，再次启动可能会出现 `8081` 端口占用
- `user-service` 目前本地启动时已关闭 Nacos 注册与配置依赖，适合开发调试
- 由于 `docker-compose` 使用的是独立容器环境，Nacos 与 MySQL 的联动还需要根据实际初始化脚本继续完善

## Docker Compose 现状

当前 `docker-compose.yaml` 已包含以下服务：

- MySQL 8.0
- Nacos 2.3.2（standalone）
- Redis 7.2-alpine
- RabbitMQ 3.12-management

### 端口映射

- MySQL：`3308 -> 3306`
- Nacos：`8848 -> 8848`
- Redis：`6379 -> 6379`
- RabbitMQ：`5672 -> 5672`
- RabbitMQ 管理界面：`15672 -> 15672`

> 注意：当前 MySQL 宿主机端口映射为 `3308`，不是 `3306`。

### 账号信息

- MySQL root 密码：`123456`
- MySQL 数据库：`travel_platform`
- MySQL 宿主机端口：`3308`
- Redis 密码：`123456`
- RabbitMQ 默认账号：`guest / guest`

### 数据库脚本与持久化说明

项目已将数据库脚本统一放在 `sql/` 目录下，并通过 Docker 持久化 MySQL 数据卷，便于组员同步和联调：

- `sql/00_create_tables.sql`：建表脚本
- `sql/01_seed_data.sql`：初始化数据脚本
- `sql/README.sql.md`：SQL 使用说明

当前 `docker-compose.yaml` 已将本地 `sql/` 目录挂载到 MySQL 容器的 `/docker-entrypoint-initdb.d`，因此在首次初始化空数据卷时会自动执行脚本。

#### 表与微服务对应关系

- `user-service`：`user`
- `attraction-service`：`attraction`
- `hotel-service`：`hotel`
- `favorite-service`：`favorite`
- `route-service`：`route`、`route_day`
- `ai-service`：复用 `route`、`route_day` 作为结果展示数据

#### 当前示例数据量

- `user`：20 条
- `attraction`：20 条
- `hotel`：20 条
- `route`：20 条
- `route_day`：20 条
- `favorite`：20 条

#### 同步与持久化建议

- 组员统一拉取同一份仓库中的 SQL 脚本
- MySQL 使用 Docker volume 持久化数据，避免容器重建后丢失
- 如果需要重新导入初始化数据，可先执行 `docker compose down -v` 再重新启动
- 前端仅依赖统一接口文档，不直接依赖数据库实现

## 本地启动方式

### 1. 启动基础设施

在项目根目录执行：

```bash
docker compose up -d
```

### 2. 启动用户服务

进入 `user-service` 目录后执行：

```bash
mvn spring-boot:run
```

如果 `8081` 已占用，请先停止旧进程或更换端口。

### 3. 启动其他服务

其余微服务可按同样方式在各自模块目录执行：

```bash
mvn spring-boot:run
```

## 当前已知情况

- `user-service` 当前本地配置不依赖 Nacos 启动，方便开发调试
- 启动 Nacos 后，如需恢复配置中心与服务注册，需要重新开启 Nacos 相关配置
- `user-service` 使用 `JwtAuthFilter` + `SecurityConfig` 实现认证链路
- 当前仓库里可能存在多个服务尚未完全接入实际业务数据源，后续仍需继续完善

## 已修复的问题

### 1. 父工程依赖缺失

之前 `user-service` 启动时报错：

- `Could not find artifact com.example:travel-platform-backend:pom:1.0.0-SNAPSHOT`

处理方式：

- 先执行根工程安装，确保父 POM 进入本地仓库

### 2. Spring Cloud Nacos import 报错

之前 `user-service` 启动时报错：

- `No spring.config.import property has been defined`

处理方式：

- 本地调试阶段关闭了 Nacos 配置与注册依赖

### 3. Spring Security 循环依赖

之前 `user-service` 启动时报错：

- `jwtAuthFilter -> userService -> securityConfig -> jwtAuthFilter`

处理方式：

- 调整了 `SecurityConfig` 中 `JwtAuthFilter` 的注入方式，解除循环依赖



## 备注

本 README 反映的是当前项目的实际开发状态，后续随着微服务、配置中心和部署方案完善，可以继续更新为完整项目说明文档。
