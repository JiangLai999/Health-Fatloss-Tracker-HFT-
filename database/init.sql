-- 健康减脂系统 - 数据库初始化脚本
-- 创建时间: 2026-03-14

-- 创建数据库
CREATE DATABASE IF NOT EXISTS health_fatloss DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE health_fatloss;

-- 删除旧表（如果存在）
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `body_data`;
DROP TABLE IF EXISTS `diet_record`;
DROP TABLE IF EXISTS `exercise_record`;
DROP TABLE IF EXISTS `food`;
DROP TABLE IF EXISTS `exercise`;
DROP TABLE IF EXISTS `goal`;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `email` VARCHAR(100) COMMENT '邮箱',
    `phone` VARCHAR(20) COMMENT '手机号',
    `avatar` VARCHAR(255) COMMENT '头像URL',
    `gender` TINYINT COMMENT '性别：0-未知，1-男，2-女',
    `age` INT COMMENT '年龄',
    `height` DECIMAL(5,2) COMMENT '身高(cm)',
    `weight` DECIMAL(5,2) COMMENT '体重(kg)',
    `activity_level` TINYINT DEFAULT 1 COMMENT '活动水平：1-久坐，2-轻度活动，3-中度活动，4-重度活动，5-极度活动',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 身体数据表
CREATE TABLE IF NOT EXISTS `body_data` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `record_date` DATE NOT NULL COMMENT '记录日期',
    `weight` DECIMAL(5,2) NOT NULL COMMENT '体重(kg)',
    `bmi` DECIMAL(5,2) COMMENT 'BMI指数',
    `body_fat_rate` DECIMAL(5,2) COMMENT '体脂率(%)',
    `muscle_mass` DECIMAL(5,2) COMMENT '肌肉量(kg)',
    `water_content` DECIMAL(5,2) COMMENT '水分含量(%)',
    `note` VARCHAR(500) COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_record_date` (`record_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='身体数据表';

-- 食物表
CREATE TABLE IF NOT EXISTS `food` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '食物名称',
    `category` VARCHAR(50) COMMENT '食物分类',
    `calories` DECIMAL(8,2) NOT NULL COMMENT '热量(千卡/100g)',
    `protein` DECIMAL(8,2) COMMENT '蛋白质(g/100g)',
    `carbohydrate` DECIMAL(8,2) COMMENT '碳水化合物(g/100g)',
    `fat` DECIMAL(8,2) COMMENT '脂肪(g/100g)',
    `fiber` DECIMAL(8,2) COMMENT '膳食纤维(g/100g)',
    `unit` VARCHAR(20) DEFAULT 'g' COMMENT '单位',
    `image` VARCHAR(255) COMMENT '食物图片',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食物表';

