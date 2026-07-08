# AI旅游规划平台 SQL 说明

## 文件说明

- `00_create_tables.sql`：建表脚本
- `segments/01_user.sql`：用户数据
- `segments/02_attraction.sql`：景点数据
- `segments/03_hotel.sql`：酒店数据
- `segments/04_route.sql`：路线数据
- `segments/05_route_day.sql`：路线日程数据
- `segments/06_favorite.sql`：收藏数据
- `import_data.ps1`：PowerShell 分批导入脚本

## 使用顺序

1. 先执行 `00_create_tables.sql`
2. 再执行 `import_data.ps1` 或按顺序执行 `segments/` 下的 SQL 分段文件

## 数据覆盖范围

- `user`：20 条示例用户数据
- `attraction`：51 条景点数据，覆盖上海、北京、杭州、成都、三亚、西安、厦门、广州等主要旅游城市
- `hotel`：36 条酒店数据，覆盖高端、商务、舒适、经济、民宿、青旅等不同类型
- `route`：20 条路线数据，覆盖不同天数与不同偏好
- `route_day`：70 条路线日程数据，补充每日行程明细
- `favorite`：20 条收藏数据

## 当前数据映射说明

这套数据已按当前项目中的实际表结构整理，可直接用于导入：

- `user` → `sql/segments/01_user.sql`
- `attraction` → `sql/segments/02_attraction.sql`
- `hotel` → `sql/segments/03_hotel.sql`
- `route` → `sql/segments/04_route.sql`
- `route_day` → `sql/segments/05_route_day.sql`
- `favorite` → `sql/segments/06_favorite.sql`

### 字段映射要点

- `user.password`：统一使用同一个明文密码 `123456` 对应的 BCrypt 哈希值
- `attraction.cover_image`、`hotel.cover_image`：统一使用 `picsum.photos` 随机图片服务
- `route.ai_summary`：已整理为更接近 AI 输出的摘要文本
- `route_day.attraction_ids`：使用逗号分隔的景点 ID 字符串
- `route_day.hotel_id`：对应酒店主键 ID
- `favorite.target_type`：仅使用 `attraction`、`hotel`、`route` 三种类型

## 数据特征

当前初始化数据已经从纯占位演示数据升级为更接近真实平台展示的数据，重点增强了：

- 城市覆盖范围
- 景点/酒店名称的真实感
- 路线与日程的完整度
- 旅游偏好与标签维度
- 可访问的真实图片 URL

## 对应微服务

- `user-service`：`user`
- `attraction-service`：`attraction`
- `hotel-service`：`hotel`
- `favorite-service`：`favorite`
- `route-service`：`route`、`route_day`
- `ai-service`：可直接复用 `route` 与 `route_day` 作为生成结果展示数据

## 说明

这套数据主要用于课程设计演示、接口联调和前后端展示。如果后续接入真实业务数据，可以在此基础上继续扩展字段和记录数量。

## 用户密码说明

`segments/01_user.sql` 中的密码字段已统一替换为同一个明文密码 `123456` 对应的 BCrypt 哈希值，便于 `user-service` 登录联调。这里保存的是加密后的字符串，不是明文密码。
