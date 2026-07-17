# AI Travel Platform

基于 Spring Cloud Alibaba 的前后端分离智能旅游规划平台。项目采用微服务架构，结合 Nacos、Gateway、Redis、RabbitMQ 和 Docker Compose，完成旅游资源管理、AI 路线生成、收藏、历史记录等核心业务能力。

## 项目概述

本项目当前采用以下技术栈与架构：

- 前端：Vue 3、Vite、Tailwind CSS、Element Plus、Axios、Pinia、Vue Router
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
- `web-frontend`：前端页面与交互

> **前端代码**：前端项目代码位于 `frontend` 分支下，包含 Vue3、Element Plus 等前端技术栈实现的完整前端界面。

## 成员工作完成情况

### 成员C - 系统架构与AI核心开发 ✅

| 序号 | 工作内容 | 完成状态 | 说明 |
| :--- | :--- | :--- | :--- |
| 1 | Nacos注册中心搭建 | ✅ | 服务注册/发现/配置管理 |
| 2 | Gateway网关服务开发 | ✅ | 路由转发、Token认证、权限校验、统一异常处理 |
| 3 | User-Service开发 | ✅ | 用户注册/登录/JWT认证/用户信息管理 |
| 4 | AI-Service开发 | ✅ | 接入DeepSeek大语言模型，根据目的地/预算/天数/偏好生成路线 |
| 5 | Route-Service开发 | ✅ | 路线生成/保存/历史查询/热门统计 |
| 6 | OpenFeign服务调用 | ✅ | Route→AI/Attraction/Hotel跨服务调用 |
| 7 | RabbitMQ消息队列 | ✅ | 异步处理：路线生成通知、热门统计、历史保存 |
| 8 | Sentinel熔断降级 | ✅ | 限流/熔断/降级，AI异常时返回预设方案 |
| 9 | Docker Compose部署 | ✅ | MySQL/Nacos/Gateway/RabbitMQ/Redis/微服务容器整合 |
| 10 | 测试与文档编写 | ✅ | 系统架构设计文档、微服务拆分设计、服务治理设计等 |

### 成员B - 旅游资源服务模块 ✅

| 服务 | 完成状态 | 说明 |
| :--- | :--- | :--- |
| Attraction-Service 景点服务 | ✅ | 景点CRUD、分页查询、热门景点、城市列表 |
| Hotel-Service 酒店服务 | ✅ | 酒店CRUD、分页查询、热门酒店、城市列表、推荐 |
| Favorite-Service 收藏服务 | ✅ | 添加/删除收藏、用户收藏列表、类型筛选、检查收藏 |
| Redis缓存实现 | ✅ | 景点/酒店/收藏缓存 |
| Docker镜像构建 | ✅ | 三个业务服务Dockerfile |

### 成员A - 前端页面开发 ✅

| 页面 | 完成状态 | 说明 |
| :--- | :--- | :--- |
| 登录页面 | ✅ | 用户登录、表单验证、Token管理 |
| 注册页面 | ✅ | 用户注册、密码一致性校验、注册成功跳转 |
| 首页 | ✅ | 热门路线展示、热门城市卡片、推荐景点 |
| 景点列表页 | ✅ | 城市筛选、关键词搜索、实时查询 |
| 景点详情页 | ✅ | 景点信息展示、收藏功能、评论展示 |
| 酒店列表页 | ✅ | 城市筛选、关键词搜索、价格展示 |
| 酒店详情页 | ✅ | 酒店信息展示、收藏功能、房型展示 |
| AI路线规划页 | ✅ | 城市/天数/风格选择、AI路线生成、多格式数据解析 |
| 路线列表页 | ✅ | 按城市筛选路线、路线卡片展示 |
| 路线详情页 | ✅ | 完整行程展示、AI建议、收藏功能 |
| 收藏页面 | ✅ | 按类型筛选、收藏列表、删除收藏 |
| 个人中心 | ✅ | 用户信息展示、收藏统计、退出登录 |
| 导航组件 | ✅ | 全局导航、登录状态展示、路由跳转 |
| API层封装 | ✅ | Axios拦截器、统一Token注入、响应数据提取 |
| Pinia状态管理 | ✅ | 用户状态管理、路由守卫、登录验证 |

## 已完成功能

