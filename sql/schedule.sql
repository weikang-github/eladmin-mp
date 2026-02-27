-- ----------------------------
-- 班次表
-- ----------------------------
DROP TABLE IF EXISTS `shift`;
CREATE TABLE `shift` (
  `shift_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '班次ID',
  `shift_name` varchar(50) NOT NULL COMMENT '班次名称',
  `shift_code` varchar(20) NOT NULL COMMENT '班次编码',
  `shift_type` varchar(20) NOT NULL COMMENT '班次类型：早班/晚班/夜班/弹性班',
  `start_time` time NOT NULL COMMENT '开始时间',
  `end_time` time NOT NULL COMMENT '结束时间',
  `break_start_time` time DEFAULT NULL COMMENT '休息开始时间',
  `break_end_time` time DEFAULT NULL COMMENT '休息结束时间',
  `work_hours` decimal(4,2) DEFAULT NULL COMMENT '工作时长(小时)',
  `color` varchar(20) DEFAULT NULL COMMENT '显示颜色',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `enabled` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否启用',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`shift_id`),
  UNIQUE KEY `uniq_shift_code` (`shift_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='班次表';

-- ----------------------------
-- 排班规则表
-- ----------------------------
DROP TABLE IF EXISTS `schedule_rule`;
CREATE TABLE `schedule_rule` (
  `rule_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `rule_name` varchar(50) NOT NULL COMMENT '规则名称',
  `rule_code` varchar(20) NOT NULL COMMENT '规则编码',
  `rule_type` varchar(20) NOT NULL COMMENT '规则类型：A/B轮班/三班倒/四班三倒/自定义',
  `cycle_days` int(11) NOT NULL COMMENT '轮班周期(天)',
  `shift_ids` varchar(500) NOT NULL COMMENT '班次ID列表(逗号分隔)',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `enabled` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否启用',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`rule_id`),
  UNIQUE KEY `uniq_rule_code` (`rule_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='排班规则表';

-- ----------------------------
-- 排班记录表
-- ----------------------------
DROP TABLE IF EXISTS `schedule`;
CREATE TABLE `schedule` (
  `schedule_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '排班ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `shift_id` bigint(20) NOT NULL COMMENT '班次ID',
  `schedule_date` date NOT NULL COMMENT '排班日期',
  `schedule_type` varchar(20) NOT NULL COMMENT '排班类型：手动/自动',
  `rule_id` bigint(20) DEFAULT NULL COMMENT '规则ID',
  `status` varchar(20) NOT NULL DEFAULT 'normal' COMMENT '状态：normal/leave/overtime',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`schedule_id`),
  UNIQUE KEY `uniq_user_date` (`user_id`, `schedule_date`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_shift_id` (`shift_id`),
  KEY `idx_schedule_date` (`schedule_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='排班记录表';

-- ----------------------------
-- 初始化班次数据
-- ----------------------------
INSERT INTO `shift` (`shift_name`, `shift_code`, `shift_type`, `start_time`, `end_time`, `break_start_time`, `break_end_time`, `work_hours`, `color`, `description`, `enabled`, `create_by`, `create_time`) VALUES
('早班', 'SHIFT_MORNING', '早班', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 8.00, '#52c41a', '上午班次', b'1', 'admin', NOW()),
('晚班', 'SHIFT_EVENING', '晚班', '16:00:00', '00:00:00', '19:00:00', '20:00:00', 7.00, '#1890ff', '下午班次', b'1', 'admin', NOW()),
('夜班', 'SHIFT_NIGHT', '夜班', '00:00:00', '08:00:00', '04:00:00', '05:00:00', 7.00, '#722ed1', '夜间班次', b'1', 'admin', NOW()),
('弹性班', 'SHIFT_FLEXIBLE', '弹性班', '09:00:00', '18:00:00', NULL, NULL, 8.00, '#fa8c16', '弹性工作班次', b'1', 'admin', NOW());

-- ----------------------------
-- 初始化排班规则数据
-- ----------------------------
INSERT INTO `schedule_rule` (`rule_name`, `rule_code`, `rule_type`, `cycle_days`, `shift_ids`, `description`, `enabled`, `create_by`, `create_time`) VALUES
('A/B轮班', 'RULE_AB', 'A/B轮班', 2, '1,2', '早班和晚班轮换', b'1', 'admin', NOW()),
('三班倒', 'RULE_THREE', '三班倒', 3, '1,2,3', '早班、晚班、夜班轮换', b'1', 'admin', NOW()),
('四班三倒', 'RULE_FOUR', '四班三倒', 4, '1,2,3,1', '早班、晚班、夜班、早班轮换', b'1', 'admin', NOW());

-- ----------------------------
-- 排班管理菜单
-- ----------------------------
INSERT INTO `sys_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `create_time`) VALUES
(100, NULL, 4, 0, '排班管理', NULL, NULL, 5, 'schedule', 'schedule', b'0', b'0', b'0', NULL, 'admin', NOW());

-- 班次管理
INSERT INTO `sys_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `create_time`) VALUES
(101, 100, 0, 1, '班次管理', 'Shift', 'schedule/shift/index', 1, 'clock', 'shift', b'0', b'0', b'0', 'shift:list', 'admin', NOW());

INSERT INTO `sys_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `create_time`) VALUES
(102, 101, 0, 2, '新增班次', NULL, NULL, 1, NULL, NULL, b'0', b'0', b'0', 'shift:add', 'admin', NOW()),
(103, 101, 0, 2, '修改班次', NULL, NULL, 2, NULL, NULL, b'0', b'0', b'0', 'shift:edit', 'admin', NOW()),
(104, 101, 0, 2, '删除班次', NULL, NULL, 3, NULL, NULL, b'0', b'0', b'0', 'shift:del', 'admin', NOW());

-- 排班规则
INSERT INTO `sys_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `create_time`) VALUES
(105, 100, 0, 1, '排班规则', 'Rule', 'schedule/rule/index', 2, 'setting', 'rule', b'0', b'0', b'0', 'rule:list', 'admin', NOW());

INSERT INTO `sys_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `create_time`) VALUES
(106, 105, 0, 2, '新增规则', NULL, NULL, 1, NULL, NULL, b'0', b'0', b'0', 'rule:add', 'admin', NOW()),
(107, 105, 0, 2, '修改规则', NULL, NULL, 2, NULL, NULL, b'0', b'0', b'0', 'rule:edit', 'admin', NOW()),
(108, 105, 0, 2, '删除规则', NULL, NULL, 3, NULL, NULL, b'0', b'0', b'0', 'rule:del', 'admin', NOW());

-- 排班记录
INSERT INTO `sys_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `create_time`) VALUES
(109, 100, 0, 1, '排班记录', 'Schedule', 'schedule/record/index', 3, 'calendar', 'schedule', b'0', b'0', b'0', 'schedule:list', 'admin', NOW());

INSERT INTO `sys_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `create_time`) VALUES
(110, 109, 0, 2, '新增排班', NULL, NULL, 1, NULL, NULL, b'0', b'0', b'0', 'schedule:add', 'admin', NOW()),
(111, 109, 0, 2, '修改排班', NULL, NULL, 2, NULL, NULL, b'0', b'0', b'0', 'schedule:edit', 'admin', NOW()),
(112, 109, 0, 2, '删除排班', NULL, NULL, 3, NULL, NULL, b'0', b'0', b'0', 'schedule:del', 'admin', NOW());

-- 排班日历
INSERT INTO `sys_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `create_time`) VALUES
(113, 100, 0, 1, '排班日历', 'Calendar', 'schedule/calendar/index', 4, 'calendar', 'calendar', b'0', b'0', b'0', 'schedule:list', 'admin', NOW());
