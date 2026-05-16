-- 插入更多食物数据
INSERT INTO `food` (`name`, `category`, `calories`, `protein`, `carbohydrate`, `fat`, `fiber`, `unit`) VALUES
('全麦面包', '主食', 246.0, 9.5, 46.0, 2.8, 6.0, 'g'),
('燕麦片', '主食', 367.0, 13.2, 66.0, 5.6, 10.3, 'g'),
('红薯', '主食', 99.0, 1.1, 23.1, 0.1, 1.6, 'g'),
('土豆', '主食', 81.0, 2.0, 17.8, 0.2, 1.1, 'g'),
('猪里脊', '肉类', 155.0, 20.3, 0.5, 7.9, 0, 'g'),
('鸡胸肉', '肉类', 133.0, 19.4, 2.5, 5.0, 0, 'g'),
('牛肉', '肉类', 125.0, 20.0, 0.2, 4.2, 0, 'g'),
('三文鱼', '鱼类', 139.0, 20.0, 0, 6.3, 0, 'g'),
('虾', '鱼类', 95.0, 18.6, 2.8, 0.8, 0, 'g'),
('豆腐', '豆类', 81.0, 8.1, 4.2, 3.7, 0.4, 'g'),
('鸡蛋', '蛋类', 144.0, 13.3, 2.8, 8.8, 0, 'g'),
('牛奶', '奶类', 54.0, 3.0, 3.4, 3.2, 0, 'g'),
('酸奶', '奶类', 72.0, 2.5, 9.3, 2.7, 0, 'g'),
('西兰花', '蔬菜', 33.0, 4.1, 4.3, 0.6, 1.6, 'g'),
('菠菜', '蔬菜', 23.0, 2.9, 3.6, 0.4, 2.2, 'g'),
('番茄', '蔬菜', 15.0, 0.9, 3.3, 0.2, 0.5, 'g'),
('黄瓜', '蔬菜', 15.0, 0.8, 2.4, 0.2, 0.5, 'g'),
('胡萝卜', '蔬菜', 25.0, 1.0, 5.6, 0.2, 2.6, 'g'),
('苹果', '水果', 52.0, 0.2, 13.8, 0.2, 1.2, 'g'),
('香蕉', '水果', 93.0, 1.4, 20.8, 0.2, 1.2, 'g'),
('橙子', '水果', 47.0, 0.8, 11.8, 0.2, 1.6, 'g'),
('葡萄', '水果', 69.0, 0.5, 16.3, 0.3, 0.9, 'g'),
('西瓜', '水果', 26.0, 0.6, 5.8, 0.1, 0.3, 'g'),
('坚果混合', '坚果', 580.0, 18.0, 15.0, 52.0, 8.0, 'g');

-- 插入更多运动数据
INSERT INTO `exercise` (`name`, `category`, `calories_per_minute`, `description`) VALUES
('慢跑', '有氧运动', 11.0, '慢速跑步，约7km/h'),
('快跑', '有氧运动', 13.0, '快速跑步，约10km/h'),
('动感单车', '有氧运动', 10.0, '室内单车运动'),
('健身操', '有氧运动', 8.0, '中等强度健身操'),
('普拉提', '柔韧训练', 5.0, '普拉提训练'),
('拉伸训练', '柔韧训练', 3.0, '拉伸放松训练'),
('俯卧撑', '力量训练', 7.0, '标准俯卧撑'),
('深蹲', '力量训练', 6.0, '标准深蹲训练'),
('平板支撑', '力量训练', 4.0, '核心力量训练'),
('引体向上', '力量训练', 8.0, '背部训练'),
('游泳', '有氧运动', 12.0, '中等强度游泳'),
('乒乓球', '球类运动', 5.0, '一般乒乓球运动'),
('网球', '球类运动', 8.0, '一般网球运动'),
('排球', '球类运动', 6.0, '一般排球运动');