### 后端功能
- Maven 多模块工程能够正常安装
- `gateway-service` 已恢复并完善统一网关入口配置
- `user-service` 已可本地启动，Nacos本地启动冲突问题已处理
- `user-service` 中的循环依赖问题已处理
- Docker 编排已统一为 `compose.yaml`
- 数据库建表与示例数据脚本已补充，并按微服务拆分为建表与初始化数据两份 SQL
- 数据导入脚本已支持 UTF-8 流式导入，避免中文乱码
- `route-service` 已可成功连接 Docker MySQL 并读取路线历史数据
- `route-service`、`attraction-service`、`hotel-service`、`gateway-service`、`ai-service`、`favorite-service` 已补充 UTF-8 响应编码配置
- `user-service` JwtAuthFilter 异常处理已完善（处理无效/空Bearer token）
- `favorite-service` Redis密码配置已补充
- SQL文件合并：将 `sql/segments/` 下的6个SQL文件合并为 `sql/ai_travel_platform_full.sql`
- `route-service` 路线数据完善：创建 `RouteDay` 实体和 `RouteDayMapper`，从数据库查询关联的行程、景点和酒店数据
- 热门路线统计修复：创建 `RouteInitializer` 在服务启动时从数据库初始化热门路线计数，添加 `/api/route/hot/cities` 按城市统计热门路线接口

### 前端功能
- 登录注册功能：支持用户名密码登录、新用户注册、密码一致性校验
- 首页展示：热门路线卡片、热门城市入口、推荐景点列表
- 景点管理：景点列表搜索（城市筛选+关键词）、景点详情展示、收藏功能
- 酒店管理：酒店列表搜索（城市筛选+关键词）、酒店详情展示、收藏功能
- AI路线规划：城市/天数/风格选择、一键生成路线、多格式数据解析与渲染
- 路线浏览：路线列表按城市筛选、路线详情完整展示、AI规划建议查看
- 收藏管理：收藏列表按类型筛选（景点/酒店/路线）、收藏添加与取消
- 个人中心：用户信息展示、收藏统计数据、退出登录
- 全局导航：登录状态展示、路由跳转、页面入口导航
- API层封装：统一请求拦截器（Token注入）、响应拦截器（数据提取、401处理）
- 响应式设计：移动端优先布局、Tailwind CSS响应式断点适配
- 错误处理：请求失败提示、加载状态展示、降级方案（原始数据展示）

## 服务架构

### 后端服务

| 服务名称 | 端口 | 职责 |
| :--- | :--- | :--- |
| `gateway-service` | 8080 | API网关，路由转发与认证 |
| `user-service` | 8081 | 用户服务，JWT认证 |
| `ai-service` | 8082 | AI服务，DeepSeek接口 |
| `route-service` | 8083 | 路线服务，路线生成与管理 |
| `attraction-service` | 8084 | 景点服务 |
| `hotel-service` | 8085 | 酒店服务 |
| `favorite-service` | 8086 | 收藏服务 |
| `common-module` | - | 公共模块 |

### 前端服务

| 服务名称 | 端口 | 职责 |
| :--- | :--- | :--- |
| `web-frontend` | 5173（本地）/ 3000（Docker） | 前端页面，用户交互界面 |

## API接口文档

### 1. 用户服务 (User-Service)

| 接口 | 方法 | 认证 | 说明 |
| :--- | :--- | :--- | :--- |
| `/api/user/register` | POST | 否 | 用户注册 |
| `/api/user/login` | POST | 否 | 用户登录 |
| `/api/user/info` | GET | 是 | 获取当前用户信息 |
| `/api/user/{username}` | GET | 是 | 根据用户名查询用户 |

**POST /api/user/register**

请求体：
```json
{
  "username": "string",
  "password": "string"
}
```

响应：
```json
{
  "success": true,
  "message": "注册成功"
}
```

**POST /api/user/login**

请求体：
```json
{
  "username": "admin",
  "password": "123456"
}
```

