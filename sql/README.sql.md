# AI旅游规划平台 SQL 说明

## 文件说明

- `00_create_tables.sql`：建表脚本
- `01_seed_data.sql`：初始化数据脚本

## 使用顺序

1. 先执行 `00_create_tables.sql`
2. 再执行 `01_seed_data.sql`

## 数据覆盖范围

- `user`：20 条示例用户数据
- `attraction`：20 条景点数据
- `hotel`：20 条酒店数据
- `route`：20 条路线数据
- `route_day`：20 条路线日程数据
- `favorite`：20 条收藏数据

## 对应微服务

- `user-service`：`user`
- `attraction-service`：`attraction`
- `hotel-service`：`hotel`
- `favorite-service`：`favorite`
- `route-service`：`route`、`route_day`
- `ai-service`：可直接复用 `route` 与 `route_day` 作为生成结果展示数据

## 说明

这套数据主要用于课程设计演示、接口联调和前后端展示。如果后续接入真实业务数据，可以在此基础上继续扩展字段和记录数量。
