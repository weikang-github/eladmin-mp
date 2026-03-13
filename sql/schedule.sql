-- 排班管理模块数据库脚本

-- 班次表
CREATE TABLE IF NOT EXISTS `schedule_shift` (
    `shift_id` bigint NOT NULL AUTO_INCREMENT COMMENT '班次ID',
    `name` varchar(255) NOT NULL COMMENT '班次名称',
    `code` varchar(255) NOT NULL COMMENT '班次编码',
    `type` varchar(255) DEFAULT NULL COMMENT '班次类型',
    `start_time` time NOT NULL COMMENT '开始时间',
    `end_time` time NOT NULL COMMENT '结束时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `enabled` tinyint(1) DEFAULT '1' COMMENT '是否启用',
    `color` varchar(20) DEFAULT NULL COMMENT '颜色标识',
    `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
    `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`shift_id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='班次表';

-- 轮班规则表
CREATE TABLE IF NOT EXISTS `schedule_rotation_rule` (
    `rule_id` bigint NOT NULL AUTO_INCREMENT COMMENT '规则ID',
    `name` varchar(255) NOT NULL COMMENT '规则名称',
    `code` varchar(255) NOT NULL COMMENT '规则编码',
    `type` varchar(255) DEFAULT NULL COMMENT '规则类型',
    `cycle_days` int DEFAULT NULL COMMENT '周期天数',
    `pattern` text DEFAULT NULL COMMENT '轮班模式(逗号分隔的班次ID序列)',
    `enabled` tinyint(1) DEFAULT '1' COMMENT '是否启用',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
    `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`rule_id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='轮班规则表';

-- 排班分组表
CREATE TABLE IF NOT EXISTS `schedule_group` (
    `group_id` bigint NOT NULL AUTO_INCREMENT COMMENT '分组ID',
    `name` varchar(255) NOT NULL COMMENT '分组名称',
    `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
    `rotation_rule_id` bigint DEFAULT NULL COMMENT '轮班规则ID',
    `start_date` date DEFAULT NULL COMMENT '生效开始日期',
    `end_date` date DEFAULT NULL COMMENT '生效结束日期',
    `enabled` tinyint(1) DEFAULT '1' COMMENT '是否启用',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
    `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`group_id`),
    KEY `fk_dept_id` (`dept_id`),
    KEY `fk_rotation_rule_id` (`rotation_rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='排班分组表';

-- 排班分组成员表
CREATE TABLE IF NOT EXISTS `schedule_group_member` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `group_id` bigint NOT NULL COMMENT '分组ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_group_user` (`group_id`, `user_id`),
    KEY `fk_group_id` (`group_id`),
    KEY `fk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='排班分组成员表';

-- 排班表
CREATE TABLE IF NOT EXISTS `schedule` (
    `schedule_id` bigint NOT NULL AUTO_INCREMENT COMMENT '排班ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `shift_id` bigint NOT NULL COMMENT '班次ID',
    `schedule_date` date NOT NULL COMMENT '排班日期',
    `type` varchar(50) DEFAULT 'manual' COMMENT '排班类型(manual/auto)',
    `status` varchar(20) DEFAULT '0' COMMENT '状态(0:正常,1:取消)',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
    `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`schedule_id`),
    UNIQUE KEY `uk_user_date` (`user_id`, `schedule_date`),
    KEY `fk_user_id` (`user_id`),
    KEY `fk_shift_id` (`shift_id`),
    KEY `idx_schedule_date` (`schedule_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='排班表';

-- 排班冲突记录表
CREATE TABLE IF NOT EXISTS `schedule_conflict` (
    `conflict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '冲突ID',
    `schedule_id` bigint NOT NULL COMMENT '排班ID',
    `conflict_type` varchar(50) DEFAULT NULL COMMENT '冲突类型',
    `conflict_description` varchar(500) DEFAULT NULL COMMENT '冲突描述',
    `resolved` tinyint(1) DEFAULT '0' COMMENT '是否已解决',
    `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`conflict_id`),
    KEY `fk_schedule_id` (`schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='排班冲突记录表';

-- 初始化班次数据
INSERT INTO `schedule_shift` (`name`, `code`, `type`, `start_time`, `end_time`, `enabled`, `color`, `create_time`) VALUES
('早班', 'MORNING', 'WORK', '08:00:00', '16:00:00', 1, '#1890ff', NOW()),
('中班', 'AFTERNOON', 'WORK', '16:00:00', '24:00:00', 1, '#52c41a', NOW()),
('晚班', 'NIGHT', 'WORK', '00:00:00', '08:00:00', 1, '#722ed1', NOW()),
('弹性班', 'FLEXIBLE', 'FLEX', '09:00:00', '18:00:00', 1, '#faad14', NOW()),
('休息', 'REST', 'REST', '00:00:00', '23:59:59', 1, '#f5222d', NOW());

-- 初始化轮班规则数据
INSERT INTO `schedule_rotation_rule` (`name`, `code`, `type`, `cycle_days`, `pattern`, `enabled`, `create_time`) VALUES
('早班/晚班轮班', 'MORNING_NIGHT', 'ROTATION', 2, '1,3', 1, NOW()),
('三班倒', 'THREE_SHIFT', 'ROTATION', 3, '1,2,3', 1, NOW()),
('做五休二', 'FIVE_DAY_WORK', 'ROTATION', 7, '1,1,1,1,1,5,5', 1, NOW());