响应：
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 604800
}
```

**GET /api/user/info**

请求头：`Authorization: Bearer <token>`

响应：
```json
{
  "username": "admin",
  "roles": [{"authority": "ROLE_ADMIN"}, {"authority": "ROLE_USER"}]
}
```

**GET /api/user/{username}**

请求头：`Authorization: Bearer <token>`

响应：
```json
{
  "username": "admin"
}
```

### 2. AI服务 (AI-Service)

| 接口 | 方法 | 认证 | 说明 |
| :--- | :--- | :--- | :--- |
| `/api/ai/plan` | POST | 是 | 生成旅游规划 |
| `/api/ai/generate-route` | POST | 是 | 生成路线 |
| `/api/ai/health` | GET | 否 | 健康检查 |

**POST /api/ai/plan**

请求体：
```json
{
  "destination": "上海",
  "budget": 3000,
  "days": 3,
  "preference": "城市观光"
}
```

响应字段：
- `destination`：目的地
- `days`：出行天数
- `budget`：预算
- `preference`：旅游偏好
- `summary`：AI 规划摘要
- `recommendations`：推荐建议列表
- `itinerary`：按天生成的行程安排

**POST /api/ai/generate-route**

请求体：
```json
{
  "city": "北京",
  "days": 3,
  "style": "文化游"
}
```

**GET /api/ai/health**

响应：
```json
{
  "status": "UP",
  "service": "ai-service"
}
```

### 3. 路线服务 (Route-Service)

| 接口 | 方法 | 认证 | 说明 |
| :--- | :--- | :--- | :--- |
| `/api/route/generate` | POST | 是 | 生成路线 |
| `/api/route/save` | POST | 是 | 保存路线 |
| `/api/route/{id}` | GET | 是 | 获取路线详情 |
| `/api/route` | GET | 是 | 获取所有路线 |
| `/api/route/history/{userId}` | GET | 是 | 用户历史路线 |
| `/api/route/hot` | GET | 是 | 获取热门路线（按城市+天数+风格组合） |
| `/api/route/hot/cities` | GET | 是 | 获取热门城市（按城市统计） |
| `/api/route/hot/count` | GET | 是 | 查询路线统计 |
| `/api/route/sentinel-test` | POST | 是 | Sentinel测试 |

**POST /api/route/generate**

请求体：
```json
{
  "city": "北京",
  "days": 3,
  "style": "文化游",
  "budget": 5000
}
```

**POST /api/route/save**

请求体：
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

**GET /api/route/{id}**

请求头：`Authorization: Bearer <token>`

**GET /api/route/history/{userId}**

请求头：`Authorization: Bearer <token>`

响应字段：
- `routeId`：路线 ID
- `name`：路线名称
- `destination`：目的地
- `days`：天数
- `budget`：预算
- `preference`：偏好
- `aiSummary`：路线摘要
- `createdAt`：创建时间

**GET /api/route/hot**

请求参数：`limit` (可选，默认10)

响应示例：
```json
[{"city":"上海","days":3,"style":"美食","count":2},{"city":"北京","days":5,"style":"历史文化","count":1}]
```

**GET /api/route/hot/cities**

请求参数：`limit` (可选，默认10)

响应示例：
```json
[{"city":"上海","count":3},{"city":"成都","count":2},{"city":"广州","count":2}]
```

**GET /api/route/hot/count**

请求参数：`city`, `days`, `style`

### 4. 景点服务 (Attraction-Service)

| 接口 | 方法 | 认证 | 说明 |
| :--- | :--- | :--- | :--- |
| `/api/attractions` | POST | 是 | 新增景点 |
| `/api/attractions/{id}` | PUT | 是 | 修改景点 |
| `/api/attractions/{id}` | DELETE | 是 | 删除景点 |
| `/api/attractions` | GET | 是 | 景点查询（分页） |
| `/api/attractions/{id}` | GET | 是 | 景点详情 |
| `/api/attractions/hot` | GET | 是 | 热门景点 |
| `/api/attractions/cities` | GET | 是 | 城市列表 |

**查询支持参数**：
- `city`：按城市筛选
- `minPrice` / `maxPrice`：按价格区间筛选
- `sortBy`：排序字段（`rating` / `price`）
- `sortOrder`：排序方式（`asc` / `desc`）
- `pageNum` / `pageSize`：分页参数

### 5. 酒店服务 (Hotel-Service)

| 接口 | 方法 | 认证 | 说明 |
| :--- | :--- | :--- | :--- |
| `/api/hotels` | POST | 是 | 新增酒店 |
| `/api/hotels/{id}` | PUT | 是 | 修改酒店 |
| `/api/hotels/{id}` | DELETE | 是 | 删除酒店 |
| `/api/hotels` | GET | 是 | 酒店查询（分页） |
| `/api/hotels/{id}` | GET | 是 | 酒店详情 |
| `/api/hotels/hot` | GET | 是 | 热门酒店 |
| `/api/hotels/cities` | GET | 是 | 城市列表 |
| `/api/hotels/recommend` | GET | 是 | 酒店推荐 |

**查询支持参数**：
- `city`：按城市筛选
- `minPrice` / `maxPrice`：按预算筛选
- `sortBy`：排序字段（`rating` / `price`）
- `sortOrder`：排序方式（`asc` / `desc`）
- `pageNum` / `pageSize`：分页参数

### 6. 收藏服务 (Favorite-Service)

| 接口 | 方法 | 认证 | 说明 |
| :--- | :--- | :--- | :--- |
| `/api/favorites` | POST | 是 | 添加收藏（支持景点/酒店/路线） |
| `/api/favorites` | DELETE | 是 | 删除收藏 |
| `/api/favorites/user/{userId}` | GET | 是 | 用户收藏列表 |
| `/api/favorites/user/{userId}/type/{targetType}` | GET | 是 | 按类型筛选收藏 |
| `/api/favorites/check` | GET | 是 | 检查是否已收藏 |

**POST /api/favorites**

请求体：
```json
{
  "userId": 1,
  "targetId": 2,
  "targetType": "route"
}
```

**DELETE /api/favorites**

请求参数：`userId`, `targetId`, `targetType`

**GET /api/favorites/check**

请求参数：`userId`, `targetId`, `targetType`

响应：
```json
{
  "isFavorite": true
}
```

## Docker Compose 现状

当前项目统一使用 `docker-compose.yaml` 作为 Docker 编排文件，包含以下服务：

### 基础设施服务

- MySQL 8.0
- Nacos 2.3.2（standalone）
- Redis 7.2-alpine
- RabbitMQ 3.12-management

### 后端微服务

- user-service：用户认证服务
- ai-service：AI 路线规划服务
- route-service：路线生成与管理服务
- attraction-service：景点服务
- hotel-service：酒店服务
- favorite-service：收藏服务
- gateway-service：API 网关

### 前端服务

- web-frontend：前端页面（基于 Nginx 部署）

### 端口映射

- MySQL：`3309 -> 3306`
- Nacos：`8848 -> 8848`
- Redis：`6379 -> 6379`
- RabbitMQ：`5672 -> 5672`
- RabbitMQ 管理界面：`15672 -> 15672`
- Gateway：`8080 -> 8080`
- Web Frontend：`3000 -> 80`

### 账号信息

- MySQL root 密码：`123456`
- MySQL 数据库：`travel_platform`
- Redis 密码：`123456`
- RabbitMQ 默认账号：`guest / guest`

### 数据库脚本与持久化

项目已将数据库脚本统一放在 `sql/` 目录下：

- `sql/ai_travel_platform_full.sql`：合并后的完整数据库脚本（包含所有表结构和数据）
- `sql/00_create_tables.sql`：建表脚本
- `sql/import_data.py`：UTF-8 分批导入脚本
- `sql/segments/01_user.sql`：用户数据
- `sql/segments/02_attraction.sql`：景点数据
- `sql/segments/03_hotel.sql`：酒店数据
- `sql/segments/04_route.sql`：路线数据
- `sql/segments/05_route_day.sql`：路线日程数据
- `sql/segments/06_favorite.sql`：收藏数据

### 表与微服务对应关系

- `user-service`：`user`
- `attraction-service`：`attraction`
- `hotel-service`：`hotel`
- `favorite-service`：`favorite`
- `route-service`：`route`、`route_day`
- `ai-service`：复用 `route`、`route_day` 作为结果展示数据

### 数据规模

- `user`：20 条
- `attraction`：51 条
- `hotel`：36 条
- `route`：20 条
- `route_day`：70 条
- `favorite`：20 条

## 本地启动方式

### 1. 启动基础设施

```bash
docker compose up -d
```

### 2. 启动后端服务

```bash
# 启动用户服务
mvn -pl user-service spring-boot:run

