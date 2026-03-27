/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MariaDB
 Source Server Version : 110206 (11.2.6-MariaDB)
 Source Host           : localhost:3306
 Source Schema         : eladmin-mp

 Target Server Type    : MariaDB
 Target Server Version : 110206 (11.2.6-MariaDB)
 File Encoding         : 65001

 Date: 27/03/2025
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for attendance_group
-- ----------------------------
DROP TABLE IF EXISTS `attendance_group`;
CREATE TABLE `attendance_group` (
  `group_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '考勤组ID',
  `group_name` varchar(255) NOT NULL COMMENT '考勤组名称',
  `company_latitude` decimal(10,6) DEFAULT NULL COMMENT '公司纬度',
  `company_longitude` decimal(10,6) DEFAULT NULL COMMENT '公司经度',
  `allowed_distance` int(11) DEFAULT 100 COMMENT '允许打卡距离(米)',
  `wifi_name` varchar(255) DEFAULT NULL COMMENT 'WiFi名称',
  `wifi_mac` varchar(255) DEFAULT NULL COMMENT 'WiFi MAC地址',
  `work_start_time` time DEFAULT NULL COMMENT '上班时间',
  `work_end_time` time DEFAULT NULL COMMENT '下班时间',
  `work_days` varchar(50) DEFAULT '1,2,3,4,5' COMMENT '工作日配置(1-7表示周一到周日)',
  `enabled` bit(1) DEFAULT b'1' COMMENT '是否启用',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`group_id`) USING BTREE,
  KEY `idx_enabled` (`enabled`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='考勤组';

-- ----------------------------
-- Records of attendance_group
-- ----------------------------
BEGIN;
INSERT INTO `attendance_group` (`group_id`, `group_name`, `company_latitude`, `company_longitude`, `allowed_distance`, `wifi_name`, `wifi_mac`, `work_start_time`, `work_end_time`, `work_days`, `enabled`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (1, '默认考勤组', 39.904200, 116.407400, 100, 'CompanyWiFi', '00:11:22:33:44:55', '09:00:00', '18:00:00', '1,2,3,4,5', b'1', 'admin', 'admin', NOW(), NOW());
COMMIT;

-- ----------------------------
-- Table structure for attendance_group_dept
-- ----------------------------
DROP TABLE IF EXISTS `attendance_group_dept`;
CREATE TABLE `attendance_group_dept` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `group_id` bigint(20) NOT NULL COMMENT '考勤组ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_group_dept` (`group_id`, `dept_id`),
  KEY `idx_group_id` (`group_id`),
  KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='考勤组部门关联';

-- ----------------------------
-- Records of attendance_group_dept
-- ----------------------------
BEGIN;
INSERT INTO `attendance_group_dept` (`group_id`, `dept_id`) VALUES (1, 2);
INSERT INTO `attendance_group_dept` (`group_id`, `dept_id`) VALUES (1, 5);
INSERT INTO `attendance_group_dept` (`group_id`, `dept_id`) VALUES (1, 6);
COMMIT;

-- ----------------------------
-- Table structure for attendance_holiday
-- ----------------------------
DROP TABLE IF EXISTS `attendance_holiday`;
CREATE TABLE `attendance_holiday` (
  `holiday_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '节假日ID',
  `holiday_date` date NOT NULL COMMENT '日期',
  `holiday_name` varchar(255) NOT NULL COMMENT '节假日名称',
  `holiday_type` varchar(20) NOT NULL COMMENT '节假日类型(HOLIDAY-节假日,WORKDAY-调休工作日)',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`holiday_id`) USING BTREE,
  UNIQUE KEY `idx_holiday_date` (`holiday_date`),
  KEY `idx_holiday_type` (`holiday_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='考勤节假日';

-- ----------------------------
-- Records of attendance_holiday
-- ----------------------------
BEGIN;
INSERT INTO `attendance_holiday` (`holiday_date`, `holiday_name`, `holiday_type`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES ('2025-01-01', '元旦', 'HOLIDAY', 'admin', 'admin', NOW(), NOW());
INSERT INTO `attendance_holiday` (`holiday_date`, `holiday_name`, `holiday_type`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES ('2025-01-26', '春节调休', 'WORKDAY', 'admin', 'admin', NOW(), NOW());
COMMIT;

-- ----------------------------
-- Table structure for attendance_record
-- ----------------------------
DROP TABLE IF EXISTS `attendance_record`;
CREATE TABLE `attendance_record` (
  `record_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '打卡记录ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `group_id` bigint(20) DEFAULT NULL COMMENT '考勤组ID',
  `check_in_time` datetime DEFAULT NULL COMMENT '打卡时间',
  `check_in_type` varchar(20) NOT NULL COMMENT '打卡类型(IN-上班,OUT-下班)',
  `check_in_method` varchar(20) NOT NULL COMMENT '打卡方式(GPS-定位,WiFi-WiFi)',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '打卡纬度',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '打卡经度',
  `wifi_name` varchar(255) DEFAULT NULL COMMENT 'WiFi名称',
  `wifi_mac` varchar(255) DEFAULT NULL COMMENT 'WiFi MAC地址',
  `status` varchar(20) DEFAULT 'NORMAL' COMMENT '考勤状态(NORMAL-正常,LATE-迟到,EARLY_LEAVE-早退,ABSENT-旷工,MISSING-缺卡)',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`record_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`),
  KEY `idx_group_id` (`group_id`),
  KEY `idx_check_in_time` (`check_in_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='考勤打卡记录';

-- ----------------------------
-- Records of attendance_record
-- ----------------------------
BEGIN;
INSERT INTO `attendance_record` (`user_id`, `group_id`, `check_in_time`, `check_in_type`, `check_in_method`, `latitude`, `longitude`, `wifi_name`, `wifi_mac`, `status`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (1, 1, '2025-03-27 08:55:00', 'IN', 'GPS', 39.904200, 116.407400, NULL, NULL, 'NORMAL', NULL, 'admin', 'admin', NOW(), NOW());
INSERT INTO `attendance_record` (`user_id`, `group_id`, `check_in_time`, `check_in_type`, `check_in_method`, `latitude`, `longitude`, `wifi_name`, `wifi_mac`, `status`, `remark`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (1, 1, '2025-03-27 18:05:00', 'OUT', 'WiFi', NULL, NULL, 'CompanyWiFi', '00:11:22:33:44:55', 'NORMAL', NULL, 'admin', 'admin', NOW(), NOW());
COMMIT;

-- ----------------------------
-- Table structure for attendance_statistics
-- ----------------------------
DROP TABLE IF EXISTS `attendance_statistics`;
CREATE TABLE `attendance_statistics` (
  `statistics_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `statistics_date` date NOT NULL COMMENT '统计日期',
  `group_id` bigint(20) DEFAULT NULL COMMENT '考勤组ID',
  `work_days` int(11) DEFAULT 0 COMMENT '应出勤天数',
  `actual_days` int(11) DEFAULT 0 COMMENT '实际出勤天数',
  `late_count` int(11) DEFAULT 0 COMMENT '迟到次数',
  `early_leave_count` int(11) DEFAULT 0 COMMENT '早退次数',
  `absent_count` int(11) DEFAULT 0 COMMENT '旷工次数',
  `missing_count` int(11) DEFAULT 0 COMMENT '缺卡次数',
  `overtime_hours` decimal(10,2) DEFAULT 0.00 COMMENT '加班时长(小时)',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`statistics_id`) USING BTREE,
  UNIQUE KEY `idx_user_date` (`user_id`, `statistics_date`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_statistics_date` (`statistics_date`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='考勤统计';

-- ----------------------------
-- Records of attendance_statistics
-- ----------------------------
BEGIN;
INSERT INTO `attendance_statistics` (`user_id`, `statistics_date`, `group_id`, `work_days`, `actual_days`, `late_count`, `early_leave_count`, `absent_count`, `missing_count`, `overtime_hours`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (1, '2025-03-27', 1, 1, 1, 0, 0, 0, 0, 0.00, 'admin', 'admin', NOW(), NOW());
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
