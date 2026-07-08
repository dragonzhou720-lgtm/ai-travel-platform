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
- `gateway-service` 已恢复并完善统一网关入口配置
- `user-service` 已可本地启动
- `user-service` 的 Nacos 本地启动冲突问题已处理
- `user-service` 中的循环依赖问题已处理
- Docker 编排已统一为 `compose.yaml`
- 数据库建表与示例数据脚本已补充，并按微服务拆分为建表与初始化数据两份 SQL
- 数据导入脚本已支持 UTF-8 流式导入，避免中文乱码
- `route-service` 已可成功连接 Docker MySQL 并读取路线历史数据
- `route-service`、`attraction-service`、`hotel-service`、`gateway-service`、`ai-service`、`favorite-service` 已补充 UTF-8 响应编码配置

### 当前状态说明

- `user-service` 本地调试时已可正常启动到 `8081`
- 如果本地已有实例运行，再次启动可能会出现 `8081` 端口占用
- 当前 Docker MySQL 宿主机端口映射为 `3309`
- 当前数据已成功导入并可被 `route-service` 读取
- `route-service` 的历史路线接口已验证可返回正常中文
- 由于 `compose.yaml` 使用独立容器环境，启动前请确保端口 `3309/8848/6379/5672/15672` 未被其他进程占用

## Docker Compose 现状

当前项目统一使用 `compose.yaml` 作为 Docker 编排文件，包含以下服务：

- MySQL 8.0
- Nacos 2.3.2（standalone）
- Redis 7.2-alpine
- RabbitMQ 3.12-management

### 端口映射

- MySQL：`3309 -> 3306`
- Nacos：`8848 -> 8848`
- Redis：`6379 -> 6379`
- RabbitMQ：`5672 -> 5672`
- RabbitMQ 管理界面：`15672 -> 15672`

> 注意：当前 MySQL 宿主机端口映射为 `3309`。

### 账号信息

- MySQL root 密码：`123456`
- MySQL 数据库：`travel_platform`
- MySQL 宿主机端口：`3309`
- Redis 密码：`123456`
- RabbitMQ 默认账号：`guest / guest`

### 数据库脚本与持久化说明

项目已将数据库脚本统一放在 `sql/` 目录下，并通过 Docker 持久化 MySQL 数据卷，便于组员同步和联调：

- `sql/00_create_tables.sql`：建表脚本
- `sql/import_data.py`：UTF-8 分批导入脚本
- `sql/import_data.ps1`：PowerShell 分批导入脚本
- `sql/segments/01_user.sql`：用户数据
- `sql/segments/02_attraction.sql`：景点数据
- `sql/segments/03_hotel.sql`：酒店数据
- `sql/segments/04_route.sql`：路线数据
- `sql/segments/05_route_day.sql`：路线日程数据
- `sql/segments/06_favorite.sql`：收藏数据
- `sql/README.sql.md`：SQL 使用说明

当前 `compose.yaml` 已将本地 `sql/` 目录挂载到 MySQL 容器的 `/docker-entrypoint-initdb.d`，因此在首次初始化空数据卷时会自动执行脚本。当前 MySQL 宿主机端口映射为 `3309`。

#### 表与微服务对应关系

- `user-service`：`user`
- `attraction-service`：`attraction`
- `hotel-service`：`hotel`
- `favorite-service`：`favorite`
- `route-service`：`route`、`route_day`
- `ai-service`：复用 `route`、`route_day` 作为结果展示数据

#### 数据规模与业务覆盖说明

当前初始化数据已经覆盖平台核心业务链路，可支撑课程设计演示、接口联调和功能答辩。

### 数据规模

- `user`：20 条
- `attraction`：51 条，覆盖上海、北京、杭州、成都、三亚、西安、厦门、广州等主要旅游城市
- `hotel`：36 条，覆盖高端、商务、舒适、经济、民宿、青旅等不同类型
- `route`：20 条，覆盖城市观光、历史文化、海滨度假、亲子游、美食游、摄影游、慢旅行等场景
- `route_day`：70 条，覆盖多天完整行程
- `favorite`：20 条，覆盖景点、酒店、路线三类收藏

### 业务覆盖