-- 饮食记录表
CREATE TABLE IF NOT EXISTS `diet_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `food_id` BIGINT NOT NULL COMMENT '食物ID',
    `record_date` DATE NOT NULL COMMENT '记录日期',
    `meal_type` VARCHAR(20) NOT NULL COMMENT '餐次类型：breakfast-早餐，lunch-午餐，dinner-晚餐，snack-加餐',
    `amount` DECIMAL(8,2) NOT NULL COMMENT '食物数量(克)',
    `calories` DECIMAL(8,2) NOT NULL COMMENT '摄入热量(千卡)',
    `protein` DECIMAL(8,2) COMMENT '蛋白质(g)',
    `carbohydrate` DECIMAL(8,2) COMMENT '碳水化合物(g)',
    `fat` DECIMAL(8,2) COMMENT '脂肪(g)',
    `note` VARCHAR(500) COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_record_date` (`record_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='饮食记录表';

-- 运动表
CREATE TABLE IF NOT EXISTS `exercise` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '运动名称',
    `category` VARCHAR(50) COMMENT '运动分类',
    `calories_per_minute` DECIMAL(8,2) NOT NULL COMMENT '每分钟消耗热量(千卡)',
    `description` VARCHAR(500) COMMENT '运动描述',
    `image` VARCHAR(255) COMMENT '运动图片',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运动表';

-- 运动记录表
CREATE TABLE IF NOT EXISTS `exercise_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `exercise_id` BIGINT NOT NULL COMMENT '运动ID',
    `record_date` DATE NOT NULL COMMENT '记录日期',
    `duration` INT NOT NULL COMMENT '运动时长(分钟)',
    `calories` DECIMAL(8,2) NOT NULL COMMENT '消耗热量(千卡)',
    `note` VARCHAR(500) COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_record_date` (`record_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运动记录表';

-- 目标表
CREATE TABLE IF NOT EXISTS `goal` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `goal_type` VARCHAR(50) NOT NULL COMMENT '目标类型：weight-减重，body_fat-减脂，exercise-运动目标',
    `target_value` DECIMAL(8,2) NOT NULL COMMENT '目标值',
    `start_value` DECIMAL(8,2) COMMENT '起始值',
    `start_date` DATE NOT NULL COMMENT '开始日期',
    `end_date` DATE NOT NULL COMMENT '结束日期',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0-进行中，1-已完成，2-已放弃',
    `note` VARCHAR(500) COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='目标表';

-- 插入测试用户数据
INSERT INTO `user` (`username`, `password`, `nickname`, `email`, `phone`, `gender`, `age`, `height`, `weight`, `activity_level`)
VALUES ('admin', '$2a$10$nL.gRbEBHQXA8VZrXVYJqOQewcwyHlHkbxvrWP4oFhKXvacFAGE0W', '健康达人', 'admin@health.com', '13800138000', 1, 28, 175.0, 76.5, 3);

-- 插入食物数据
INSERT INTO `food` (`name`, `category`, `calories`, `protein`, `carbohydrate`, `fat`, `fiber`) VALUES
('米饭', '主食', 116.0, 2.6, 25.9, 0.3, 0.3),
('面条', '主食', 110.0, 3.2, 24.3, 0.4, 0.4),
('馒头', '主食', 221.0, 7.0, 45.0, 1.1, 1.3),
('鸡蛋', '蛋类', 144.0, 13.3, 2.8, 8.8, 0),
('牛奶', '奶类', 54.0, 3.0, 3.4, 3.2, 0),
('苹果', '水果', 52.0, 0.2, 13.8, 0.2, 1.2),
('香蕉', '水果', 93.0, 1.4, 20.8, 0.2, 1.2),
('鸡胸肉', '肉类', 133.0, 19.4, 2.5, 5.0, 0),
('牛肉', '肉类', 125.0, 20.0, 0.2, 4.2, 0),
('三文鱼', '鱼类', 139.0, 20.0, 0, 6.3, 0),
('西兰花', '蔬菜', 33.0, 4.1, 4.3, 0.6, 1.6),
('番茄', '蔬菜', 15.0, 0.9, 3.3, 0.2, 0.5),
('黄瓜', '蔬菜', 15.0, 0.8, 2.4, 0.2, 0.5);

-- 插入运动数据
INSERT INTO `exercise` (`name`, `category`, `calories_per_minute`, `description`) VALUES
('跑步', '有氧运动', 10.0, '中等速度跑步，约8km/h'),
('快走', '有氧运动', 6.0, '快速步行，约6km/h'),
('游泳', '有氧运动', 12.0, '中等强度游泳'),
('骑自行车', '有氧运动', 8.0, '中等速度骑行，约15km/h'),
('瑜伽', '柔韧训练', 4.0, '初级瑜伽练习'),
('力量训练', '力量训练', 5.0, '中等强度力量训练'),
('跳绳', '有氧运动', 11.0, '中等速度跳绳'),
('篮球', '球类运动', 9.0, '一般篮球运动'),
('羽毛球', '球类运动', 7.0, '一般羽毛球运动'),
('登山', '户外运动', 10.0, '中等强度登山');

-- 插入身体数据（最近30天的减脂过程）
INSERT INTO `body_data` (`user_id`, `record_date`, `weight`, `bmi`, `body_fat_rate`, `muscle_mass`, `water_content`, `note`) VALUES
(1, DATE_SUB(CURDATE(), INTERVAL 30 DAY), 82.5, 26.9, 25.5, 55.0, 55.0, '开始减脂计划'),
(1, DATE_SUB(CURDATE(), INTERVAL 29 DAY), 82.3, 26.8, 25.3, 55.2, 55.1, '控制饮食'),
(1, DATE_SUB(CURDATE(), INTERVAL 28 DAY), 82.1, 26.8, 25.1, 55.3, 55.2, '增加运动'),
(1, DATE_SUB(CURDATE(), INTERVAL 27 DAY), 81.8, 26.7, 24.9, 55.5, 55.3, '坚持中'),
(1, DATE_SUB(CURDATE(), INTERVAL 26 DAY), 81.5, 26.6, 24.7, 55.6, 55.4, NULL),
(1, DATE_SUB(CURDATE(), INTERVAL 25 DAY), 81.2, 26.5, 24.5, 55.8, 55.5, '效果显现'),
(1, DATE_SUB(CURDATE(), INTERVAL 24 DAY), 81.0, 26.4, 24.3, 56.0, 55.6, NULL),
(1, DATE_SUB(CURDATE(), INTERVAL 23 DAY), 80.8, 26.3, 24.1, 56.1, 55.7, NULL),
(1, DATE_SUB(CURDATE(), INTERVAL 22 DAY), 80.5, 26.2, 23.9, 56.3, 55.8, '突破80.5'),
(1, DATE_SUB(CURDATE(), INTERVAL 21 DAY), 80.3, 26.2, 23.7, 56.4, 55.9, NULL),
(1, DATE_SUB(CURDATE(), INTERVAL 20 DAY), 80.1, 26.1, 23.5, 56.6, 56.0, '三周成果'),
(1, DATE_SUB(CURDATE(), INTERVAL 19 DAY), 79.8, 26.0, 23.3, 56.8, 56.1, NULL),
(1, DATE_SUB(CURDATE(), INTERVAL 18 DAY), 79.6, 25.9, 23.1, 57.0, 56.2, NULL),
(1, DATE_SUB(CURDATE(), INTERVAL 17 DAY), 79.4, 25.9, 22.9, 57.1, 56.3, NULL),
(1, DATE_SUB(CURDATE(), INTERVAL 16 DAY), 79.2, 25.8, 22.7, 57.3, 56.4, NULL),
(1, DATE_SUB(CURDATE(), INTERVAL 15 DAY), 79.0, 25.7, 22.5, 57.5, 56.5, '半个月达标'),
(1, DATE_SUB(CURDATE(), INTERVAL 14 DAY), 78.8, 25.7, 22.4, 57.6, 56.6, NULL),
(1, DATE_SUB(CURDATE(), INTERVAL 13 DAY), 78.6, 25.6, 22.2, 57.8, 56.7, NULL),
(1, DATE_SUB(CURDATE(), INTERVAL 12 DAY), 78.4, 25.5, 22.0, 58.0, 56.8, NULL),
(1, DATE_SUB(CURDATE(), INTERVAL 11 DAY), 78.3, 25.5, 21.9, 58.1, 56.9, NULL),
(1, DATE_SUB(CURDATE(), INTERVAL 10 DAY), 78.1, 25.4, 21.7, 58.3, 57.0, NULL),
(1, DATE_SUB(CURDATE(), INTERVAL 9 DAY), 78.0, 25.4, 21.6, 58.4, 57.1, NULL),
(1, DATE_SUB(CURDATE(), INTERVAL 8 DAY), 77.8, 25.3, 21.4, 58.6, 57.2, NULL),
(1, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 77.6, 25.3, 21.3, 58.7, 57.3, '一周前'),
(1, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 77.5, 25.2, 21.1, 58.9, 57.4, NULL),
(1, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 77.3, 25.2, 21.0, 59.0, 57.5, NULL),
(1, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 77.2, 25.1, 20.9, 59.1, 57.6, NULL),
(1, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 77.0, 25.1, 20.7, 59.3, 57.7, NULL),
(1, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 76.8, 25.0, 20.6, 59.4, 57.8, '前天'),
(1, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 76.6, 24.9, 20.4, 59.6, 57.9, '昨天'),
(1, CURDATE(), 76.5, 24.9, 20.3, 59.7, 58.0, '今天');

-- 插入饮食记录（最近7天）
INSERT INTO `diet_record` (`user_id`, `food_id`, `record_date`, `meal_type`, `amount`, `calories`, `protein`, `carbohydrate`, `fat`) VALUES
(1, 4, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 'breakfast', 100, 144, 13.3, 2.8, 8.8),
(1, 5, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 'breakfast', 250, 135, 7.5, 8.5, 8.0),
(1, 1, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 'breakfast', 100, 116, 2.6, 25.9, 0.3),
(1, 8, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 'lunch', 150, 199.5, 29.1, 3.75, 7.5),
(1, 11, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 'lunch', 200, 66, 8.2, 8.6, 1.2),
(1, 1, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 'lunch', 150, 174, 3.9, 38.85, 0.45),
(1, 9, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 'dinner', 100, 125, 20.0, 0.2, 4.2),
(1, 12, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 'dinner', 150, 22.5, 1.35, 4.95, 0.3),
(1, 6, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 'snack', 150, 78, 0.3, 20.7, 0.3);

-- 插入运动记录（最近7天）
INSERT INTO `exercise_record` (`user_id`, `exercise_id`, `record_date`, `duration`, `calories`) VALUES
(1, 1, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 45, 450),
(1, 6, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 30, 150),
(1, 7, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 20, 220),
(1, 3, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 40, 480),
(1, 5, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 60, 240),
(1, 2, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 30, 180),
(1, 6, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 45, 225),
(1, 1, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 40, 400),
(1, 8, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 40, 360),
(1, 7, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 15, 165),
(1, 4, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 50, 400),
(1, 6, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 30, 150),
(1, 3, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 35, 420),
(1, 9, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 30, 210),
(1, 1, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 45, 450),
(1, 6, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 40, 200),
(1, 2, CURDATE(), 40, 240),
(1, 5, CURDATE(), 45, 180);

-- 插入目标数据
INSERT INTO `goal` (`user_id`, `goal_type`, `target_value`, `start_value`, `start_date`, `end_date`, `status`) VALUES
(1, 'weight', 72.0, 82.5, DATE_SUB(CURDATE(), INTERVAL 30 DAY), DATE_ADD(CURDATE(), INTERVAL 60 DAY), 0),
(1, 'body_fat', 18.0, 25.5, DATE_SUB(CURDATE(), INTERVAL 30 DAY), DATE_ADD(CURDATE(), INTERVAL 90 DAY), 0);