-- 为admin用户插入身体数据（最近30天的减脂过程）
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
-- 第7天
INSERT INTO `diet_record` (`user_id`, `food_id`, `record_date`, `meal_type`, `amount`, `calories`, `protein`, `carbohydrate`, `fat`, `note`) VALUES
(1, 11, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 'breakfast', 100, 144, 13.3, 2.8, 8.8, '煮鸡蛋'),
(1, 12, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 'breakfast', 250, 135, 7.5, 8.5, 8.0, '牛奶'),
(1, 2, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 'breakfast', 100, 116, 2.6, 25.9, 0.3, '米饭'),
(1, 8, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 'lunch', 150, 199.5, 29.1, 3.75, 7.5, '鸡胸肉'),
(1, 15, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 'lunch', 200, 66, 8.2, 8.6, 1.2, '西兰花'),
(1, 2, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 'lunch', 150, 174, 3.9, 38.85, 0.45, '米饭'),
(1, 10, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 'dinner', 200, 162, 16.2, 8.4, 7.4, '豆腐'),
(1, 17, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 'dinner', 150, 22.5, 1.35, 4.95, 0.3, '番茄'),
(1, 19, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 'snack', 150, 78, 0.3, 20.7, 0.3, '苹果');

-- 第6天
INSERT INTO `diet_record` (`user_id`, `food_id`, `record_date`, `meal_type`, `amount`, `calories`, `protein`, `carbohydrate`, `fat`, `note`) VALUES
(1, 3, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 'breakfast', 100, 367, 13.2, 66.0, 5.6, '燕麦片'),
(1, 12, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 'breakfast', 200, 108, 6.0, 6.8, 6.4, '牛奶'),
(1, 19, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 'breakfast', 100, 52, 0.2, 13.8, 0.2, '苹果'),
(1, 9, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 'lunch', 200, 190, 37.2, 5.6, 1.6, '虾'),
(1, 16, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 'lunch', 200, 46, 5.8, 7.2, 0.8, '菠菜'),
(1, 2, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 'lunch', 120, 139.2, 3.12, 31.08, 0.36, '米饭'),
(1, 8, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 'dinner', 120, 159.6, 23.28, 3.0, 6.0, '鸡胸肉'),
(1, 18, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 'dinner', 150, 37.5, 1.5, 8.4, 0.3, '胡萝卜'),
(1, 20, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 'snack', 100, 93, 1.4, 20.8, 0.2, '香蕉');

-- 第5天
INSERT INTO `diet_record` (`user_id`, `food_id`, `record_date`, `meal_type`, `amount`, `calories`, `protein`, `carbohydrate`, `fat`, `note`) VALUES
(1, 11, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'breakfast', 100, 144, 13.3, 2.8, 8.8, '煮鸡蛋'),
(1, 14, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'breakfast', 200, 178, 6.0, 34.0, 2.8, '全麦面包'),
(1, 13, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'breakfast', 150, 108, 3.75, 13.95, 4.05, '酸奶'),
(1, 7, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'lunch', 150, 187.5, 30.0, 0.3, 6.3, '牛肉'),
(1, 15, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'lunch', 200, 66, 8.2, 8.6, 1.2, '西兰花'),
(1, 2, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'lunch', 100, 116, 2.6, 25.9, 0.3, '米饭'),
(1, 9, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'dinner', 150, 142.5, 27.9, 4.2, 1.2, '虾'),
(1, 17, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'dinner', 200, 30, 1.8, 6.6, 0.4, '番茄'),
(1, 21, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'snack', 150, 70.5, 1.2, 17.7, 0.3, '橙子');

-- 第4天
INSERT INTO `diet_record` (`user_id`, `food_id`, `record_date`, `meal_type`, `amount`, `calories`, `protein`, `carbohydrate`, `fat`, `note`) VALUES
(1, 3, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'breakfast', 80, 293.6, 10.56, 52.8, 4.48, '燕麦片'),
(1, 12, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'breakfast', 250, 135, 7.5, 8.5, 8.0, '牛奶'),
(1, 20, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'breakfast', 100, 93, 1.4, 20.8, 0.2, '香蕉'),
(1, 8, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'lunch', 180, 239.4, 34.92, 4.5, 9.0, '鸡胸肉'),
(1, 18, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'lunch', 200, 50, 2.0, 11.2, 0.4, '胡萝卜'),
(1, 2, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'lunch', 150, 174, 3.9, 38.85, 0.45, '米饭'),
(1, 10, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'dinner', 250, 202.5, 20.25, 10.5, 9.25, '豆腐'),
(1, 16, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'dinner', 200, 46, 5.8, 7.2, 0.8, '菠菜'),
(1, 22, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'snack', 100, 69, 0.5, 16.3, 0.3, '葡萄');

