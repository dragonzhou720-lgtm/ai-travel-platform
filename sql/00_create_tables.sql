-- AI旅游规划平台：建表脚本
-- 数据库：travel_platform

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS travel_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE travel_platform;

DROP TABLE IF EXISTS route_day;
DROP TABLE IF EXISTS route;
DROP TABLE IF EXISTS favorite;
DROP TABLE IF EXISTS hotel;
DROP TABLE IF EXISTS attraction;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码',
  `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像地址',
  `gender` TINYINT DEFAULT 0 COMMENT '性别：0未知 1男 2女',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1正常 0禁用',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_phone` (`phone`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE `attraction` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '景点ID',
  `name` VARCHAR(100) NOT NULL COMMENT '景点名称',
  `city` VARCHAR(50) NOT NULL COMMENT '所属城市',
  `address` VARCHAR(255) DEFAULT NULL COMMENT '详细地址',
  `ticket_price` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '门票价格',
  `rating` DECIMAL(3,1) NOT NULL DEFAULT 0.0 COMMENT '景点评分',
  `description` TEXT COMMENT '景点描述',
  `open_time` VARCHAR(100) DEFAULT NULL COMMENT '开放时间',
  `tags` VARCHAR(255) DEFAULT NULL COMMENT '标签，逗号分隔',
  `cover_image` VARCHAR(255) DEFAULT NULL COMMENT '封面图',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用 0下架',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_city` (`city`),
  KEY `idx_rating` (`rating`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景点表';

CREATE TABLE `hotel` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '酒店ID',
  `name` VARCHAR(100) NOT NULL COMMENT '酒店名称',
  `city` VARCHAR(50) NOT NULL COMMENT '所属城市',
  `address` VARCHAR(255) DEFAULT NULL COMMENT '酒店地址',
  `price_per_night` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '每晚价格',
  `rating` DECIMAL(3,1) NOT NULL DEFAULT 0.0 COMMENT '酒店评分',
  `star_level` TINYINT DEFAULT 3 COMMENT '星级',
  `description` TEXT COMMENT '酒店描述',
  `tags` VARCHAR(255) DEFAULT NULL COMMENT '标签，逗号分隔',
  `cover_image` VARCHAR(255) DEFAULT NULL COMMENT '封面图',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用 0下架',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_city` (`city`),
  KEY `idx_price` (`price_per_night`),
  KEY `idx_rating` (`rating`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='酒店表';

CREATE TABLE `favorite` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `target_id` BIGINT NOT NULL COMMENT '收藏对象ID',
  `target_type` VARCHAR(20) NOT NULL COMMENT '收藏类型：attraction/hotel/route',
  `target_name` VARCHAR(100) DEFAULT NULL COMMENT '收藏对象名称',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_target_type` (`target_type`),
  KEY `idx_target_id` (`target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

CREATE TABLE `route` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '路线ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `name` VARCHAR(100) NOT NULL COMMENT '路线名称',
  `destination` VARCHAR(50) NOT NULL COMMENT '目的地',
  `days` INT NOT NULL COMMENT '出行天数',
  `budget` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '预算',
  `preference` VARCHAR(100) DEFAULT NULL COMMENT '旅游偏好',
  `ai_summary` TEXT COMMENT 'AI规划摘要',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1正常 0删除',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_destination` (`destination`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='路线主表';

CREATE TABLE `route_day` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日程ID',
  `route_id` BIGINT NOT NULL COMMENT '路线ID',
  `day_no` INT NOT NULL COMMENT '第几天',
  `title` VARCHAR(100) NOT NULL COMMENT '当日标题',
  `schedule_detail` TEXT NOT NULL COMMENT '当日行程详情',
  `attraction_ids` VARCHAR(255) DEFAULT NULL COMMENT '推荐景点ID列表',
  `hotel_id` BIGINT DEFAULT NULL COMMENT '推荐酒店ID',
  PRIMARY KEY (`id`),
  KEY `idx_route_id` (`route_id`),
  KEY `idx_day_no` (`day_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='路线日程表';

SET FOREIGN_KEY_CHECKS = 1;