# 启动其他服务
mvn -pl ai-service spring-boot:run
mvn -pl route-service spring-boot:run
mvn -pl attraction-service spring-boot:run
mvn -pl hotel-service spring-boot:run
mvn -pl favorite-service spring-boot:run
mvn -pl gateway-service spring-boot:run
```

### 3. 启动前端服务

```bash
cd web-frontend
npm install
npm run dev
```

前端访问地址：`http://localhost:5173`

### 4. 测试账号

- 用户名：`admin` / 密码：`123456`
- 用户名：`user` / 密码：`123456`
- 用户名：`travel_lily` / 密码：`lily123`

## Gateway 统一入口

`gateway-service` 已配置统一路由入口，支持按以下路径转发：

- `/api/user/**` -> `user-service`
- `/api/ai/**` -> `ai-service`
- `/api/route/**` -> `route-service`
- `/api/attraction/**` -> `attraction-service`
- `/api/hotel/**` -> `hotel-service`
- `/api/favorite/**` -> `favorite-service`

## 已修复的问题

### 后端问题

#### 1. 父工程依赖缺失

- 错误：`Could not find artifact com.example:travel-platform-backend:pom:1.0.0-SNAPSHOT`
- 处理：先执行根工程安装，确保父 POM 进入本地仓库

#### 2. Spring Cloud Nacos import 报错