-- 第3天
INSERT INTO `diet_record` (`user_id`, `food_id`, `record_date`, `meal_type`, `amount`, `calories`, `protein`, `carbohydrate`, `fat`, `note`) VALUES
(1, 11, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'breakfast', 100, 144, 13.3, 2.8, 8.8, '煮鸡蛋'),
(1, 14, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'breakfast', 150, 369, 14.25, 69.0, 4.2, '全麦面包'),
(1, 12, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'breakfast', 200, 108, 6.0, 6.8, 6.4, '牛奶'),
(1, 7, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'lunch', 120, 150, 24.0, 0.24, 5.04, '牛肉'),
(1, 15, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'lunch', 200, 66, 8.2, 8.6, 1.2, '西兰花'),
(1, 2, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'lunch', 100, 116, 2.6, 25.9, 0.3, '米饭'),
(1, 9, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'dinner', 180, 171, 33.48, 5.04, 1.44, '虾'),
(1, 17, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'dinner', 150, 22.5, 1.35, 4.95, 0.3, '番茄'),
(1, 23, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'snack', 200, 52, 1.2, 11.6, 0.2, '西瓜');

-- 第2天
INSERT INTO `diet_record` (`user_id`, `food_id`, `record_date`, `meal_type`, `amount`, `calories`, `protein`, `carbohydrate`, `fat`, `note`) VALUES
(1, 3, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'breakfast', 100, 367, 13.2, 66.0, 5.6, '燕麦片'),
(1, 13, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'breakfast', 200, 144, 5.0, 18.6, 5.4, '酸奶'),
(1, 19, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'breakfast', 100, 52, 0.2, 13.8, 0.2, '苹果'),
(1, 8, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'lunch', 200, 266, 38.8, 5.0, 10.0, '鸡胸肉'),
(1, 18, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'lunch', 150, 37.5, 1.5, 8.4, 0.3, '胡萝卜'),
(1, 2, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'lunch', 120, 139.2, 3.12, 31.08, 0.36, '米饭'),
(1, 7, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'dinner', 100, 125, 20.0, 0.2, 4.2, '牛肉'),
(1, 16, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'dinner', 200, 46, 5.8, 7.2, 0.8, '菠菜'),
(1, 24, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'snack', 20, 116, 3.6, 3.0, 10.4, '坚果');

-- 第1天（昨天）
INSERT INTO `diet_record` (`user_id`, `food_id`, `record_date`, `meal_type`, `amount`, `calories`, `protein`, `carbohydrate`, `fat`, `note`) VALUES
(1, 11, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'breakfast', 100, 144, 13.3, 2.8, 8.8, '煮鸡蛋'),
(1, 12, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'breakfast', 250, 135, 7.5, 8.5, 8.0, '牛奶'),
(1, 14, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'breakfast', 100, 246, 9.5, 46.0, 2.8, '全麦面包'),
(1, 9, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'lunch', 200, 190, 37.2, 5.6, 1.6, '虾'),
(1, 15, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'lunch', 200, 66, 8.2, 8.6, 1.2, '西兰花'),
(1, 2, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'lunch', 100, 116, 2.6, 25.9, 0.3, '米饭'),
(1, 8, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'dinner', 150, 199.5, 29.1, 3.75, 7.5, '鸡胸肉'),
(1, 17, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'dinner', 200, 30, 1.8, 6.6, 0.4, '番茄'),
(1, 21, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'snack', 100, 47, 0.8, 11.8, 0.2, '橙子');

