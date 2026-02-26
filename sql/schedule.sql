/*
 Navicat Premium Dump SQL

 Source Server Type    : MariaDB
 File Encoding         : 65001

 Date: 2026-02-26
 Description: 员工排班管理模块数据库表结构
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for schedule_shift_type
-- ----------------------------
DROP TABLE IF EXISTS `schedule_shift_type`;
CREATE TABLE `schedule_shift_type` (
  `shift_type_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '班次类型ID',
  `name` varchar(100) NOT NULL COMMENT '班次名称',
  `code` varchar(50) NOT NULL COMMENT '班次编码',
  `color` varchar(20) DEFAULT '#1890ff' COMMENT '显示颜色',
  `start_time` time DEFAULT NULL COMMENT '上班时间',
  `end_time` time DEFAULT NULL COMMENT '下班时间',
  `work_hours` decimal(4,2) DEFAULT 8.00 COMMENT '工作时长（小时）',
  `is_rest` bit(1) DEFAULT b'0' COMMENT '是否休息',
  `is_enabled` bit(1) DEFAULT b'1' COMMENT '是否启用',
  `sort_order` int(11) DEFAULT 0 COMMENT '排序',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`shift_type_id`) USING BTREE,
  UNIQUE KEY `uk_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='班次类型表';

-- ----------------------------
-- Records of schedule_shift_type
-- ----------------------------
BEGIN;
INSERT INTO `schedule_shift_type` (`name`, `code`, `color`, `start_time`, `end_time`, `work_hours`, `is_rest`, `is_enabled`, `sort_order`, `remark`, `create_time`) VALUES
('早班', 'MORNING', '#52c41a', '08:00:00', '17:00:00', 8.00, b'0', b'1', 1, '早班 8:00-17:00', NOW()),
('晚班', 'EVENING', '#faad14', '14:00:00', '23:00:00', 8.00, b'0', b'1', 2, '晚班 14:00-23:00', NOW()),
('夜班', 'NIGHT', '#722ed1', '23:00:00', '08:00:00', 8.00, b'0', b'1', 3, '夜班 23:00-08:00', NOW()),
('弹性班', 'FLEXIBLE', '#13c2c2', NULL, NULL, 8.00, b'0', b'1', 4, '弹性工作时间', NOW()),
('休息', 'REST', '#bfbfbf', NULL, NULL, 0.00, b'1', b'1', 5, '休息日', NOW()),
('全天班', 'FULLDAY', '#f5222d', '08:00:00', '20:00:00', 12.00, b'0', b'1', 6, '全天班 8:00-20:00', NOW()),
('中班', 'AFTERNOON', '#eb2f96', '12:00:00', '21:00:00', 8.00, b'0', b'1', 7, '中班 12:00-21:00', NOW());
COMMIT;

-- ----------------------------
-- Table structure for schedule_rotation_rule
-- ----------------------------
DROP TABLE IF EXISTS `schedule_rotation_rule`;
CREATE TABLE `schedule_rotation_rule` (
  `rule_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `name` varchar(100) NOT NULL COMMENT '规则名称',
  `code` varchar(50) NOT NULL COMMENT '规则编码',
  `rotation_type` varchar(20) NOT NULL COMMENT '轮班类型：TWO_SHIFT-两班倒，THREE_SHIFT-三班倒，FOUR_SHIFT-四班三运转，CUSTOM-自定义',
  `cycle_days` int(11) NOT NULL COMMENT '轮班周期（天）',
  `shift_sequence` varchar(500) NOT NULL COMMENT '班次序列，JSON数组格式，如：["MORNING","EVENING","REST"]',
  `is_enabled` bit(1) DEFAULT b'1' COMMENT '是否启用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`rule_id`) USING BTREE,
  UNIQUE KEY `uk_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='轮班规则表';

-- ----------------------------
-- Records of schedule_rotation_rule
-- ----------------------------
BEGIN;
INSERT INTO `schedule_rotation_rule` (`name`, `code`, `rotation_type`, `cycle_days`, `shift_sequence`, `is_enabled`, `remark`, `create_time`) VALUES
('A/B轮班', 'AB_ROTATION', 'TWO_SHIFT', 4, '["MORNING","MORNING","EVENING","EVENING"]', b'1', 'A/B两班倒：2天早班+2天晚班', NOW()),
('三班倒', 'THREE_SHIFT_ROTATION', 'THREE_SHIFT', 3, '["MORNING","EVENING","NIGHT"]', b'1', '三班倒：早班-晚班-夜班循环', NOW()),
('四班三运转', 'FOUR_SHIFT_THREE', 'FOUR_SHIFT', 4, '["MORNING","EVENING","NIGHT","REST"]', b'1', '四班三运转：早-晚-夜-休', NOW()),
('做五休二', 'FIVE_WORK_TWO_REST', 'CUSTOM', 7, '["MORNING","MORNING","MORNING","MORNING","MORNING","REST","REST"]', b'1', '周一至周五早班，周末休息', NOW());
COMMIT;

-- ----------------------------
-- Table structure for schedule_employee_shift
-- ----------------------------
DROP TABLE IF EXISTS `schedule_employee_shift`;
CREATE TABLE `schedule_employee_shift` (
  `shift_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '排班ID',
  `user_id` bigint(20) NOT NULL COMMENT '员工ID',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `shift_date` date NOT NULL COMMENT '排班日期',
  `shift_type_id` bigint(20) NOT NULL COMMENT '班次类型ID',
  `start_time` datetime DEFAULT NULL COMMENT '实际上班时间',
  `end_time` datetime DEFAULT NULL COMMENT '实际下班时间',
  `status` varchar(20) DEFAULT 'SCHEDULED' COMMENT '状态：SCHEDULED-已排班，CONFIRMED-已确认，WORKING-工作中，COMPLETED-已完成，ABSENT-缺勤，LEAVE-请假',
  `source` varchar(20) DEFAULT 'MANUAL' COMMENT '来源：MANUAL-手动，AUTO-自动生成，IMPORT-导入',
  `rule_id` bigint(20) DEFAULT NULL COMMENT '应用的轮班规则ID',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`shift_id`) USING BTREE,
  UNIQUE KEY `uk_user_date` (`user_id`,`shift_date`) USING BTREE,
  KEY `idx_dept_date` (`dept_id`,`shift_date`) USING BTREE,
  KEY `idx_shift_type` (`shift_type_id`) USING BTREE,
  KEY `idx_date` (`shift_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='员工排班表';

-- ----------------------------
-- Table structure for schedule_employee_rule
-- ----------------------------
DROP TABLE IF EXISTS `schedule_employee_rule`;
CREATE TABLE `schedule_employee_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '员工ID',
  `rule_id` bigint(20) NOT NULL COMMENT '轮班规则ID',
  `start_date` date NOT NULL COMMENT '规则生效开始日期',
  `end_date` date DEFAULT NULL COMMENT '规则生效结束日期',
  `cycle_start_index` int(11) DEFAULT 0 COMMENT '周期开始索引（用于轮班规则）',
  `is_enabled` bit(1) DEFAULT b'1' COMMENT '是否启用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_user_rule` (`user_id`,`rule_id`,`start_date`) USING BTREE,
  KEY `idx_user` (`user_id`) USING BTREE,
  KEY `idx_rule` (`rule_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='员工轮班规则关联表';

-- ----------------------------
-- Table structure for_schedule_conflict
-- ----------------------------
DROP TABLE IF EXISTS `schedule_conflict`;
CREATE TABLE `schedule_conflict` (
  `conflict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '冲突ID',
  `user_id` bigint(20) NOT NULL COMMENT '员工ID',
  `shift_date` date NOT NULL COMMENT '冲突日期',
  `conflict_type` varchar(50) NOT NULL COMMENT '冲突类型：TIME_OVERLAP-时间重叠，CONTINUOUS_WORK-连续工作超时，REST_INSUFFICIENT-休息不足',
  `conflict_desc` varchar(500) NOT NULL COMMENT '冲突描述',
  `related_shift_id` bigint(20) DEFAULT NULL COMMENT '关联排班ID',
  `status` varchar(20) DEFAULT 'UNRESOLVED' COMMENT '状态：UNRESOLVED-未解决，RESOLVED-已解决，IGNORED-已忽略',
  `resolved_by` varchar(255) DEFAULT NULL COMMENT '解决人',
  `resolved_time` datetime DEFAULT NULL COMMENT '解决时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`conflict_id`) USING BTREE,
  KEY `idx_user_date` (`user_id`,`shift_date`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='排班冲突记录表';

-- ----------------------------
-- Table structure for schedule_template
-- ----------------------------
DROP TABLE IF EXISTS `schedule_template`;
CREATE TABLE `schedule_template` (
  `template_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `name` varchar(100) NOT NULL COMMENT '模板名称',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '适用部门ID',
  `week_schedule` text NOT NULL COMMENT '周排班模板，JSON格式，如：{"MON":"MORNING","TUE":"MORNING",...}',
  `is_enabled` bit(1) DEFAULT b'1' COMMENT '是否启用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`template_id`) USING BTREE,
  KEY `idx_dept` (`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='排班模板表';

SET FOREIGN_KEY_CHECKS = 1;