- 用户登录与认证测试
- 景点浏览与推荐展示
- 酒店查询与预算匹配
- AI 旅游路线生成展示
- 路线保存与历史查询
- 收藏功能演示
- RabbitMQ 异步消息联调
- Sentinel 降级兜底联调
- Docker MySQL 真实数据读写联调

### 数据特征

- 图片统一使用 `picsum.photos` 随机生成服务，便于演示和联调
- 路线摘要已按更接近 AI 生成的风格重写
- 景点城市分布更均衡，覆盖多个核心城市
- 酒店价位更分层，适合预算筛选展示
- 数据库中文已完成 UTF-8 重新导入与验证

#### 同步与持久化建议

- 组员统一拉取同一份仓库中的 SQL 脚本
- MySQL 使用 Docker volume 持久化数据，避免容器重建后丢失
- 如果需要重新导入初始化数据，可执行 `python sql/import_data.py`
- 如果需要重新初始化 Docker 数据，可先执行 `docker compose down -v` 再重新启动
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

### 1. Gateway 统一入口

`gateway-service` 已配置统一路由入口，支持按以下路径转发：

- `/api/user/**` -> `user-service`
- `/api/ai/**` -> `ai-service`
- `/api/route/**` -> `route-service`
- `/api/attraction/**` -> `attraction-service`
- `/api/hotel/**` -> `hotel-service`
- `/api/favorite/**` -> `favorite-service`

统一入口用于让前端只访问网关，不直接调用各微服务地址。

### 2. AI 核心规划接口

`ai-service` 已补充核心旅游规划接口：

- `POST /api/ai/plan`

#### 请求参数

```json
{
  "destination": "上海",
  "budget": 3000,
  "days": 3,
  "preference": "城市观光"
}
```

#### 响应字段

- `destination`：目的地
- `days`：出行天数
- `budget`：预算
- `preference`：旅游偏好
- `summary`：AI 规划摘要
- `recommendations`：推荐建议列表
- `itinerary`：按天生成的行程安排

#### 作用

用于根据用户输入生成一份基础的旅游规划结果，后续可替换为真实的大模型接口调用。当前服务也已统一补充 UTF-8 响应编码，便于中文结果正确展示。

### 3. Route 路线保存与历史查询接口

`route-service` 已补充路线保存与历史查询接口，并使用 MySQL 持久化保存结果，同时增加 RabbitMQ 异步消息发布：

- `POST /api/route/save`
- `GET /api/route/history/{userId}`

#### `POST /api/route/save`

用于保存一条旅游路线。

##### 请求参数

```json
{
  "userId": 1,
  "name": "上海经典三日游",
  "destination": "上海",
  "days": 3,
  "budget": 2500,
  "preference": "城市观光",
  "aiSummary": "第一天外滩，第二天迪士尼，第三天自由活动",
  "itinerary": [
    {
      "dayNo": 1,
      "title": "抵达上海",
      "detail": "入住酒店后游览外滩",
      "attractionIds": [1],
      "hotelId": 1
    }
  ]
}
```

##### 作用

用于保存 AI 生成后的路线结果，方便历史查看与后续统计。保存成功后会向 RabbitMQ 发布路线创建事件，用于后续通知和统计处理。


#### `GET /api/route/history/{userId}`

用于查询某个用户的历史路线列表。

##### 路径参数

- `userId`：用户 ID

##### 响应字段

- `routeId`：路线 ID
- `name`：路线名称
- `destination`：目的地
- `days`：天数
- `budget`：预算
- `preference`：偏好
- `aiSummary`：路线摘要
- `createdAt`：创建时间

#### 作用

用于历史记录页面展示和路线回看。

### 4. 目前的联调状态

- `user-service` 当前本地配置不依赖 Nacos 启动，方便开发调试
- 启动 Nacos 后，如需恢复配置中心与服务注册，需要重新开启 Nacos 相关配置
- `user-service` 使用 `JwtAuthFilter` + `SecurityConfig` 实现认证链路
- `route-service` 已连接 Docker MySQL 并验证可读取历史路线数据
- `attraction-service`、`hotel-service`、`gateway-service`、`ai-service`、`favorite-service` 已统一补充 UTF-8 响应编码
- 当前仓库已经具备 Docker 数据导入、中文返回和历史查询联调能力

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
# ai-travel-platform