-- 今天
INSERT INTO `diet_record` (`user_id`, `food_id`, `record_date`, `meal_type`, `amount`, `calories`, `protein`, `carbohydrate`, `fat`, `note`) VALUES
(1, 3, CURDATE(), 'breakfast', 100, 367, 13.2, 66.0, 5.6, '燕麦片'),
(1, 12, CURDATE(), 'breakfast', 200, 108, 6.0, 6.8, 6.4, '牛奶'),
(1, 19, CURDATE(), 'breakfast', 150, 78, 0.3, 20.7, 0.3, '苹果'),
(1, 8, CURDATE(), 'lunch', 180, 239.4, 34.92, 4.5, 9.0, '鸡胸肉'),
(1, 15, CURDATE(), 'lunch', 200, 66, 8.2, 8.6, 1.2, '西兰花'),
(1, 2, CURDATE(), 'lunch', 120, 139.2, 3.12, 31.08, 0.36, '米饭'),
(1, 7, CURDATE(), 'dinner', 120, 150, 24.0, 0.24, 5.04, '牛肉'),
(1, 16, CURDATE(), 'dinner', 200, 46, 5.8, 7.2, 0.8, '菠菜');

-- 插入运动记录（最近7天）
-- 第7天
INSERT INTO `exercise_record` (`user_id`, `exercise_id`, `record_date`, `duration`, `calories`, `note`) VALUES
(1, 1, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 45, 495, '晨跑'),
(1, 6, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 30, 150, '力量训练'),
(1, 7, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 20, 220, '跳绳');

-- 第6天
INSERT INTO `exercise_record` (`user_id`, `exercise_id`, `record_date`, `duration`, `calories`, `note`) VALUES
(1, 3, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 40, 480, '游泳'),
(1, 5, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 60, 240, '瑜伽');

-- 第5天
INSERT INTO `exercise_record` (`user_id`, `exercise_id`, `record_date`, `duration`, `calories`, `note`) VALUES
(1, 2, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 30, 180, '快走'),
(1, 6, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 45, 225, '力量训练');

-- 第4天
INSERT INTO `exercise_record` (`user_id`, `exercise_id`, `record_date`, `duration`, `calories`, `note`) VALUES
(1, 1, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 40, 440, '晨跑'),
(1, 8, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 40, 360, '篮球'),
(1, 7, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 15, 165, '跳绳');

-- 第3天
INSERT INTO `exercise_record` (`user_id`, `exercise_id`, `record_date`, `duration`, `calories`, `note`) VALUES
(1, 4, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 50, 400, '骑自行车'),
(1, 6, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 30, 150, '力量训练');

-- 第2天
INSERT INTO `exercise_record` (`user_id`, `exercise_id`, `record_date`, `duration`, `calories`, `note`) VALUES
(1, 3, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 35, 420, '游泳'),
(1, 9, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 30, 210, '羽毛球');

-- 第1天（昨天）
INSERT INTO `exercise_record` (`user_id`, `exercise_id`, `record_date`, `duration`, `calories`, `note`) VALUES
(1, 1, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 45, 495, '晨跑'),
(1, 6, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 40, 200, '力量训练');

-- 今天
INSERT INTO `exercise_record` (`user_id`, `exercise_id`, `record_date`, `duration`, `calories`, `note`) VALUES
(1, 2, CURDATE(), 40, 240, '快走'),
(1, 5, CURDATE(), 45, 180, '瑜伽');

-- 插入目标数据
INSERT INTO `goal` (`user_id`, `goal_type`, `target_value`, `start_value`, `start_date`, `end_date`, `status`, `note`) VALUES
(1, 'weight', 72.0, 82.5, DATE_SUB(CURDATE(), INTERVAL 30 DAY), DATE_ADD(CURDATE(), INTERVAL 60 DAY), 0, '减重10.5公斤目标'),
(1, 'body_fat', 18.0, 25.5, DATE_SUB(CURDATE(), INTERVAL 30 DAY), DATE_ADD(CURDATE(), INTERVAL 90 DAY), 0, '体脂率降到18%以下'),
(1, 'weight', 75.0, 82.5, DATE_SUB(CURDATE(), INTERVAL 60 DAY), DATE_SUB(CURDATE(), INTERVAL 30 DAY), 1, '第一阶段减重目标已完成');

-- 更新用户信息
UPDATE `user` SET 
  `nickname` = '健康达人',
  `gender` = 1,
  `age` = 28,
  `height` = 175.0,
  `weight` = 76.5,
  `activity_level` = 3,
  `email` = 'admin@health.com'
WHERE `id` = 1;
