-- 排班管理模块数据库表结构

-- 1. 班次表
DROP TABLE IF EXISTS `sch_shift`;
CREATE TABLE `sch_shift` (
  `shift_id` bigint NOT NULL AUTO_INCREMENT COMMENT '班次ID',
  `shift_name` varchar(50) NOT NULL COMMENT '班次名称',
  `shift_code` varchar(20) DEFAULT NULL COMMENT '班次编码',
  `shift_type` varchar(20) NOT NULL COMMENT '班次类型: MORNING-早班, AFTERNOON-中班, NIGHT-夜班, FLEXIBLE-弹性班, REST-休息',
  `start_time` time DEFAULT NULL COMMENT '开始时间',
  `end_time` time DEFAULT NULL COMMENT '结束时间',
  `break_duration` int DEFAULT '0' COMMENT '休息时长(分钟)',
  `color` varchar(20) DEFAULT '#1890ff' COMMENT '显示颜色',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '是否启用',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`shift_id`),
  UNIQUE KEY `uk_shift_code` (`shift_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='班次表';

-- 初始化默认班次数据
INSERT INTO `sch_shift` (`shift_name`, `shift_code`, `shift_type`, `start_time`, `end_time`, `break_duration`, `color`, `enabled`, `description`) VALUES
('早班', 'MORNING', 'MORNING', '08:00:00', '16:00:00', 60, '#52c41a', 1, '早上8点到下午4点'),
('中班', 'AFTERNOON', 'AFTERNOON', '16:00:00', '00:00:00', 60, '#1890ff', 1, '下午4点到凌晨12点'),
('夜班', 'NIGHT', 'NIGHT', '00:00:00', '08:00:00', 60, '#722ed1', 1, '凌晨12点到早上8点'),
('弹性班', 'FLEXIBLE', 'FLEXIBLE', '09:00:00', '18:00:00', 60, '#faad14', 1, '弹性工作时间'),
('休息', 'REST', 'REST', NULL, NULL, 0, '#ff4d4f', 1, '休息日');

-- 2. 排班表
DROP TABLE IF EXISTS `sch_schedule`;
CREATE TABLE `sch_schedule` (
  `schedule_id` bigint NOT NULL AUTO_INCREMENT COMMENT '排班ID',
  `user_id` bigint NOT NULL COMMENT '员工ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `shift_id` bigint DEFAULT NULL COMMENT '班次ID',
  `schedule_date` date NOT NULL COMMENT '排班日期',
  `schedule_type` varchar(20) DEFAULT 'AUTO' COMMENT '排班类型: AUTO-自动生成, MANUAL-手动调整',
  `is_locked` tinyint(1) DEFAULT '0' COMMENT '是否锁定(锁定后不可自动覆盖)',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`schedule_id`),
  KEY `idx_user_date` (`user_id`, `schedule_date`),
  KEY `idx_dept_date` (`dept_id`, `schedule_date`),
  KEY `idx_schedule_date` (`schedule_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='排班表';

-- 3. 轮班规则表
DROP TABLE IF EXISTS `sch_shift_rule`;
CREATE TABLE `sch_shift_rule` (
  `rule_id` bigint NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `rule_name` varchar(100) NOT NULL COMMENT '规则名称',
  `rule_code` varchar(50) DEFAULT NULL COMMENT '规则编码',
  `rule_type` varchar(20) NOT NULL COMMENT '规则类型: TWO_SHIFT-两班倒, THREE_SHIFT-三班倒, CUSTOM-自定义',
  `cycle_days` int NOT NULL DEFAULT '7' COMMENT '周期天数',
  `dept_id` bigint DEFAULT NULL COMMENT '适用部门ID(为空则通用)',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '是否启用',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`rule_id`),
  UNIQUE KEY `uk_rule_code` (`rule_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='轮班规则表';

-- 4. 轮班规则明细表
DROP TABLE IF EXISTS `sch_shift_rule_item`;
CREATE TABLE `sch_shift_rule_item` (
  `item_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `rule_id` bigint NOT NULL COMMENT '规则ID',
  `day_index` int NOT NULL COMMENT '周期内第几天(1开始)',
  `shift_id` bigint NOT NULL COMMENT '班次ID',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`item_id`),
  KEY `idx_rule_id` (`rule_id`),
  CONSTRAINT `fk_rule_item_rule` FOREIGN KEY (`rule_id`) REFERENCES `sch_shift_rule` (`rule_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='轮班规则明细表';

-- 5. 员工轮班规则关联表
DROP TABLE IF EXISTS `sch_user_rule`;
CREATE TABLE `sch_user_rule` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '员工ID',
  `rule_id` bigint NOT NULL COMMENT '规则ID',
  `start_date` date DEFAULT NULL COMMENT '生效开始日期',
  `end_date` date DEFAULT NULL COMMENT '生效结束日期',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '是否启用',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_rule` (`user_id`, `rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='员工轮班规则关联表';

-- 初始化默认轮班规则
-- A/B轮班规则
INSERT INTO `sch_shift_rule` (`rule_name`, `rule_code`, `rule_type`, `cycle_days`, `enabled`, `description`) VALUES
('A/B轮班', 'AB_SHIFT', 'TWO_SHIFT', 14, 1, 'A/B两班倒，两周一个周期');

-- 三班倒规则
INSERT INTO `sch_shift_rule` (`rule_name`, `rule_code`, `rule_type`, `cycle_days`, `enabled`, `description`) VALUES
('三班倒', 'THREE_SHIFT', 'THREE_SHIFT', 21, 1, '早中夜三班倒，三周一个周期');

-- 假设班次ID为: 早班=1, 中班=2, 夜班=3, 弹性班=4, 休息=5
-- A/B轮班示例: 第一周早班，第二周中班
INSERT INTO `sch_shift_rule_item` (`rule_id`, `day_index`, `shift_id`) VALUES
(1, 1, 1), (1, 2, 1), (1, 3, 1), (1, 4, 1), (1, 5, 1), (1, 6, 5), (1, 7, 5),
(1, 8, 2), (1, 9, 2), (1, 10, 2), (1, 11, 2), (1, 12, 2), (1, 13, 5), (1, 14, 5);

-- 三班倒示例: 每周倒一次班
INSERT INTO `sch_shift_rule_item` (`rule_id`, `day_index`, `shift_id`) VALUES
(2, 1, 1), (2, 2, 1), (2, 3, 1), (2, 4, 1), (2, 5, 1), (2, 6, 5), (2, 7, 5),
(2, 8, 2), (2, 9, 2), (2, 10, 2), (2, 11, 2), (2, 12, 2), (2, 13, 5), (2, 14, 5),
(2, 15, 3), (2, 16, 3), (2, 17, 3), (2, 18, 3), (2, 19, 3), (2, 20, 5), (2, 21, 5);

-- 6. 排班日历视图(可选，用于快速查询)
-- 建议创建存储过程或在应用层实现日历生成功能