- 错误：`No spring.config.import property has been defined`
- 处理：本地调试阶段关闭了 Nacos 配置与注册依赖，生产环境可重新开启

#### 3. Spring Security 循环依赖

- 错误：`jwtAuthFilter -> userService -> securityConfig -> jwtAuthFilter`
- 处理：调整了 `SecurityConfig` 中 `JwtAuthFilter` 的注入方式，解除循环依赖

#### 4. JwtAuthFilter 无效Token处理

- 问题：Postman 请求携带空的或无效的 Authorization 头（如 `Bearer `）时返回 500 错误
- 处理：在 `JwtAuthFilter` 中添加了空token检查和 `JwtException` 异常捕获，无效token请求会继续过滤器链而不是抛出异常

#### 5. Favorite-Service Redis密码缺失

- 问题：Redis 启用了密码认证，但 `favorite-service` 配置中未提供密码，导致添加收藏时返回 500 错误
- 处理：在 `favorite-service` 的 `application.yml` 中添加了 Redis 密码配置

#### 6. Route-Service 路线ID问题

- 问题：`GET /api/route/1` 返回 404
- 处理：数据库中路线ID从 2 开始，建议使用有效的路线ID测试

### 前端问题

#### 7. AI路线规划数据结构兼容性问题

- 问题：后端返回的AI路线数据格式不统一（day1/day2/day3直接属性、itinerary对象、schedule字段、itinerary数组），导致前端无法正确解析和渲染行程内容
- 处理：在 Route.vue 组件中增强 formattedDays 计算属性，依次支持多种数据格式的解析

#### 8. Gateway认证拦截导致AI接口无法访问

- 问题：AI路线生成接口被Gateway的JwtAuthenticationFilter拦截，未登录用户无法调用
- 处理：在 GatewaySecurityFilter 和 GatewayAuthFilter 中添加 `/api/ai/generate-route` 和 `/api/ai/plan` 到白名单，并移除 application.yml 中 ai-service 路由的 JwtAuthenticationFilter 配置

#### 9. 响应式布局适配问题

- 问题：首页、景点列表页和酒店列表页在移动端显示时布局错乱
- 处理：使用 Tailwind CSS 的响应式前缀（sm:、md:、lg:）配置不同屏幕尺寸的布局

#### 10. 收藏功能未登录状态处理

- 问题：未登录用户点击收藏按钮时页面无响应或报错
- 处理：在收藏相关组件中添加登录状态检查，未登录时自动跳转登录页面

## Redis 缓存实现

**Attraction-Service**：
- `attraction:hot`：热门景点缓存（30分钟）
- `attraction:cities`：热门城市缓存（30分钟）
- `attraction:detail:{id}`：景点详情缓存（30分钟）

**Hotel-Service**：
- `hotel:hot`：热门酒店缓存（30分钟）
- `hotel:cities`：热门城市缓存（30分钟）
- `hotel:detail:{id}`：酒店详情缓存（30分钟）

**Favorite-Service**：
- `favorite:user:{userId}`：用户收藏列表缓存（15分钟）

## RabbitMQ 异步消息

Route-Service 通过 RabbitMQ 处理异步任务：
- `route.generated.queue` - 路线生成通知
- `hot.route.stats.queue` - 热门路线统计
- `history.save.queue` - 历史记录保存

## Sentinel 配置

- Route-Service：10 QPS
- AI-Service：5 QPS

## 调用说明

### 认证方式

1. 通过 `/api/user/login` 获取 JWT Token
2. 在后续请求的 Header 中携带 `Authorization: Bearer <token>`

### 服务调用链

```
Gateway → Route-Service → AI-Service
                        → Attraction-Service
                        → Hotel-Service
```

## 目录结构

```
ai-travel-platform/
├── common-module/          # 公共模块
├── gateway-service/        # API网关
├── user-service/           # 用户服务
├── ai-service/             # AI服务
├── route-service/          # 路线服务
├── attraction-service/     # 景点服务
├── hotel-service/          # 酒店服务
├── favorite-service/       # 收藏服务
├── web-frontend/           # 前端页面
│   ├── src/
│   │   ├── api/            # API接口封装
│   │   ├── components/     # 公共组件
│   │   ├── stores/         # Pinia状态管理
│   │   ├── views/          # 页面组件
│   │   ├── App.vue         # 根组件
│   │   └── main.js         # 入口文件
│   └── package.json        # 前端依赖配置
├── sql/                    # 数据库脚本
├── docker-compose.yaml     # Docker部署配置
└── README.md               # 项目说明文档
```

## 许可证

MIT License