/*
 Navicat Premium Dump SQL

 Source Server Type    : MariaDB
 Target Server Type    : MariaDB
 File Encoding         : 65001

 Date: 10/04/2025
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sch_shift
-- ----------------------------
DROP TABLE IF EXISTS `sch_shift`;
CREATE TABLE `sch_shift` (
  `shift_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '班次ID',
  `name` varchar(100) NOT NULL COMMENT '班次名称',
  `code` varchar(50) NOT NULL COMMENT '班次编码',
  `start_time` time NOT NULL COMMENT '开始时间',
  `end_time` time NOT NULL COMMENT '结束时间',
  `type` varchar(20) NOT NULL COMMENT '班次类型：MORNING-早班, AFTERNOON-午班, NIGHT-夜班, FLEXIBLE-弹性班, OVERTIME-加班',
  `is_cross_day` bit(1) DEFAULT b'0' COMMENT '是否跨天',
  `color` varchar(20) DEFAULT '#1890ff' COMMENT '显示颜色',
  `enabled` bit(1) DEFAULT b'1' COMMENT '是否启用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`shift_id`) USING BTREE,
  UNIQUE KEY `uk_shift_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='班次表';

-- ----------------------------
-- Records of sch_shift
-- ----------------------------
BEGIN;
INSERT INTO `sch_shift` (`name`, `code`, `start_time`, `end_time`, `type`, `is_cross_day`, `color`, `enabled`, `remark`, `create_time`, `update_time`) VALUES
('早班', 'MORNING', '08:00:00', '16:00:00', 'MORNING', b'0', '#52c41a', b'1', '标准早班', NOW(), NOW()),
('午班', 'AFTERNOON', '16:00:00', '24:00:00', 'AFTERNOON', b'0', '#1890ff', b'1', '标准午班', NOW(), NOW()),
('夜班', 'NIGHT', '00:00:00', '08:00:00', 'NIGHT', b'1', '#722ed1', b'1', '标准夜班', NOW(), NOW()),
('弹性班', 'FLEXIBLE', '09:00:00', '18:00:00', 'FLEXIBLE', b'0', '#fa8c16', b'1', '弹性工作制', NOW(), NOW());
COMMIT;

-- ----------------------------
-- Table structure for sch_shift_rotation
-- ----------------------------
DROP TABLE IF EXISTS `sch_shift_rotation`;
CREATE TABLE `sch_shift_rotation` (
  `rotation_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '轮班规则ID',
  `name` varchar(100) NOT NULL COMMENT '规则名称',
  `code` varchar(50) NOT NULL COMMENT '规则编码',
  `type` varchar(20) NOT NULL COMMENT '轮班类型：AB-AB轮班, ABC-三班倒, FOUR_SHIFT-四班三倒, CUSTOM-自定义',
  `cycle_days` int(11) NOT NULL COMMENT '周期天数',
  `rotation_pattern` text NOT NULL COMMENT '轮班模式（JSON数组，如：["MORNING","AFTERNOON","NIGHT"]）',
  `rest_days` int(11) DEFAULT 0 COMMENT '休息天数',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '适用部门ID',
  `user_ids` varchar(1000) DEFAULT NULL COMMENT '适用用户ID列表，逗号分隔',
  `dept_ids` varchar(1000) DEFAULT NULL COMMENT '适用部门ID列表，逗号分隔',
  `start_date` date DEFAULT NULL COMMENT '规则生效开始日期',
  `end_date` date DEFAULT NULL COMMENT '规则生效结束日期',
  `enabled` bit(1) DEFAULT b'1' COMMENT '是否启用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`rotation_id`) USING BTREE,
  UNIQUE KEY `uk_rotation_code` (`code`),
  KEY `fk_rotation_dept` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='轮班规则表';

-- ----------------------------
-- Records of sch_shift_rotation
-- ----------------------------
BEGIN;
INSERT INTO `sch_shift_rotation` (`name`, `code`, `type`, `cycle_days`, `rotation_pattern`, `rest_days`, `enabled`, `remark`, `create_time`, `update_time`) VALUES
('AB轮班', 'AB_ROTATION', 'AB', 2, '[{"shiftId":1,"shiftCode":"MORNING"},{"shiftId":2,"shiftCode":"AFTERNOON"}]', 0, b'1', 'A/B两班倒', NOW(), NOW()),
('三班倒', 'THREE_SHIFT', 'ABC', 3, '[{"shiftId":1,"shiftCode":"MORNING"},{"shiftId":2,"shiftCode":"AFTERNOON"},{"shiftId":3,"shiftCode":"NIGHT"}]', 0, b'1', '早中晚三班倒', NOW(), NOW()),
('四班三倒', 'FOUR_SHIFT', 'FOUR_SHIFT', 4, '[{"shiftId":1,"shiftCode":"MORNING"},{"shiftId":2,"shiftCode":"AFTERNOON"},{"shiftId":3,"shiftCode":"NIGHT"},{"shiftId":0,"shiftCode":"REST"}]', 1, b'1', '四班三运转', NOW(), NOW());
COMMIT;

-- ----------------------------
-- Table structure for sch_schedule
-- ----------------------------
DROP TABLE IF EXISTS `sch_schedule`;
CREATE TABLE `sch_schedule` (
  `schedule_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '排班ID',
  `user_id` bigint(20) NOT NULL COMMENT '员工ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  `shift_id` bigint(20) NOT NULL COMMENT '班次ID',
  `schedule_date` date NOT NULL COMMENT '排班日期',
  `start_time` datetime DEFAULT NULL COMMENT '实际开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '实际结束时间',
  `status` varchar(20) NOT NULL DEFAULT 'NORMAL' COMMENT '状态：NORMAL-正常, TEMPORARY-临时, ADJUSTED-调整, CANCELLED-取消',
  `source_type` varchar(20) NOT NULL DEFAULT 'AUTO' COMMENT '来源类型：AUTO-自动生成, MANUAL-手动, BATCH-批量, IMPORT-导入',
  `conflict_flag` bit(1) DEFAULT b'0' COMMENT '是否有冲突',
  `conflict_msg` varchar(500) DEFAULT NULL COMMENT '冲突信息',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`schedule_id`) USING BTREE,
  UNIQUE KEY `uk_user_date` (`user_id`, `schedule_date`),
  KEY `idx_schedule_date` (`schedule_date`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_shift_id` (`shift_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='排班表';

-- ----------------------------
-- Table structure for sch_schedule_rule
-- ----------------------------
DROP TABLE IF EXISTS `sch_schedule_rule`;
CREATE TABLE `sch_schedule_rule` (
  `rule_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `name` varchar(100) NOT NULL COMMENT '规则名称',
  `type` varchar(20) NOT NULL COMMENT '规则类型：RESTRICTION-限制, PREFERENCE-偏好, STATUTORY-法定',
  `expression` varchar(500) NOT NULL COMMENT '规则表达式',
  `priority` int(11) DEFAULT 0 COMMENT '优先级',
  `enabled` bit(1) DEFAULT b'1' COMMENT '是否启用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`rule_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='排班规则表';

-- ----------------------------
-- Records of sch_schedule_rule
-- ----------------------------
BEGIN;
INSERT INTO `sch_schedule_rule` (`name`, `type`, `expression`, `priority`, `enabled`, `remark`, `create_time`, `update_time`) VALUES
('连续工作限制', 'RESTRICTION', 'max_consecutive_days:6', 1, b'1', '连续工作不超过6天', NOW(), NOW()),
('夜班后休息', 'RESTRICTION', 'rest_after_night:1', 2, b'1', '夜班后至少休息1天', NOW(), NOW()),
('每月加班限制', 'RESTRICTION', 'max_overtime_hours:36', 3, b'1', '每月加班不超过36小时', NOW(), NOW());
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
