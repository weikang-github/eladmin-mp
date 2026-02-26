/*
 Navicat Premium Dump SQL

 Source Server         : 192.168.8.63-pgsql
 Source Server Type    : PostgreSQL
 Source Server Version : 140017 (140017)
 Source Host           : 192.168.8.63:5432
 Source Catalog        : forest
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 140017 (140017)
 File Encoding         : 65001

 Date: 04/12/2025 16:14:54
*/


-- ----------------------------
-- Table structure for code_column
-- ----------------------------
DROP TABLE IF EXISTS "public"."code_column";
CREATE TABLE "public"."code_column" (
  "column_id" int8 NOT NULL,
  "table_name" varchar(180) COLLATE "pg_catalog"."default",
  "column_name" varchar(255) COLLATE "pg_catalog"."default",
  "column_type" varchar(255) COLLATE "pg_catalog"."default",
  "dict_name" varchar(255) COLLATE "pg_catalog"."default",
  "extra" varchar(255) COLLATE "pg_catalog"."default",
  "form_show" varchar(1) COLLATE "pg_catalog"."default",
  "form_type" varchar(255) COLLATE "pg_catalog"."default",
  "key_type" varchar(255) COLLATE "pg_catalog"."default",
  "list_show" varchar(1) COLLATE "pg_catalog"."default",
  "not_null" varchar(1) COLLATE "pg_catalog"."default",
  "query_type" varchar(255) COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."code_column"."column_id" IS 'ID';
COMMENT ON COLUMN "public"."code_column"."table_name" IS '表名';
COMMENT ON COLUMN "public"."code_column"."column_name" IS '数据库字段名称';
COMMENT ON COLUMN "public"."code_column"."column_type" IS '数据库字段类型';
COMMENT ON COLUMN "public"."code_column"."dict_name" IS '字典名称';
COMMENT ON COLUMN "public"."code_column"."extra" IS '字段额外的参数';
COMMENT ON COLUMN "public"."code_column"."form_show" IS '是否表单显示';
COMMENT ON COLUMN "public"."code_column"."form_type" IS '表单类型';
COMMENT ON COLUMN "public"."code_column"."key_type" IS '数据库字段键类型';
COMMENT ON COLUMN "public"."code_column"."list_show" IS '是否在列表显示';
COMMENT ON COLUMN "public"."code_column"."not_null" IS '是否为空';
COMMENT ON COLUMN "public"."code_column"."query_type" IS '查询类型';
COMMENT ON COLUMN "public"."code_column"."remark" IS '描述';
COMMENT ON TABLE "public"."code_column" IS '代码生成字段信息存储';

-- ----------------------------
-- Records of code_column
-- ----------------------------

-- ----------------------------
-- Table structure for code_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."code_config";
CREATE TABLE "public"."code_config" (
  "config_id" int8 NOT NULL,
  "table_name" varchar(255) COLLATE "pg_catalog"."default",
  "author" varchar(255) COLLATE "pg_catalog"."default",
  "cover" varchar(1) COLLATE "pg_catalog"."default",
  "module_name" varchar(255) COLLATE "pg_catalog"."default",
  "pack" varchar(255) COLLATE "pg_catalog"."default",
  "path" varchar(255) COLLATE "pg_catalog"."default",
  "api_path" varchar(255) COLLATE "pg_catalog"."default",
  "prefix" varchar(255) COLLATE "pg_catalog"."default",
  "api_alias" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."code_config"."config_id" IS 'ID';
COMMENT ON COLUMN "public"."code_config"."table_name" IS '表名';
COMMENT ON COLUMN "public"."code_config"."author" IS '作者';
COMMENT ON COLUMN "public"."code_config"."cover" IS '是否覆盖';
COMMENT ON COLUMN "public"."code_config"."module_name" IS '模块名称';
COMMENT ON COLUMN "public"."code_config"."pack" IS '至于哪个包下';
COMMENT ON COLUMN "public"."code_config"."path" IS '前端代码生成的路径';
COMMENT ON COLUMN "public"."code_config"."api_path" IS '前端Api文件路径';
COMMENT ON COLUMN "public"."code_config"."prefix" IS '表前缀';
COMMENT ON COLUMN "public"."code_config"."api_alias" IS '接口名称';
COMMENT ON TABLE "public"."code_config" IS '代码生成器配置';

-- ----------------------------
-- Records of code_config
-- ----------------------------

-- ----------------------------
-- Table structure for mnt_app
-- ----------------------------
DROP TABLE IF EXISTS "public"."mnt_app";
CREATE TABLE "public"."mnt_app" (
  "app_id" int8 NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "upload_path" varchar(255) COLLATE "pg_catalog"."default",
  "deploy_path" varchar(255) COLLATE "pg_catalog"."default",
  "backup_path" varchar(255) COLLATE "pg_catalog"."default",
  "port" int4,
  "start_script" varchar(4000) COLLATE "pg_catalog"."default",
  "deploy_script" varchar(4000) COLLATE "pg_catalog"."default",
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."mnt_app"."app_id" IS 'ID';
COMMENT ON COLUMN "public"."mnt_app"."name" IS '应用名称';
COMMENT ON COLUMN "public"."mnt_app"."upload_path" IS '上传目录';
COMMENT ON COLUMN "public"."mnt_app"."deploy_path" IS '部署路径';
COMMENT ON COLUMN "public"."mnt_app"."backup_path" IS '备份路径';
COMMENT ON COLUMN "public"."mnt_app"."port" IS '应用端口';
COMMENT ON COLUMN "public"."mnt_app"."start_script" IS '启动脚本';
COMMENT ON COLUMN "public"."mnt_app"."deploy_script" IS '部署脚本';
COMMENT ON COLUMN "public"."mnt_app"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."mnt_app"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."mnt_app"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."mnt_app"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."mnt_app" IS '应用管理';

-- ----------------------------
-- Records of mnt_app
-- ----------------------------

-- ----------------------------
-- Table structure for mnt_database
-- ----------------------------
DROP TABLE IF EXISTS "public"."mnt_database";
CREATE TABLE "public"."mnt_database" (
  "db_id" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "jdbc_url" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "user_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "pwd" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."mnt_database"."db_id" IS 'ID';
COMMENT ON COLUMN "public"."mnt_database"."name" IS '名称';
COMMENT ON COLUMN "public"."mnt_database"."jdbc_url" IS 'jdbc连接';
COMMENT ON COLUMN "public"."mnt_database"."user_name" IS '账号';
COMMENT ON COLUMN "public"."mnt_database"."pwd" IS '密码';
COMMENT ON COLUMN "public"."mnt_database"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."mnt_database"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."mnt_database"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mnt_database"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."mnt_database" IS '数据库管理';

-- ----------------------------
-- Records of mnt_database
-- ----------------------------

-- ----------------------------
-- Table structure for mnt_deploy
-- ----------------------------
DROP TABLE IF EXISTS "public"."mnt_deploy";
CREATE TABLE "public"."mnt_deploy" (
  "deploy_id" int8 NOT NULL,
  "app_id" int8,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."mnt_deploy"."deploy_id" IS 'ID';
COMMENT ON COLUMN "public"."mnt_deploy"."app_id" IS '应用编号';
COMMENT ON COLUMN "public"."mnt_deploy"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."mnt_deploy"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."mnt_deploy"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."mnt_deploy" IS '部署管理';

-- ----------------------------
-- Records of mnt_deploy
-- ----------------------------

-- ----------------------------
-- Table structure for mnt_deploy_history
-- ----------------------------
DROP TABLE IF EXISTS "public"."mnt_deploy_history";
CREATE TABLE "public"."mnt_deploy_history" (
  "history_id" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "app_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "deploy_date" timestamp(6) NOT NULL,
  "deploy_user" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "ip" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "deploy_id" int8
)
;
COMMENT ON COLUMN "public"."mnt_deploy_history"."history_id" IS 'ID';
COMMENT ON COLUMN "public"."mnt_deploy_history"."app_name" IS '应用名称';
COMMENT ON COLUMN "public"."mnt_deploy_history"."deploy_date" IS '部署日期';
COMMENT ON COLUMN "public"."mnt_deploy_history"."deploy_user" IS '部署用户';
COMMENT ON COLUMN "public"."mnt_deploy_history"."ip" IS '服务器IP';
COMMENT ON COLUMN "public"."mnt_deploy_history"."deploy_id" IS '部署编号';
COMMENT ON TABLE "public"."mnt_deploy_history" IS '部署历史管理';

-- ----------------------------
-- Records of mnt_deploy_history
-- ----------------------------

-- ----------------------------
-- Table structure for mnt_deploy_server
-- ----------------------------
DROP TABLE IF EXISTS "public"."mnt_deploy_server";
CREATE TABLE "public"."mnt_deploy_server" (
  "deploy_id" int8 NOT NULL,
  "server_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."mnt_deploy_server"."deploy_id" IS '部署ID';
COMMENT ON COLUMN "public"."mnt_deploy_server"."server_id" IS '服务ID';
COMMENT ON TABLE "public"."mnt_deploy_server" IS '应用与服务器关联';

-- ----------------------------
-- Records of mnt_deploy_server
-- ----------------------------

-- ----------------------------
-- Table structure for mnt_server
-- ----------------------------
DROP TABLE IF EXISTS "public"."mnt_server";
CREATE TABLE "public"."mnt_server" (
  "server_id" int8 NOT NULL,
  "account" varchar(50) COLLATE "pg_catalog"."default",
  "ip" varchar(20) COLLATE "pg_catalog"."default",
  "name" varchar(100) COLLATE "pg_catalog"."default",
  "password" varchar(100) COLLATE "pg_catalog"."default",
  "port" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."mnt_server"."server_id" IS 'ID';
COMMENT ON COLUMN "public"."mnt_server"."account" IS '账号';
COMMENT ON COLUMN "public"."mnt_server"."ip" IS 'IP地址';
COMMENT ON COLUMN "public"."mnt_server"."name" IS '名称';
COMMENT ON COLUMN "public"."mnt_server"."password" IS '密码';
COMMENT ON COLUMN "public"."mnt_server"."port" IS '端口';
COMMENT ON COLUMN "public"."mnt_server"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."mnt_server"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."mnt_server"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mnt_server"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."mnt_server" IS '服务器管理';

-- ----------------------------
-- Records of mnt_server
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_blob_triggers";
CREATE TABLE "public"."qrtz_blob_triggers" (
  "sched_name" varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_name" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_group" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "blob_data" bytea
)
;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_calendars";
CREATE TABLE "public"."qrtz_calendars" (
  "sched_name" varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
  "calendar_name" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "calendar" bytea NOT NULL
)
;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_cron_triggers";
CREATE TABLE "public"."qrtz_cron_triggers" (
  "sched_name" varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_name" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_group" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "cron_expression" varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
  "time_zone_id" varchar(80) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_fired_triggers";
CREATE TABLE "public"."qrtz_fired_triggers" (
  "sched_name" varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
  "entry_id" varchar(95) COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_name" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_group" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "instance_name" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "fired_time" int8 NOT NULL,
  "sched_time" int8 NOT NULL,
  "priority" int4 NOT NULL,
  "state" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "job_name" varchar(200) COLLATE "pg_catalog"."default",
  "job_group" varchar(200) COLLATE "pg_catalog"."default",
  "is_nonconcurrent" varchar(1) COLLATE "pg_catalog"."default",
  "requests_recovery" varchar(1) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_job_details";
CREATE TABLE "public"."qrtz_job_details" (
  "sched_name" varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
  "job_name" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "job_group" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "description" varchar(250) COLLATE "pg_catalog"."default",
  "job_class_name" varchar(250) COLLATE "pg_catalog"."default" NOT NULL,
  "is_durable" varchar(1) COLLATE "pg_catalog"."default" NOT NULL,
  "is_nonconcurrent" varchar(1) COLLATE "pg_catalog"."default" NOT NULL,
  "is_update_data" varchar(1) COLLATE "pg_catalog"."default" NOT NULL,
  "requests_recovery" varchar(1) COLLATE "pg_catalog"."default" NOT NULL,
  "job_data" bytea
)
;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_locks";
CREATE TABLE "public"."qrtz_locks" (
  "sched_name" varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
  "lock_name" varchar(40) COLLATE "pg_catalog"."default" NOT NULL
)
;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_paused_trigger_grps";
CREATE TABLE "public"."qrtz_paused_trigger_grps" (
  "sched_name" varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_group" varchar(200) COLLATE "pg_catalog"."default" NOT NULL
)
;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_scheduler_state";
CREATE TABLE "public"."qrtz_scheduler_state" (
  "sched_name" varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
  "instance_name" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "last_checkin_time" int8 NOT NULL,
  "checkin_interval" int8 NOT NULL
)
;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_simple_triggers";
CREATE TABLE "public"."qrtz_simple_triggers" (
  "sched_name" varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_name" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_group" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "repeat_count" int8 NOT NULL,
  "repeat_interval" int8 NOT NULL,
  "times_triggered" int8 NOT NULL
)
;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_simprop_triggers";
CREATE TABLE "public"."qrtz_simprop_triggers" (
  "sched_name" varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_name" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_group" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "str_prop_1" varchar(512) COLLATE "pg_catalog"."default",
  "str_prop_2" varchar(512) COLLATE "pg_catalog"."default",
  "str_prop_3" varchar(512) COLLATE "pg_catalog"."default",
  "int_prop_1" int4,
  "int_prop_2" int4,
  "long_prop_1" int8,
  "long_prop_2" int8,
  "dec_prop_1" numeric(13,4),
  "dec_prop_2" numeric(13,4),
  "bool_prop_1" varchar(1) COLLATE "pg_catalog"."default",
  "bool_prop_2" varchar(1) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_triggers";
CREATE TABLE "public"."qrtz_triggers" (
  "sched_name" varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_name" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_group" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "job_name" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "job_group" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "description" varchar(250) COLLATE "pg_catalog"."default",
  "next_fire_time" int8,
  "prev_fire_time" int8,
  "priority" int4,
  "trigger_state" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_type" varchar(8) COLLATE "pg_catalog"."default" NOT NULL,
  "start_time" int8 NOT NULL,
  "end_time" int8,
  "calendar_name" varchar(200) COLLATE "pg_catalog"."default",
  "misfire_instr" int2,
  "job_data" bytea
)
;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dept";
CREATE TABLE "public"."sys_dept" (
  "dept_id" int8 NOT NULL,
  "pid" int8,
  "sub_count" int4,
  "name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "dept_sort" int4,
  "enabled" varchar(1) COLLATE "pg_catalog"."default" NOT NULL,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_dept"."dept_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_dept"."pid" IS '上级部门';
COMMENT ON COLUMN "public"."sys_dept"."sub_count" IS '子部门数目';
COMMENT ON COLUMN "public"."sys_dept"."name" IS '名称';
COMMENT ON COLUMN "public"."sys_dept"."dept_sort" IS '排序';
COMMENT ON COLUMN "public"."sys_dept"."enabled" IS '状态';
COMMENT ON COLUMN "public"."sys_dept"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_dept"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_dept"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_dept"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_dept" IS '部门';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO "public"."sys_dept" VALUES (2, 7, 1, '研发部', 3, '1', 'admin', 'admin', '2019-03-25 09:15:32', '2020-08-02 14:48:47');
INSERT INTO "public"."sys_dept" VALUES (5, 7, 0, '运维部', 4, '1', 'admin', 'admin', '2019-03-25 09:20:44', '2020-05-17 14:27:27');
INSERT INTO "public"."sys_dept" VALUES (6, 8, 0, '测试部', 6, '1', 'admin', 'admin', '2019-03-25 09:52:18', '2020-06-08 11:59:21');
INSERT INTO "public"."sys_dept" VALUES (7, NULL, 2, '华南分部', 0, '1', 'admin', 'admin', '2019-03-25 11:04:50', '2020-06-08 12:08:56');
INSERT INTO "public"."sys_dept" VALUES (8, NULL, 2, '华北分部', 1, '1', 'admin', 'admin', '2019-03-25 11:04:53', '2020-05-14 12:54:00');
INSERT INTO "public"."sys_dept" VALUES (15, 8, 0, 'UI部门', 7, '1', 'admin', 'admin', '2020-05-13 22:56:53', '2020-05-14 12:54:13');
INSERT INTO "public"."sys_dept" VALUES (17, 2, 0, '研发一组', 999, '1', 'admin', 'admin', '2020-08-02 14:49:07', '2020-08-02 14:49:07');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dict";
CREATE TABLE "public"."sys_dict" (
  "dict_id" int8 NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "description" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_dict"."dict_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_dict"."name" IS '字典名称';
COMMENT ON COLUMN "public"."sys_dict"."description" IS '描述';
COMMENT ON COLUMN "public"."sys_dict"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_dict"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_dict"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_dict"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_dict" IS '数据字典';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO "public"."sys_dict" VALUES (1, 'user_status', '用户状态', NULL, NULL, '2019-10-27 20:31:36', NULL);
INSERT INTO "public"."sys_dict" VALUES (4, 'dept_status', '部门状态', NULL, NULL, '2019-10-27 20:31:36', NULL);
INSERT INTO "public"."sys_dict" VALUES (5, 'job_status', '岗位状态', NULL, 'admin', '2019-10-27 20:31:36', '2025-01-14 15:48:29');

-- ----------------------------
-- Table structure for sys_dict_detail
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dict_detail";
CREATE TABLE "public"."sys_dict_detail" (
  "detail_id" int8 NOT NULL,
  "dict_id" int8,
  "label" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "value" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "dict_sort" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_dict_detail"."detail_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_dict_detail"."dict_id" IS '字典id';
COMMENT ON COLUMN "public"."sys_dict_detail"."label" IS '字典标签';
COMMENT ON COLUMN "public"."sys_dict_detail"."value" IS '字典值';
COMMENT ON COLUMN "public"."sys_dict_detail"."dict_sort" IS '排序';
COMMENT ON COLUMN "public"."sys_dict_detail"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_dict_detail"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_dict_detail"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_dict_detail"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_dict_detail" IS '数据字典详情';

-- ----------------------------
-- Records of sys_dict_detail
-- ----------------------------
INSERT INTO "public"."sys_dict_detail" VALUES (1, 1, '激活', 'true', 1, NULL, NULL, '2019-10-27 20:31:36', NULL);
INSERT INTO "public"."sys_dict_detail" VALUES (2, 1, '禁用', 'false', 2, NULL, NULL, NULL, NULL);
INSERT INTO "public"."sys_dict_detail" VALUES (3, 4, '启用', 'true', 1, NULL, NULL, NULL, NULL);
INSERT INTO "public"."sys_dict_detail" VALUES (4, 4, '停用', 'false', 2, NULL, NULL, '2019-10-27 20:31:36', NULL);
INSERT INTO "public"."sys_dict_detail" VALUES (5, 5, '启用', 'true', 1, NULL, NULL, NULL, NULL);
INSERT INTO "public"."sys_dict_detail" VALUES (6, 5, '停用', 'false', 2, NULL, NULL, '2019-10-27 20:31:36', NULL);

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_job";
CREATE TABLE "public"."sys_job" (
  "job_id" int8 NOT NULL,
  "name" varchar(180) COLLATE "pg_catalog"."default" NOT NULL,
  "enabled" varchar(1) COLLATE "pg_catalog"."default" NOT NULL,
  "job_sort" int4,
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_job"."job_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_job"."name" IS '岗位名称';
COMMENT ON COLUMN "public"."sys_job"."enabled" IS '岗位状态';
COMMENT ON COLUMN "public"."sys_job"."job_sort" IS '排序';
COMMENT ON COLUMN "public"."sys_job"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_job"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_job"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_job"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_job" IS '岗位';

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO "public"."sys_job" VALUES (8, '人事专员', '1', 3, NULL, NULL, '2019-03-29 14:52:28', NULL);
INSERT INTO "public"."sys_job" VALUES (10, '产品经理', '1', 4, NULL, NULL, '2019-03-29 14:55:51', NULL);
INSERT INTO "public"."sys_job" VALUES (11, '全栈开发', '1', 2, NULL, 'admin', '2019-03-31 13:39:30', '2020-05-05 11:33:43');
INSERT INTO "public"."sys_job" VALUES (12, '软件测试', '1', 5, NULL, 'admin', '2019-03-31 13:39:43', '2020-05-10 19:56:26');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_log";
CREATE TABLE "public"."sys_log" (
  "log_id" int8 NOT NULL,
  "description" varchar(255) COLLATE "pg_catalog"."default",
  "log_type" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "method" varchar(255) COLLATE "pg_catalog"."default",
  "params" text COLLATE "pg_catalog"."default",
  "request_ip" varchar(255) COLLATE "pg_catalog"."default",
  "time" int8,
  "username" varchar(255) COLLATE "pg_catalog"."default",
  "address" varchar(255) COLLATE "pg_catalog"."default",
  "browser" varchar(255) COLLATE "pg_catalog"."default",
  "exception_detail" text COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_log"."log_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_log"."description" IS '描述';
COMMENT ON COLUMN "public"."sys_log"."log_type" IS '日志类型：INFI/ERROR';
COMMENT ON COLUMN "public"."sys_log"."method" IS '方法名';
COMMENT ON COLUMN "public"."sys_log"."params" IS '参数';
COMMENT ON COLUMN "public"."sys_log"."request_ip" IS '请求IP';
COMMENT ON COLUMN "public"."sys_log"."time" IS '执行时间';
COMMENT ON COLUMN "public"."sys_log"."username" IS '用户名';
COMMENT ON COLUMN "public"."sys_log"."address" IS '地址';
COMMENT ON COLUMN "public"."sys_log"."browser" IS '浏览器';
COMMENT ON COLUMN "public"."sys_log"."exception_detail" IS '异常';
COMMENT ON COLUMN "public"."sys_log"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."sys_log" IS '系统日志';

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO "public"."sys_log" VALUES (13753, '删除多个文件', 'INFO', 'me.zhengjie.rest.S3StorageController.deleteAllQiNiu()', '{"reqBodyList":[2]}', '127.0.0.1', 225, 'admin', '内网IP', 'Chrome 137', NULL, '2025-06-19 16:55:15');
INSERT INTO "public"."sys_log" VALUES (13754, '用户登录', 'ERROR', 'me.zhengjie.modules.security.rest.AuthController.login()', '{"code":"11","password":"******","username":"admin","uuid":"1232323"}', '0:0:0:0:0:0:0:1', 42, 'admin', 'IANA保留地址', 'Chrome 142', 'javax.crypto.BadPaddingException: Padding error in decryption
	at com.sun.crypto.provider.RSACipher.doFinal(RSACipher.java:385)
	at com.sun.crypto.provider.RSACipher.engineDoFinal(RSACipher.java:420)
	at javax.crypto.Cipher.doFinal(Cipher.java:2170)
	at me.zhengjie.utils.RsaUtils.doLongerCipherFinal(RsaUtils.java:144)
	at me.zhengjie.utils.RsaUtils.decryptByPrivateKey(RsaUtils.java:120)
	at me.zhengjie.modules.security.rest.AuthController.login(AuthController.java:83)
	at me.zhengjie.modules.security.rest.AuthController$$FastClassBySpringCGLIB$$1.invoke(<generated>)
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:792)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:762)
	at org.springframework.aop.aspectj.AspectJAfterThrowingAdvice.invoke(AspectJAfterThrowingAdvice.java:64)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:175)
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:762)
	at org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint.proceed(MethodInvocationProceedingJoinPoint.java:89)
	at me.zhengjie.aspect.LogAspect.logAround(LogAspect.java:68)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethodWithGivenArgs(AbstractAspectJAdvice.java:634)
	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethod(AbstractAspectJAdvice.java:624)
	at org.springframework.aop.aspectj.AspectJAroundAdvice.invoke(AspectJAroundAdvice.java:72)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:175)
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:762)
	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:97)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:762)
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:707)
	at me.zhengjie.modules.security.rest.AuthController$$EnhancerBySpringCGLIB$$1.login(<generated>)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:150)
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117)
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:895)
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808)
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1072)
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:965)
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)
	at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:909)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:555)
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:623)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:209)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:111)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
	at com.alibaba.druid.support.http.WebStatFilter.doFilter(WebStatFilter.java:114)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:337)
	at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.invoke(FilterSecurityInterceptor.java:115)
	at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.doFilter(FilterSecurityInterceptor.java:81)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(ExceptionTranslationFilter.java:122)
	at org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(ExceptionTranslationFilter.java:116)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.session.SessionManagementFilter.doFilter(SessionManagementFilter.java:126)
	at org.springframework.security.web.session.SessionManagementFilter.doFilter(SessionManagementFilter.java:81)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.authentication.AnonymousAuthenticationFilter.doFilter(AnonymousAuthenticationFilter.java:109)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter.doFilter(SecurityContextHolderAwareRequestFilter.java:149)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.savedrequest.RequestCacheAwareFilter.doFilter(RequestCacheAwareFilter.java:63)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at me.zhengjie.modules.security.security.TokenFilter.doFilter(TokenFilter.java:73)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:103)
	at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:89)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.web.filter.CorsFilter.doFilterInternal(CorsFilter.java:91)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.header.HeaderWriterFilter.doHeadersAfter(HeaderWriterFilter.java:90)
	at org.springframework.security.web.header.HeaderWriterFilter.doFilterInternal(HeaderWriterFilter.java:75)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.context.SecurityContextPersistenceFilter.doFilter(SecurityContextPersistenceFilter.java:112)
	at org.springframework.security.web.context.SecurityContextPersistenceFilter.doFilter(SecurityContextPersistenceFilter.java:82)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter.doFilterInternal(WebAsyncManagerIntegrationFilter.java:55)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.session.DisableEncodeUrlFilter.doFilterInternal(DisableEncodeUrlFilter.java:42)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.FilterChainProxy.doFilterInternal(FilterChainProxy.java:221)
	at org.springframework.security.web.FilterChainProxy.doFilter(FilterChainProxy.java:186)
	at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:354)
	at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:267)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
	at org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter.doFilterInternal(WebMvcMetricsFilter.java:96)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:168)
	at org.apache.catalina.core.StandardContextValve.__invoke(StandardContextValve.java:90)
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:41002)
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:481)
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:130)
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93)
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:342)
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:390)
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:928)
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1794)
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)
	at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)
	at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:750)
', '2025-12-02 15:55:24');
INSERT INTO "public"."sys_log" VALUES (13755, '用户登录', 'ERROR', 'me.zhengjie.modules.security.rest.AuthController.login()', '{"code":"11","password":"******","username":"admin","uuid":"1232323"}', '0:0:0:0:0:0:0:1', 9210, 'admin', 'IANA保留地址', 'Chrome 142', 'javax.crypto.BadPaddingException: Padding error in decryption
	at com.sun.crypto.provider.RSACipher.doFinal(RSACipher.java:385)
	at com.sun.crypto.provider.RSACipher.engineDoFinal(RSACipher.java:420)
	at javax.crypto.Cipher.doFinal(Cipher.java:2170)
	at me.zhengjie.utils.RsaUtils.doLongerCipherFinal(RsaUtils.java:144)
	at me.zhengjie.utils.RsaUtils.decryptByPrivateKey(RsaUtils.java:120)
	at me.zhengjie.modules.security.rest.AuthController.login(AuthController.java:83)
	at me.zhengjie.modules.security.rest.AuthController$$FastClassBySpringCGLIB$$1.invoke(<generated>)
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:792)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:762)
	at org.springframework.aop.aspectj.AspectJAfterThrowingAdvice.invoke(AspectJAfterThrowingAdvice.java:64)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:175)
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:762)
	at org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint.proceed(MethodInvocationProceedingJoinPoint.java:89)
	at me.zhengjie.aspect.LogAspect.logAround(LogAspect.java:68)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethodWithGivenArgs(AbstractAspectJAdvice.java:634)
	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethod(AbstractAspectJAdvice.java:624)
	at org.springframework.aop.aspectj.AspectJAroundAdvice.invoke(AspectJAroundAdvice.java:72)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:175)
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:762)
	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:97)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:762)
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:707)
	at me.zhengjie.modules.security.rest.AuthController$$EnhancerBySpringCGLIB$$1.login(<generated>)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:150)
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117)
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:895)
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808)
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1072)
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:965)
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)
	at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:909)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:555)
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:623)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:209)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:111)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
	at com.alibaba.druid.support.http.WebStatFilter.doFilter(WebStatFilter.java:114)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:337)
	at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.invoke(FilterSecurityInterceptor.java:115)
	at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.doFilter(FilterSecurityInterceptor.java:81)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(ExceptionTranslationFilter.java:122)
	at org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(ExceptionTranslationFilter.java:116)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.session.SessionManagementFilter.doFilter(SessionManagementFilter.java:126)
	at org.springframework.security.web.session.SessionManagementFilter.doFilter(SessionManagementFilter.java:81)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.authentication.AnonymousAuthenticationFilter.doFilter(AnonymousAuthenticationFilter.java:109)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter.doFilter(SecurityContextHolderAwareRequestFilter.java:149)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.savedrequest.RequestCacheAwareFilter.doFilter(RequestCacheAwareFilter.java:63)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at me.zhengjie.modules.security.security.TokenFilter.doFilter(TokenFilter.java:73)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:103)
	at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:89)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.web.filter.CorsFilter.doFilterInternal(CorsFilter.java:91)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.header.HeaderWriterFilter.doHeadersAfter(HeaderWriterFilter.java:90)
	at org.springframework.security.web.header.HeaderWriterFilter.doFilterInternal(HeaderWriterFilter.java:75)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.context.SecurityContextPersistenceFilter.doFilter(SecurityContextPersistenceFilter.java:112)
	at org.springframework.security.web.context.SecurityContextPersistenceFilter.doFilter(SecurityContextPersistenceFilter.java:82)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter.doFilterInternal(WebAsyncManagerIntegrationFilter.java:55)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.session.DisableEncodeUrlFilter.doFilterInternal(DisableEncodeUrlFilter.java:42)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
	at org.springframework.security.web.FilterChainProxy.doFilterInternal(FilterChainProxy.java:221)
	at org.springframework.security.web.FilterChainProxy.doFilter(FilterChainProxy.java:186)
	at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:354)
	at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:267)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
	at org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter.doFilterInternal(WebMvcMetricsFilter.java:96)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:168)
	at org.apache.catalina.core.StandardContextValve.__invoke(StandardContextValve.java:90)
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:41002)
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:481)
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:130)
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93)
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:342)
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:390)
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:928)
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1794)
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)
	at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)
	at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:750)
', '2025-12-02 15:57:50');
INSERT INTO "public"."sys_log" VALUES (13756, '用户登录', 'INFO', 'me.zhengjie.modules.security.rest.AuthController.login()', '{"code":"16","password":"******","username":"admin","uuid":"captcha_code:383630a56c93428784ae736371089916"}', '30.30.7.28', 14751, 'admin', '美国', 'Chrome 142', NULL, '2025-12-02 17:37:15');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_menu";
CREATE TABLE "public"."sys_menu" (
  "menu_id" int8 NOT NULL,
  "pid" int8,
  "sub_count" int4,
  "type" int4,
  "title" varchar(100) COLLATE "pg_catalog"."default",
  "name" varchar(100) COLLATE "pg_catalog"."default",
  "component" varchar(255) COLLATE "pg_catalog"."default",
  "menu_sort" int4,
  "icon" varchar(255) COLLATE "pg_catalog"."default",
  "path" varchar(255) COLLATE "pg_catalog"."default",
  "i_frame" varchar(1) COLLATE "pg_catalog"."default",
  "cache" varchar(1) COLLATE "pg_catalog"."default",
  "hidden" varchar(1) COLLATE "pg_catalog"."default",
  "permission" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_menu"."menu_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_menu"."pid" IS '上级菜单ID';
COMMENT ON COLUMN "public"."sys_menu"."sub_count" IS '子菜单数目';
COMMENT ON COLUMN "public"."sys_menu"."type" IS '菜单类型';
COMMENT ON COLUMN "public"."sys_menu"."title" IS '菜单标题';
COMMENT ON COLUMN "public"."sys_menu"."name" IS '组件名称';
COMMENT ON COLUMN "public"."sys_menu"."component" IS '组件';
COMMENT ON COLUMN "public"."sys_menu"."menu_sort" IS '排序';
COMMENT ON COLUMN "public"."sys_menu"."icon" IS '图标';
COMMENT ON COLUMN "public"."sys_menu"."path" IS '链接地址';
COMMENT ON COLUMN "public"."sys_menu"."i_frame" IS '是否外链';
COMMENT ON COLUMN "public"."sys_menu"."cache" IS '缓存';
COMMENT ON COLUMN "public"."sys_menu"."hidden" IS '隐藏';
COMMENT ON COLUMN "public"."sys_menu"."permission" IS '权限';
COMMENT ON COLUMN "public"."sys_menu"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_menu"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_menu"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_menu"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_menu" IS '系统菜单';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO "public"."sys_menu" VALUES (1, NULL, 7, 0, '系统管理', NULL, NULL, 1, 'system', 'system', '0', '0', '0', NULL, NULL, 'admin', '2018-12-18 15:11:29', '2025-01-14 15:48:18');
INSERT INTO "public"."sys_menu" VALUES (2, 1, 3, 1, '用户管理', 'User', 'system/user/index', 2, 'peoples', 'user', '0', '0', '0', 'user:list', NULL, NULL, '2018-12-18 15:14:44', NULL);
INSERT INTO "public"."sys_menu" VALUES (3, 1, 3, 1, '角色管理', 'Role', 'system/role/index', 3, 'role', 'role', '0', '0', '0', 'roles:list', NULL, NULL, '2018-12-18 15:16:07', NULL);
INSERT INTO "public"."sys_menu" VALUES (5, 1, 3, 1, '菜单管理', 'Menu', 'system/menu/index', 5, 'menu', 'menu', '0', '0', '0', 'menu:list', NULL, NULL, '2018-12-18 15:17:28', NULL);
INSERT INTO "public"."sys_menu" VALUES (6, NULL, 5, 0, '系统监控', NULL, NULL, 10, 'monitor', 'monitor', '0', '0', '0', NULL, NULL, NULL, '2018-12-18 15:17:48', NULL);
INSERT INTO "public"."sys_menu" VALUES (7, 6, 0, 1, '操作日志', 'Log', 'monitor/log/index', 11, 'log', 'logs', '0', '1', '0', NULL, NULL, 'admin', '2018-12-18 15:18:26', '2020-06-06 13:11:57');
INSERT INTO "public"."sys_menu" VALUES (9, 6, 0, 1, 'SQL监控', 'Sql', 'monitor/sql/index', 18, 'sqlMonitor', 'druid', '0', '0', '0', NULL, NULL, NULL, '2018-12-18 15:19:34', NULL);
INSERT INTO "public"."sys_menu" VALUES (10, NULL, 5, 0, '组件管理', NULL, NULL, 50, 'zujian', 'components', '0', '0', '0', NULL, NULL, NULL, '2018-12-19 13:38:16', NULL);
INSERT INTO "public"."sys_menu" VALUES (11, 10, 0, 1, '图标库', 'Icons', 'components/icons/index', 51, 'icon', 'icon', '0', '0', '0', NULL, NULL, NULL, '2018-12-19 13:38:49', NULL);
INSERT INTO "public"."sys_menu" VALUES (14, 36, 0, 1, '邮件工具', 'Email', 'tools/email/index', 35, 'email', 'email', '0', '0', '0', NULL, NULL, NULL, '2018-12-27 10:13:09', NULL);
INSERT INTO "public"."sys_menu" VALUES (15, 10, 0, 1, '富文本', 'Editor', 'components/Editor', 52, 'fwb', 'tinymce', '0', '0', '0', NULL, NULL, NULL, '2018-12-27 11:58:25', NULL);
INSERT INTO "public"."sys_menu" VALUES (18, 36, 1, 1, '存储管理', 'Storage', 'tools/storage/index', 34, 'qiniu', 'storage', '0', '0', '0', 'storage:list', NULL, NULL, '2018-12-31 11:12:15', NULL);
INSERT INTO "public"."sys_menu" VALUES (19, 36, 0, 1, '支付宝工具', 'AliPay', 'tools/aliPay/index', 37, 'alipay', 'aliPay', '0', '0', '0', NULL, NULL, NULL, '2018-12-31 14:52:38', NULL);
INSERT INTO "public"."sys_menu" VALUES (21, NULL, 2, 0, '多级菜单', NULL, '', 900, 'menu', 'nested', '0', '0', '0', NULL, NULL, 'admin', '2019-01-04 16:22:03', '2020-06-21 17:27:35');
INSERT INTO "public"."sys_menu" VALUES (22, 21, 2, 0, '二级菜单1', NULL, '', 999, 'menu', 'menu1', '0', '0', '0', NULL, NULL, 'admin', '2019-01-04 16:23:29', '2020-06-21 17:27:20');
INSERT INTO "public"."sys_menu" VALUES (23, 21, 0, 1, '二级菜单2', NULL, 'nested/menu2/index', 999, 'menu', 'menu2', '0', '0', '0', NULL, NULL, NULL, '2019-01-04 16:23:57', NULL);
INSERT INTO "public"."sys_menu" VALUES (24, 22, 0, 1, '三级菜单1', 'Test', 'nested/menu1/menu1-1', 999, 'menu', 'menu1-1', '0', '0', '0', NULL, NULL, NULL, '2019-01-04 16:24:48', NULL);
INSERT INTO "public"."sys_menu" VALUES (27, 22, 0, 1, '三级菜单2', NULL, 'nested/menu1/menu1-2', 999, 'menu', 'menu1-2', '0', '0', '0', NULL, NULL, NULL, '2019-01-07 17:27:32', NULL);
INSERT INTO "public"."sys_menu" VALUES (28, 1, 3, 1, '任务调度', 'Timing', 'system/timing/index', 999, 'timing', 'timing', '0', '0', '0', 'timing:list', NULL, NULL, '2019-01-07 20:34:40', NULL);
INSERT INTO "public"."sys_menu" VALUES (30, 36, 0, 1, '代码生成', 'GeneratorIndex', 'generator/index', 32, 'dev', 'generator', '0', '1', '0', NULL, NULL, NULL, '2019-01-11 15:45:55', NULL);
INSERT INTO "public"."sys_menu" VALUES (32, 6, 0, 1, '异常日志', 'ErrorLog', 'monitor/log/errorLog', 12, 'error', 'errorLog', '0', '0', '0', NULL, NULL, NULL, '2019-01-13 13:49:03', NULL);
INSERT INTO "public"."sys_menu" VALUES (33, 10, 0, 1, 'Markdown', 'Markdown', 'components/MarkDown', 53, 'markdown', 'markdown', '0', '0', '0', NULL, NULL, NULL, '2019-03-08 13:46:44', NULL);
INSERT INTO "public"."sys_menu" VALUES (34, 10, 0, 1, 'Yaml编辑器', 'YamlEdit', 'components/YamlEdit', 54, 'dev', 'yaml', '0', '0', '0', NULL, NULL, NULL, '2019-03-08 15:49:40', NULL);
INSERT INTO "public"."sys_menu" VALUES (35, 1, 3, 1, '部门管理', 'Dept', 'system/dept/index', 6, 'dept', 'dept', '0', '0', '0', 'dept:list', NULL, NULL, '2019-03-25 09:46:00', NULL);
INSERT INTO "public"."sys_menu" VALUES (36, NULL, 6, 0, '系统工具', NULL, '', 30, 'sys-tools', 'sys-tools', '0', '0', '0', NULL, NULL, NULL, '2019-03-29 10:57:35', NULL);
INSERT INTO "public"."sys_menu" VALUES (37, 1, 3, 1, '岗位管理', 'Job', 'system/job/index', 7, 'Steve-Jobs', 'job', '0', '0', '0', 'job:list', NULL, NULL, '2019-03-29 13:51:18', NULL);
INSERT INTO "public"."sys_menu" VALUES (39, 1, 3, 1, '字典管理', 'Dict', 'system/dict/index', 8, 'dictionary', 'dict', '0', '0', '0', 'dict:list', NULL, NULL, '2019-04-10 11:49:04', NULL);
INSERT INTO "public"."sys_menu" VALUES (41, 6, 0, 1, '在线用户', 'OnlineUser', 'monitor/online/index', 10, 'Steve-Jobs', 'online', '0', '0', '0', NULL, NULL, NULL, '2019-10-26 22:08:43', NULL);
INSERT INTO "public"."sys_menu" VALUES (44, 2, 0, 2, '用户新增', NULL, '', 2, '', '', '0', '0', '0', 'user:add', NULL, NULL, '2019-10-29 10:59:46', NULL);
INSERT INTO "public"."sys_menu" VALUES (45, 2, 0, 2, '用户编辑', NULL, '', 3, '', '', '0', '0', '0', 'user:edit', NULL, NULL, '2019-10-29 11:00:08', NULL);
INSERT INTO "public"."sys_menu" VALUES (46, 2, 0, 2, '用户删除', NULL, '', 4, '', '', '0', '0', '0', 'user:del', NULL, NULL, '2019-10-29 11:00:23', NULL);
INSERT INTO "public"."sys_menu" VALUES (48, 3, 0, 2, '角色创建', NULL, '', 2, '', '', '0', '0', '0', 'roles:add', NULL, NULL, '2019-10-29 12:45:34', NULL);
INSERT INTO "public"."sys_menu" VALUES (49, 3, 0, 2, '角色修改', NULL, '', 3, '', '', '0', '0', '0', 'roles:edit', NULL, NULL, '2019-10-29 12:46:16', NULL);
INSERT INTO "public"."sys_menu" VALUES (50, 3, 0, 2, '角色删除', NULL, '', 4, '', '', '0', '0', '0', 'roles:del', NULL, NULL, '2019-10-29 12:46:51', NULL);
INSERT INTO "public"."sys_menu" VALUES (52, 5, 0, 2, '菜单新增', NULL, '', 2, '', '', '0', '0', '0', 'menu:add', NULL, NULL, '2019-10-29 12:55:07', NULL);
INSERT INTO "public"."sys_menu" VALUES (53, 5, 0, 2, '菜单编辑', NULL, '', 3, '', '', '0', '0', '0', 'menu:edit', NULL, NULL, '2019-10-29 12:55:40', NULL);
INSERT INTO "public"."sys_menu" VALUES (54, 5, 0, 2, '菜单删除', NULL, '', 4, '', '', '0', '0', '0', 'menu:del', NULL, NULL, '2019-10-29 12:56:00', NULL);
INSERT INTO "public"."sys_menu" VALUES (56, 35, 0, 2, '部门新增', NULL, '', 2, '', '', '0', '0', '0', 'dept:add', NULL, NULL, '2019-10-29 12:57:09', NULL);
INSERT INTO "public"."sys_menu" VALUES (57, 35, 0, 2, '部门编辑', NULL, '', 3, '', '', '0', '0', '0', 'dept:edit', NULL, NULL, '2019-10-29 12:57:27', NULL);
INSERT INTO "public"."sys_menu" VALUES (58, 35, 0, 2, '部门删除', NULL, '', 4, '', '', '0', '0', '0', 'dept:del', NULL, NULL, '2019-10-29 12:57:41', NULL);
INSERT INTO "public"."sys_menu" VALUES (60, 37, 0, 2, '岗位新增', NULL, '', 2, '', '', '0', '0', '0', 'job:add', NULL, NULL, '2019-10-29 12:58:27', NULL);
INSERT INTO "public"."sys_menu" VALUES (61, 37, 0, 2, '岗位编辑', NULL, '', 3, '', '', '0', '0', '0', 'job:edit', NULL, NULL, '2019-10-29 12:58:45', NULL);
INSERT INTO "public"."sys_menu" VALUES (62, 37, 0, 2, '岗位删除', NULL, '', 4, '', '', '0', '0', '0', 'job:del', NULL, NULL, '2019-10-29 12:59:04', NULL);
INSERT INTO "public"."sys_menu" VALUES (64, 39, 0, 2, '字典新增', NULL, '', 2, '', '', '0', '0', '0', 'dict:add', NULL, NULL, '2019-10-29 13:00:17', NULL);
INSERT INTO "public"."sys_menu" VALUES (65, 39, 0, 2, '字典编辑', NULL, '', 3, '', '', '0', '0', '0', 'dict:edit', NULL, NULL, '2019-10-29 13:00:42', NULL);
INSERT INTO "public"."sys_menu" VALUES (66, 39, 0, 2, '字典删除', NULL, '', 4, '', '', '0', '0', '0', 'dict:del', NULL, NULL, '2019-10-29 13:00:59', NULL);
INSERT INTO "public"."sys_menu" VALUES (73, 28, 0, 2, '任务新增', NULL, '', 2, '', '', '0', '0', '0', 'timing:add', NULL, NULL, '2019-10-29 13:07:28', NULL);
INSERT INTO "public"."sys_menu" VALUES (74, 28, 0, 2, '任务编辑', NULL, '', 3, '', '', '0', '0', '0', 'timing:edit', NULL, NULL, '2019-10-29 13:07:41', NULL);
INSERT INTO "public"."sys_menu" VALUES (75, 28, 0, 2, '任务删除', NULL, '', 4, '', '', '0', '0', '0', 'timing:del', NULL, NULL, '2019-10-29 13:07:54', NULL);
INSERT INTO "public"."sys_menu" VALUES (79, 18, 0, 2, '文件删除', NULL, '', 4, '', '', '0', '0', '0', 'storage:del', NULL, NULL, '2019-10-29 13:09:34', NULL);
INSERT INTO "public"."sys_menu" VALUES (80, 6, 0, 1, '服务监控', 'ServerMonitor', 'monitor/server/index', 14, 'codeConsole', 'server', '0', '0', '0', 'monitor:list', NULL, 'admin', '2019-11-07 13:06:39', '2020-05-04 18:20:50');
INSERT INTO "public"."sys_menu" VALUES (82, 36, 0, 1, '生成配置', 'GeneratorConfig', 'generator/config', 33, 'dev', 'generator/config/:tableName', '0', '1', '1', '', NULL, NULL, '2019-11-17 20:08:56', NULL);
INSERT INTO "public"."sys_menu" VALUES (83, 10, 0, 1, '图表库', 'Echarts', 'components/Echarts', 50, 'chart', 'echarts', '0', '1', '0', '', NULL, NULL, '2019-11-21 09:04:32', NULL);
INSERT INTO "public"."sys_menu" VALUES (90, NULL, 5, 1, '运维管理', 'Mnt', '', 20, 'mnt', 'mnt', '0', '0', '0', NULL, NULL, NULL, '2019-11-09 10:31:08', NULL);
INSERT INTO "public"."sys_menu" VALUES (92, 90, 3, 1, '服务器', 'ServerDeploy', 'maint/server/index', 22, 'server', 'maint/serverDeploy', '0', '0', '0', 'serverDeploy:list', NULL, NULL, '2019-11-10 10:29:25', NULL);
INSERT INTO "public"."sys_menu" VALUES (93, 90, 3, 1, '应用管理', 'App', 'maint/app/index', 23, 'app', 'maint/app', '0', '0', '0', 'app:list', NULL, NULL, '2019-11-10 11:05:16', NULL);
INSERT INTO "public"."sys_menu" VALUES (94, 90, 3, 1, '部署管理', 'Deploy', 'maint/deploy/index', 24, 'deploy', 'maint/deploy', '0', '0', '0', 'deploy:list', NULL, NULL, '2019-11-10 15:56:55', NULL);
INSERT INTO "public"."sys_menu" VALUES (97, 90, 1, 1, '部署备份', 'DeployHistory', 'maint/deployHistory/index', 25, 'backup', 'maint/deployHistory', '0', '0', '0', 'deployHistory:list', NULL, NULL, '2019-11-10 16:49:44', NULL);
INSERT INTO "public"."sys_menu" VALUES (98, 90, 3, 1, '数据库管理', 'Database', 'maint/database/index', 26, 'database', 'maint/database', '0', '0', '0', 'database:list', NULL, NULL, '2019-11-10 20:40:04', NULL);
INSERT INTO "public"."sys_menu" VALUES (102, 97, 0, 2, '删除', NULL, '', 999, '', '', '0', '0', '0', 'deployHistory:del', NULL, NULL, '2019-11-17 09:32:48', NULL);
INSERT INTO "public"."sys_menu" VALUES (103, 92, 0, 2, '服务器新增', NULL, '', 999, '', '', '0', '0', '0', 'serverDeploy:add', NULL, NULL, '2019-11-17 11:08:33', NULL);
INSERT INTO "public"."sys_menu" VALUES (104, 92, 0, 2, '服务器编辑', NULL, '', 999, '', '', '0', '0', '0', 'serverDeploy:edit', NULL, NULL, '2019-11-17 11:08:57', NULL);
INSERT INTO "public"."sys_menu" VALUES (105, 92, 0, 2, '服务器删除', NULL, '', 999, '', '', '0', '0', '0', 'serverDeploy:del', NULL, NULL, '2019-11-17 11:09:15', NULL);
INSERT INTO "public"."sys_menu" VALUES (106, 93, 0, 2, '应用新增', NULL, '', 999, '', '', '0', '0', '0', 'app:add', NULL, NULL, '2019-11-17 11:10:03', NULL);
INSERT INTO "public"."sys_menu" VALUES (107, 93, 0, 2, '应用编辑', NULL, '', 999, '', '', '0', '0', '0', 'app:edit', NULL, NULL, '2019-11-17 11:10:28', NULL);
INSERT INTO "public"."sys_menu" VALUES (108, 93, 0, 2, '应用删除', NULL, '', 999, '', '', '0', '0', '0', 'app:del', NULL, NULL, '2019-11-17 11:10:55', NULL);
INSERT INTO "public"."sys_menu" VALUES (109, 94, 0, 2, '部署新增', NULL, '', 999, '', '', '0', '0', '0', 'deploy:add', NULL, NULL, '2019-11-17 11:11:22', NULL);
INSERT INTO "public"."sys_menu" VALUES (110, 94, 0, 2, '部署编辑', NULL, '', 999, '', '', '0', '0', '0', 'deploy:edit', NULL, NULL, '2019-11-17 11:11:41', NULL);
INSERT INTO "public"."sys_menu" VALUES (111, 94, 0, 2, '部署删除', NULL, '', 999, '', '', '0', '0', '0', 'deploy:del', NULL, NULL, '2019-11-17 11:12:01', NULL);
INSERT INTO "public"."sys_menu" VALUES (116, 36, 0, 1, '生成预览', 'Preview', 'generator/preview', 999, 'java', 'generator/preview/:tableName', '0', '1', '1', NULL, NULL, NULL, '2019-11-26 14:54:36', NULL);

-- ----------------------------
-- Table structure for sys_quartz_job
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_quartz_job";
CREATE TABLE "public"."sys_quartz_job" (
  "job_id" int8 NOT NULL,
  "bean_name" varchar(255) COLLATE "pg_catalog"."default",
  "cron_expression" varchar(255) COLLATE "pg_catalog"."default",
  "is_pause" varchar(1) COLLATE "pg_catalog"."default",
  "job_name" varchar(255) COLLATE "pg_catalog"."default",
  "method_name" varchar(255) COLLATE "pg_catalog"."default",
  "params" varchar(255) COLLATE "pg_catalog"."default",
  "description" varchar(255) COLLATE "pg_catalog"."default",
  "person_in_charge" varchar(100) COLLATE "pg_catalog"."default",
  "email" varchar(100) COLLATE "pg_catalog"."default",
  "sub_task" varchar(100) COLLATE "pg_catalog"."default",
  "pause_after_failure" varchar(1) COLLATE "pg_catalog"."default",
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_quartz_job"."job_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_quartz_job"."bean_name" IS 'Spring Bean名称';
COMMENT ON COLUMN "public"."sys_quartz_job"."cron_expression" IS 'cron 表达式';
COMMENT ON COLUMN "public"."sys_quartz_job"."is_pause" IS '状态：1暂停、0启用';
COMMENT ON COLUMN "public"."sys_quartz_job"."job_name" IS '任务名称';
COMMENT ON COLUMN "public"."sys_quartz_job"."method_name" IS '方法名称';
COMMENT ON COLUMN "public"."sys_quartz_job"."params" IS '参数';
COMMENT ON COLUMN "public"."sys_quartz_job"."description" IS '备注';
COMMENT ON COLUMN "public"."sys_quartz_job"."person_in_charge" IS '负责人';
COMMENT ON COLUMN "public"."sys_quartz_job"."email" IS '报警邮箱';
COMMENT ON COLUMN "public"."sys_quartz_job"."sub_task" IS '子任务ID';
COMMENT ON COLUMN "public"."sys_quartz_job"."pause_after_failure" IS '任务失败后是否暂停';
COMMENT ON COLUMN "public"."sys_quartz_job"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_quartz_job"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_quartz_job"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_quartz_job"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_quartz_job" IS '定时任务';

-- ----------------------------
-- Records of sys_quartz_job
-- ----------------------------
INSERT INTO "public"."sys_quartz_job" VALUES (2, 'testTask', '0/5 * * * * ?', '1', '测试1', 'run1', 'test', '带参测试，多参使用json', '测试', NULL, NULL, NULL, NULL, 'admin', '2019-08-22 14:08:29', '2020-05-24 13:58:33');
INSERT INTO "public"."sys_quartz_job" VALUES (3, 'testTask', '0/5 * * * * ?', '1', '测试', 'run', '', '不带参测试', 'Zheng Jie', '', '6', '1', NULL, 'admin', '2019-09-26 16:44:39', '2020-05-24 14:48:12');
INSERT INTO "public"."sys_quartz_job" VALUES (5, 'Test', '0/5 * * * * ?', '1', '任务告警测试', 'run', NULL, '测试', 'test', '', NULL, '1', 'admin', 'admin', '2020-05-05 20:32:41', '2020-05-05 20:36:13');
INSERT INTO "public"."sys_quartz_job" VALUES (6, 'testTask', '0/5 * * * * ?', '1', '测试3', 'run2', NULL, '测试3', 'Zheng Jie', '', NULL, '1', 'admin', 'admin', '2020-05-05 20:35:41', '2020-05-05 20:36:07');

-- ----------------------------
-- Table structure for sys_quartz_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_quartz_log";
CREATE TABLE "public"."sys_quartz_log" (
  "log_id" int8 NOT NULL,
  "bean_name" varchar(255) COLLATE "pg_catalog"."default",
  "cron_expression" varchar(255) COLLATE "pg_catalog"."default",
  "is_success" varchar(1) COLLATE "pg_catalog"."default",
  "job_name" varchar(255) COLLATE "pg_catalog"."default",
  "method_name" varchar(255) COLLATE "pg_catalog"."default",
  "params" varchar(255) COLLATE "pg_catalog"."default",
  "time" int8,
  "exception_detail" text COLLATE "pg_catalog"."default",
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_quartz_log"."log_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_quartz_log"."bean_name" IS 'Bean名称';
COMMENT ON COLUMN "public"."sys_quartz_log"."cron_expression" IS 'cron 表达式';
COMMENT ON COLUMN "public"."sys_quartz_log"."is_success" IS '是否执行成功';
COMMENT ON COLUMN "public"."sys_quartz_log"."job_name" IS '任务名称';
COMMENT ON COLUMN "public"."sys_quartz_log"."method_name" IS '方法名称';
COMMENT ON COLUMN "public"."sys_quartz_log"."params" IS '参数';
COMMENT ON COLUMN "public"."sys_quartz_log"."time" IS '执行耗时';
COMMENT ON COLUMN "public"."sys_quartz_log"."exception_detail" IS '异常详情';
COMMENT ON COLUMN "public"."sys_quartz_log"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."sys_quartz_log" IS '定时任务日志';

-- ----------------------------
-- Records of sys_quartz_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role";
CREATE TABLE "public"."sys_role" (
  "role_id" int8 NOT NULL,
  "name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "level" int4,
  "data_scope" varchar(255) COLLATE "pg_catalog"."default",
  "description" varchar(255) COLLATE "pg_catalog"."default",
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_role"."role_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_role"."name" IS '名称';
COMMENT ON COLUMN "public"."sys_role"."level" IS '角色级别';
COMMENT ON COLUMN "public"."sys_role"."data_scope" IS '数据权限';
COMMENT ON COLUMN "public"."sys_role"."description" IS '角色描述';
COMMENT ON COLUMN "public"."sys_role"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_role"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_role"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_role"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_role" IS '角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO "public"."sys_role" VALUES (1, '管理员', 1, '全部', NULL, NULL, 'admin', '2018-11-23 11:04:37', '2020-08-06 16:10:24');
INSERT INTO "public"."sys_role" VALUES (2, '普通用户', 2, '本级', NULL, NULL, 'admin', '2018-11-23 13:09:06', '2020-09-05 10:45:12');

-- ----------------------------
-- Table structure for sys_roles_depts
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_roles_depts";
CREATE TABLE "public"."sys_roles_depts" (
  "role_id" int8 NOT NULL,
  "dept_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_roles_depts"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."sys_roles_depts"."dept_id" IS '部门ID';
COMMENT ON TABLE "public"."sys_roles_depts" IS '角色部门关联';

-- ----------------------------
-- Records of sys_roles_depts
-- ----------------------------

-- ----------------------------
-- Table structure for sys_roles_menus
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_roles_menus";
CREATE TABLE "public"."sys_roles_menus" (
  "menu_id" int8 NOT NULL,
  "role_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_roles_menus"."menu_id" IS '菜单ID';
COMMENT ON COLUMN "public"."sys_roles_menus"."role_id" IS '角色ID';
COMMENT ON TABLE "public"."sys_roles_menus" IS '角色菜单关联';

-- ----------------------------
-- Records of sys_roles_menus
-- ----------------------------
INSERT INTO "public"."sys_roles_menus" VALUES (1, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (1, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (2, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (2, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (3, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (5, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (6, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (6, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (7, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (7, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (9, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (9, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (10, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (10, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (11, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (11, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (14, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (14, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (15, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (15, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (18, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (19, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (19, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (21, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (21, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (22, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (22, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (23, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (23, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (24, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (24, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (27, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (27, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (28, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (30, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (30, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (32, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (32, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (33, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (33, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (34, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (34, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (35, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (36, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (36, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (37, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (39, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (41, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (44, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (45, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (46, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (48, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (49, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (50, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (52, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (53, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (54, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (56, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (57, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (58, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (60, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (61, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (62, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (64, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (65, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (66, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (73, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (74, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (75, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (79, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (80, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (80, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (82, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (82, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (83, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (83, 2);
INSERT INTO "public"."sys_roles_menus" VALUES (90, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (92, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (93, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (94, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (97, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (98, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (102, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (103, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (104, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (105, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (106, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (107, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (108, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (109, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (110, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (111, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (116, 1);
INSERT INTO "public"."sys_roles_menus" VALUES (116, 2);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user";
CREATE TABLE "public"."sys_user" (
  "user_id" int8 NOT NULL,
  "dept_id" int8,
  "username" varchar(180) COLLATE "pg_catalog"."default",
  "nick_name" varchar(255) COLLATE "pg_catalog"."default",
  "gender" varchar(2) COLLATE "pg_catalog"."default",
  "phone" varchar(255) COLLATE "pg_catalog"."default",
  "email" varchar(180) COLLATE "pg_catalog"."default",
  "avatar_name" varchar(255) COLLATE "pg_catalog"."default",
  "avatar_path" varchar(255) COLLATE "pg_catalog"."default",
  "password" varchar(255) COLLATE "pg_catalog"."default",
  "is_admin" varchar(1) COLLATE "pg_catalog"."default",
  "enabled" varchar(1) COLLATE "pg_catalog"."default",
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "pwd_reset_time" timestamp(6),
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_user"."user_id" IS 'ID';
COMMENT ON COLUMN "public"."sys_user"."dept_id" IS '部门名称';
COMMENT ON COLUMN "public"."sys_user"."username" IS '用户名';
COMMENT ON COLUMN "public"."sys_user"."nick_name" IS '昵称';
COMMENT ON COLUMN "public"."sys_user"."gender" IS '性别';
COMMENT ON COLUMN "public"."sys_user"."phone" IS '手机号码';
COMMENT ON COLUMN "public"."sys_user"."email" IS '邮箱';
COMMENT ON COLUMN "public"."sys_user"."avatar_name" IS '头像地址';
COMMENT ON COLUMN "public"."sys_user"."avatar_path" IS '头像真实路径';
COMMENT ON COLUMN "public"."sys_user"."password" IS '密码';
COMMENT ON COLUMN "public"."sys_user"."is_admin" IS '是否为admin账号';
COMMENT ON COLUMN "public"."sys_user"."enabled" IS '状态：1启用、0禁用';
COMMENT ON COLUMN "public"."sys_user"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_user"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_user"."pwd_reset_time" IS '修改密码的时间';
COMMENT ON COLUMN "public"."sys_user"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."sys_user"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_user" IS '系统用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "public"."sys_user" VALUES (1, 2, 'admin', '管理员', '男', '18888888888', '201507802@qq.com', 'avatar-20250121112710866.png', '/Users/jie/Documents/work/private/eladmin-mp/~/avatar/avatar-20250121112710866.png', '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', '1', '1', NULL, 'admin', '2020-05-03 16:38:31', '2018-08-23 09:11:56', '2020-09-05 10:43:31');
INSERT INTO "public"."sys_user" VALUES (2, 2, 'test', '测试', '男', '19999999999', '231@qq.com', NULL, NULL, '$2a$10$BSR9oUNtzWhnqs8NmZk5Zu3zfsNop3KxZO0xGEzy01cumf9k/AW6.', '0', '1', 'admin', 'admin', '2025-01-21 15:25:12', '2020-05-05 11:15:49', '2020-09-05 10:43:38');

-- ----------------------------
-- Table structure for sys_users_jobs
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_users_jobs";
CREATE TABLE "public"."sys_users_jobs" (
  "user_id" int8 NOT NULL,
  "job_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_users_jobs"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_users_jobs"."job_id" IS '岗位ID';
COMMENT ON TABLE "public"."sys_users_jobs" IS '用户与岗位关联表';

-- ----------------------------
-- Records of sys_users_jobs
-- ----------------------------
INSERT INTO "public"."sys_users_jobs" VALUES (1, 11);
INSERT INTO "public"."sys_users_jobs" VALUES (2, 11);

-- ----------------------------
-- Table structure for sys_users_roles
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_users_roles";
CREATE TABLE "public"."sys_users_roles" (
  "user_id" int8 NOT NULL,
  "role_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_users_roles"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_users_roles"."role_id" IS '角色ID';
COMMENT ON TABLE "public"."sys_users_roles" IS '用户角色关联';

-- ----------------------------
-- Records of sys_users_roles
-- ----------------------------
INSERT INTO "public"."sys_users_roles" VALUES (1, 1);
INSERT INTO "public"."sys_users_roles" VALUES (2, 2);

-- ----------------------------
-- Table structure for tool_alipay_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."tool_alipay_config";
CREATE TABLE "public"."tool_alipay_config" (
  "config_id" int8 NOT NULL,
  "app_id" varchar(255) COLLATE "pg_catalog"."default",
  "charset" varchar(255) COLLATE "pg_catalog"."default",
  "format" varchar(255) COLLATE "pg_catalog"."default",
  "gateway_url" varchar(255) COLLATE "pg_catalog"."default",
  "notify_url" varchar(255) COLLATE "pg_catalog"."default",
  "private_key" text COLLATE "pg_catalog"."default",
  "public_key" text COLLATE "pg_catalog"."default",
  "return_url" varchar(255) COLLATE "pg_catalog"."default",
  "sign_type" varchar(255) COLLATE "pg_catalog"."default",
  "sys_service_provider_id" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."tool_alipay_config"."config_id" IS 'ID';
COMMENT ON COLUMN "public"."tool_alipay_config"."app_id" IS '应用ID';
COMMENT ON COLUMN "public"."tool_alipay_config"."charset" IS '编码';
COMMENT ON COLUMN "public"."tool_alipay_config"."format" IS '类型 固定格式json';
COMMENT ON COLUMN "public"."tool_alipay_config"."gateway_url" IS '网关地址';
COMMENT ON COLUMN "public"."tool_alipay_config"."notify_url" IS '异步回调';
COMMENT ON COLUMN "public"."tool_alipay_config"."private_key" IS '私钥';
COMMENT ON COLUMN "public"."tool_alipay_config"."public_key" IS '公钥';
COMMENT ON COLUMN "public"."tool_alipay_config"."return_url" IS '回调地址';
COMMENT ON COLUMN "public"."tool_alipay_config"."sign_type" IS '签名方式';
COMMENT ON COLUMN "public"."tool_alipay_config"."sys_service_provider_id" IS '商户号';
COMMENT ON TABLE "public"."tool_alipay_config" IS '支付宝配置类';

-- ----------------------------
-- Records of tool_alipay_config
-- ----------------------------
INSERT INTO "public"."tool_alipay_config" VALUES (1, '2016091700532697', 'utf-8', 'JSON', 'https://openapi.alipaydev.com/gateway.do', 'http://api.auauz.net/api/aliPay/notify', 'MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC5js8sInU10AJ0cAQ8UMMyXrQ+oHZEkVt5lBwsStmTJ7YikVYgbskx1YYEXTojRsWCb+SH/kDmDU4pK/u91SJ4KFCRMF2411piYuXU/jF96zKrADznYh/zAraqT6hvAIVtQAlMHN53nx16rLzZ/8jDEkaSwT7+HvHiS+7sxSojnu/3oV7BtgISoUNstmSe8WpWHOaWv19xyS+Mce9MY4BfseFhzTICUymUQdd/8hXA28/H6osUfAgsnxAKv7Wil3aJSgaJczWuflYOve0dJ3InZkhw5Cvr0atwpk8YKBQjy5CdkoHqvkOcIB+cYHXJKzOE5tqU7inSwVbHzOLQ3XbnAgMBAAECggEAVJp5eT0Ixg1eYSqFs9568WdetUNCSUchNxDBu6wxAbhUgfRUGZuJnnAll63OCTGGck+EGkFh48JjRcBpGoeoHLL88QXlZZbC/iLrea6gcDIhuvfzzOffe1RcZtDFEj9hlotg8dQj1tS0gy9pN9g4+EBH7zeu+fyv+qb2e/v1l6FkISXUjpkD7RLQr3ykjiiEw9BpeKb7j5s7Kdx1NNIzhkcQKNqlk8JrTGDNInbDM6inZfwwIO2R1DHinwdfKWkvOTODTYa2MoAvVMFT9Bec9FbLpoWp7ogv1JMV9svgrcF9XLzANZ/OQvkbe9TV9GWYvIbxN6qwQioKCWO4GPnCAQKBgQDgW5MgfhX8yjXqoaUy/d1VjI8dHeIyw8d+OBAYwaxRSlCfyQ+tieWcR2HdTzPca0T0GkWcKZm0ei5xRURgxt4DUDLXNh26HG0qObbtLJdu/AuBUuCqgOiLqJ2f1uIbrz6OZUHns+bT/jGW2Ws8+C13zTCZkZt9CaQsrp3QOGDx5wKBgQDTul39hp3ZPwGNFeZdkGoUoViOSd5Lhowd5wYMGAEXWRLlU8z+smT5v0POz9JnIbCRchIY2FAPKRdVTICzmPk2EPJFxYTcwaNbVqL6lN7J2IlXXMiit5QbiLauo55w7plwV6LQmKm9KV7JsZs5XwqF7CEovI7GevFzyD3w+uizAQKBgC3LY1eRhOlpWOIAhpjG6qOoohmeXOphvdmMlfSHq6WYFqbWwmV4rS5d/6LNpNdL6fItXqIGd8I34jzql49taCmi+A2nlR/E559j0mvM20gjGDIYeZUz5MOE8k+K6/IcrhcgofgqZ2ZED1ksHdB/E8DNWCswZl16V1FrfvjeWSNnAoGAMrBplCrIW5xz+J0Hm9rZKrs+AkK5D4fUv8vxbK/KgxZ2KaUYbNm0xv39c+PZUYuFRCz1HDGdaSPDTE6WeWjkMQd5mS6ikl9hhpqFRkyh0d0fdGToO9yLftQKOGE/q3XUEktI1XvXF0xyPwNgUCnq0QkpHyGVZPtGFxwXiDvpvgECgYA5PoB+nY8iDiRaJNko9w0hL4AeKogwf+4TbCw+KWVEn6jhuJa4LFTdSqp89PktQaoVpwv92el/AhYjWOl/jVCm122f9b7GyoelbjMNolToDwe5pF5RnSpEuDdLy9MfE8LnE3PlbE7E5BipQ3UjSebkgNboLHH/lNZA5qvEtvbfvQ==', 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAut9evKRuHJ/2QNfDlLwvN/S8l9hRAgPbb0u61bm4AtzaTGsLeMtScetxTWJnVvAVpMS9luhEJjt+Sbk5TNLArsgzzwARgaTKOLMT1TvWAK5EbHyI+eSrc3s7Awe1VYGwcubRFWDm16eQLv0k7iqiw+4mweHSz/wWyvBJVgwLoQ02btVtAQErCfSJCOmt0Q/oJQjj08YNRV4EKzB19+f5A+HQVAKy72dSybTzAK+3FPtTtNen/+b5wGeat7c32dhYHnGorPkPeXLtsqqUTp1su5fMfd4lElNdZaoCI7osZxWWUo17vBCZnyeXc9fk0qwD9mK6yRAxNbrY72Xx5VqIqwIDAQAB', 'http://api.auauz.net/api/aliPay/return', 'RSA2', '2088102176044281');

-- ----------------------------
-- Table structure for tool_email_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."tool_email_config";
CREATE TABLE "public"."tool_email_config" (
  "config_id" int8 NOT NULL,
  "from_user" varchar(255) COLLATE "pg_catalog"."default",
  "host" varchar(255) COLLATE "pg_catalog"."default",
  "pass" varchar(255) COLLATE "pg_catalog"."default",
  "port" varchar(255) COLLATE "pg_catalog"."default",
  "user" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."tool_email_config"."config_id" IS 'ID';
COMMENT ON COLUMN "public"."tool_email_config"."from_user" IS '收件人';
COMMENT ON COLUMN "public"."tool_email_config"."host" IS '邮件服务器SMTP地址';
COMMENT ON COLUMN "public"."tool_email_config"."pass" IS '密码';
COMMENT ON COLUMN "public"."tool_email_config"."port" IS '端口';
COMMENT ON COLUMN "public"."tool_email_config"."user" IS '发件者用户名';
COMMENT ON TABLE "public"."tool_email_config" IS '邮箱配置';

-- ----------------------------
-- Records of tool_email_config
-- ----------------------------

-- ----------------------------
-- Table structure for tool_local_storage
-- ----------------------------
DROP TABLE IF EXISTS "public"."tool_local_storage";
CREATE TABLE "public"."tool_local_storage" (
  "storage_id" int8 NOT NULL,
  "real_name" varchar(255) COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "suffix" varchar(255) COLLATE "pg_catalog"."default",
  "path" varchar(255) COLLATE "pg_catalog"."default",
  "type" varchar(255) COLLATE "pg_catalog"."default",
  "size" varchar(256) COLLATE "pg_catalog"."default",
  "create_by" varchar(255) COLLATE "pg_catalog"."default",
  "update_by" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."tool_local_storage"."storage_id" IS 'ID';
COMMENT ON COLUMN "public"."tool_local_storage"."real_name" IS '文件真实的名称';
COMMENT ON COLUMN "public"."tool_local_storage"."name" IS '文件名';
COMMENT ON COLUMN "public"."tool_local_storage"."suffix" IS '文件后缀';
COMMENT ON COLUMN "public"."tool_local_storage"."path" IS '文件存储路径';
COMMENT ON COLUMN "public"."tool_local_storage"."type" IS '文件类型';
COMMENT ON COLUMN "public"."tool_local_storage"."size" IS '文件大小';
COMMENT ON COLUMN "public"."tool_local_storage"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."tool_local_storage"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."tool_local_storage"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."tool_local_storage"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."tool_local_storage" IS '本地存储';

-- ----------------------------
-- Records of tool_local_storage
-- ----------------------------

-- ----------------------------
-- Table structure for tool_s3_storage
-- ----------------------------
DROP TABLE IF EXISTS "public"."tool_s3_storage";
CREATE TABLE "public"."tool_s3_storage" (
  "storage_id" int8 NOT NULL,
  "file_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "file_real_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "file_size" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "file_mime_type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "file_type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "file_path" text COLLATE "pg_catalog"."default" NOT NULL,
  "create_by" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "update_by" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" timestamp(6) NOT NULL,
  "update_time" timestamp(6) NOT NULL
)
;
COMMENT ON COLUMN "public"."tool_s3_storage"."storage_id" IS '主键';
COMMENT ON COLUMN "public"."tool_s3_storage"."file_name" IS '文件名称';
COMMENT ON COLUMN "public"."tool_s3_storage"."file_real_name" IS '真实存储的名称';
COMMENT ON COLUMN "public"."tool_s3_storage"."file_size" IS '文件大小';
COMMENT ON COLUMN "public"."tool_s3_storage"."file_mime_type" IS '文件MIME 类型';
COMMENT ON COLUMN "public"."tool_s3_storage"."file_type" IS '文件类型';
COMMENT ON COLUMN "public"."tool_s3_storage"."file_path" IS '文件路径';
COMMENT ON COLUMN "public"."tool_s3_storage"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."tool_s3_storage"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."tool_s3_storage"."create_time" IS '创建日期';
COMMENT ON COLUMN "public"."tool_s3_storage"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."tool_s3_storage" IS 's3 协议对象存储';

-- ----------------------------
-- Records of tool_s3_storage
-- ----------------------------

-- ----------------------------
-- Indexes structure for table code_column
-- ----------------------------
CREATE INDEX "idx_table_name" ON "public"."code_column" USING btree (
  "table_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table code_column
-- ----------------------------
ALTER TABLE "public"."code_column" ADD CONSTRAINT "code_column_pkey" PRIMARY KEY ("column_id");

-- ----------------------------
-- Primary Key structure for table code_config
-- ----------------------------
ALTER TABLE "public"."code_config" ADD CONSTRAINT "code_config_pkey" PRIMARY KEY ("config_id");

-- ----------------------------
-- Primary Key structure for table mnt_app
-- ----------------------------
ALTER TABLE "public"."mnt_app" ADD CONSTRAINT "mnt_app_pkey" PRIMARY KEY ("app_id");

-- ----------------------------
-- Primary Key structure for table mnt_database
-- ----------------------------
ALTER TABLE "public"."mnt_database" ADD CONSTRAINT "mnt_database_pkey" PRIMARY KEY ("db_id");

-- ----------------------------
-- Indexes structure for table mnt_deploy
-- ----------------------------
CREATE INDEX "idx_app_id" ON "public"."mnt_deploy" USING btree (
  "app_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table mnt_deploy
-- ----------------------------
ALTER TABLE "public"."mnt_deploy" ADD CONSTRAINT "mnt_deploy_pkey" PRIMARY KEY ("deploy_id");

-- ----------------------------
-- Primary Key structure for table mnt_deploy_history
-- ----------------------------
ALTER TABLE "public"."mnt_deploy_history" ADD CONSTRAINT "mnt_deploy_history_pkey" PRIMARY KEY ("history_id");

-- ----------------------------
-- Indexes structure for table mnt_deploy_server
-- ----------------------------
CREATE INDEX "idx_deploy_id" ON "public"."mnt_deploy_server" USING btree (
  "deploy_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
CREATE INDEX "idx_server_id" ON "public"."mnt_deploy_server" USING btree (
  "server_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table mnt_deploy_server
-- ----------------------------
ALTER TABLE "public"."mnt_deploy_server" ADD CONSTRAINT "mnt_deploy_server_pkey" PRIMARY KEY ("deploy_id", "server_id");

-- ----------------------------
-- Indexes structure for table mnt_server
-- ----------------------------
CREATE INDEX "idx_ip" ON "public"."mnt_server" USING btree (
  "ip" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table mnt_server
-- ----------------------------
ALTER TABLE "public"."mnt_server" ADD CONSTRAINT "mnt_server_pkey" PRIMARY KEY ("server_id");

-- ----------------------------
-- Indexes structure for table qrtz_blob_triggers
-- ----------------------------
CREATE INDEX "sched_name" ON "public"."qrtz_blob_triggers" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "trigger_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "trigger_group" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table qrtz_blob_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_blob_triggers" ADD CONSTRAINT "qrtz_blob_triggers_pkey" PRIMARY KEY ("sched_name", "trigger_name", "trigger_group");

-- ----------------------------
-- Primary Key structure for table qrtz_calendars
-- ----------------------------
ALTER TABLE "public"."qrtz_calendars" ADD CONSTRAINT "qrtz_calendars_pkey" PRIMARY KEY ("sched_name", "calendar_name");

-- ----------------------------
-- Primary Key structure for table qrtz_cron_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_cron_triggers" ADD CONSTRAINT "qrtz_cron_triggers_pkey" PRIMARY KEY ("sched_name", "trigger_name", "trigger_group");

-- ----------------------------
-- Indexes structure for table qrtz_fired_triggers
-- ----------------------------
CREATE INDEX "idx_qrtz_ft_inst_job_req_rcvry" ON "public"."qrtz_fired_triggers" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "instance_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "requests_recovery" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_qrtz_ft_j_g" ON "public"."qrtz_fired_triggers" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "job_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "job_group" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_qrtz_ft_jg" ON "public"."qrtz_fired_triggers" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "job_group" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_qrtz_ft_t_g" ON "public"."qrtz_fired_triggers" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "trigger_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "trigger_group" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_qrtz_ft_tg" ON "public"."qrtz_fired_triggers" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "trigger_group" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_qrtz_ft_trig_inst_name" ON "public"."qrtz_fired_triggers" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "instance_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table qrtz_fired_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_fired_triggers" ADD CONSTRAINT "qrtz_fired_triggers_pkey" PRIMARY KEY ("sched_name", "entry_id");

-- ----------------------------
-- Indexes structure for table qrtz_job_details
-- ----------------------------
CREATE INDEX "idx_qrtz_j_grp" ON "public"."qrtz_job_details" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "job_group" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_qrtz_j_req_recovery" ON "public"."qrtz_job_details" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "requests_recovery" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table qrtz_job_details
-- ----------------------------
ALTER TABLE "public"."qrtz_job_details" ADD CONSTRAINT "qrtz_job_details_pkey" PRIMARY KEY ("sched_name", "job_name", "job_group");

-- ----------------------------
-- Primary Key structure for table qrtz_locks
-- ----------------------------
ALTER TABLE "public"."qrtz_locks" ADD CONSTRAINT "qrtz_locks_pkey" PRIMARY KEY ("sched_name", "lock_name");

-- ----------------------------
-- Primary Key structure for table qrtz_paused_trigger_grps
-- ----------------------------
ALTER TABLE "public"."qrtz_paused_trigger_grps" ADD CONSTRAINT "qrtz_paused_trigger_grps_pkey" PRIMARY KEY ("sched_name", "trigger_group");

-- ----------------------------
-- Primary Key structure for table qrtz_scheduler_state
-- ----------------------------
ALTER TABLE "public"."qrtz_scheduler_state" ADD CONSTRAINT "qrtz_scheduler_state_pkey" PRIMARY KEY ("sched_name", "instance_name");

-- ----------------------------
-- Primary Key structure for table qrtz_simple_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_simple_triggers" ADD CONSTRAINT "qrtz_simple_triggers_pkey" PRIMARY KEY ("sched_name", "trigger_name", "trigger_group");

-- ----------------------------
-- Primary Key structure for table qrtz_simprop_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_simprop_triggers" ADD CONSTRAINT "qrtz_simprop_triggers_pkey" PRIMARY KEY ("sched_name", "trigger_name", "trigger_group");

-- ----------------------------
-- Indexes structure for table qrtz_triggers
-- ----------------------------
CREATE INDEX "idx_qrtz_t_c" ON "public"."qrtz_triggers" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "calendar_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_qrtz_t_g" ON "public"."qrtz_triggers" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "trigger_group" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_qrtz_t_j" ON "public"."qrtz_triggers" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "job_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "job_group" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_qrtz_t_jg" ON "public"."qrtz_triggers" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "job_group" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_qrtz_t_n_g_state" ON "public"."qrtz_triggers" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "trigger_group" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "trigger_state" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_qrtz_t_n_state" ON "public"."qrtz_triggers" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "trigger_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "trigger_group" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "trigger_state" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_qrtz_t_next_fire_time" ON "public"."qrtz_triggers" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "next_fire_time" "pg_catalog"."int8_ops" ASC NULLS LAST
);
CREATE INDEX "idx_qrtz_t_nft_misfire" ON "public"."qrtz_triggers" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "misfire_instr" "pg_catalog"."int2_ops" ASC NULLS LAST,
  "next_fire_time" "pg_catalog"."int8_ops" ASC NULLS LAST
);
CREATE INDEX "idx_qrtz_t_nft_st" ON "public"."qrtz_triggers" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "trigger_state" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "next_fire_time" "pg_catalog"."int8_ops" ASC NULLS LAST
);
CREATE INDEX "idx_qrtz_t_nft_st_misfire" ON "public"."qrtz_triggers" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "misfire_instr" "pg_catalog"."int2_ops" ASC NULLS LAST,
  "next_fire_time" "pg_catalog"."int8_ops" ASC NULLS LAST,
  "trigger_state" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_qrtz_t_nft_st_misfire_grp" ON "public"."qrtz_triggers" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "misfire_instr" "pg_catalog"."int2_ops" ASC NULLS LAST,
  "next_fire_time" "pg_catalog"."int8_ops" ASC NULLS LAST,
  "trigger_group" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "trigger_state" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_qrtz_t_state" ON "public"."qrtz_triggers" USING btree (
  "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "trigger_state" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table qrtz_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_triggers" ADD CONSTRAINT "qrtz_triggers_pkey" PRIMARY KEY ("sched_name", "trigger_name", "trigger_group");

-- ----------------------------
-- Indexes structure for table sys_dept
-- ----------------------------
CREATE INDEX "idx_enabled" ON "public"."sys_dept" USING btree (
  "enabled" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_pid" ON "public"."sys_dept" USING btree (
  "pid" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_dept
-- ----------------------------
ALTER TABLE "public"."sys_dept" ADD CONSTRAINT "sys_dept_pkey" PRIMARY KEY ("dept_id");

-- ----------------------------
-- Primary Key structure for table sys_dict
-- ----------------------------
ALTER TABLE "public"."sys_dict" ADD CONSTRAINT "sys_dict_pkey" PRIMARY KEY ("dict_id");

-- ----------------------------
-- Indexes structure for table sys_dict_detail
-- ----------------------------
CREATE INDEX "idx_dict_id" ON "public"."sys_dict_detail" USING btree (
  "dict_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_dict_detail
-- ----------------------------
ALTER TABLE "public"."sys_dict_detail" ADD CONSTRAINT "sys_dict_detail_pkey" PRIMARY KEY ("detail_id");

-- ----------------------------
-- Indexes structure for table sys_job
-- ----------------------------
CREATE UNIQUE INDEX "uniq_name" ON "public"."sys_job" USING btree (
  "name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_job
-- ----------------------------
ALTER TABLE "public"."sys_job" ADD CONSTRAINT "sys_job_pkey" PRIMARY KEY ("job_id");

-- ----------------------------
-- Indexes structure for table sys_log
-- ----------------------------
CREATE INDEX "idx_create_time_index" ON "public"."sys_log" USING btree (
  "create_time" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);
CREATE INDEX "idx_log_type" ON "public"."sys_log" USING btree (
  "log_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_log
-- ----------------------------
ALTER TABLE "public"."sys_log" ADD CONSTRAINT "sys_log_pkey" PRIMARY KEY ("log_id");

-- ----------------------------
-- Indexes structure for table sys_menu
-- ----------------------------
CREATE UNIQUE INDEX "uniq_title" ON "public"."sys_menu" USING btree (
  "title" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_menu
-- ----------------------------
ALTER TABLE "public"."sys_menu" ADD CONSTRAINT "sys_menu_pkey" PRIMARY KEY ("menu_id");

-- ----------------------------
-- Indexes structure for table sys_quartz_job
-- ----------------------------
CREATE INDEX "idx_is_pause" ON "public"."sys_quartz_job" USING btree (
  "is_pause" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_quartz_job
-- ----------------------------
ALTER TABLE "public"."sys_quartz_job" ADD CONSTRAINT "sys_quartz_job_pkey" PRIMARY KEY ("job_id");

-- ----------------------------
-- Primary Key structure for table sys_quartz_log
-- ----------------------------
ALTER TABLE "public"."sys_quartz_log" ADD CONSTRAINT "sys_quartz_log_pkey" PRIMARY KEY ("log_id");

-- ----------------------------
-- Indexes structure for table sys_role
-- ----------------------------
CREATE INDEX "idx_level" ON "public"."sys_role" USING btree (
  "level" "pg_catalog"."int4_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_role
-- ----------------------------
ALTER TABLE "public"."sys_role" ADD CONSTRAINT "sys_role_pkey" PRIMARY KEY ("role_id");

-- ----------------------------
-- Indexes structure for table sys_roles_depts
-- ----------------------------
CREATE INDEX "idx_dept_id" ON "public"."sys_roles_depts" USING btree (
  "dept_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
CREATE INDEX "idx_role_id" ON "public"."sys_roles_depts" USING btree (
  "role_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_roles_depts
-- ----------------------------
ALTER TABLE "public"."sys_roles_depts" ADD CONSTRAINT "sys_roles_depts_pkey" PRIMARY KEY ("role_id", "dept_id");

-- ----------------------------
-- Indexes structure for table sys_roles_menus
-- ----------------------------
CREATE INDEX "idx_menu_id" ON "public"."sys_roles_menus" USING btree (
  "menu_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_roles_menus
-- ----------------------------
ALTER TABLE "public"."sys_roles_menus" ADD CONSTRAINT "sys_roles_menus_pkey" PRIMARY KEY ("menu_id", "role_id");

-- ----------------------------
-- Indexes structure for table sys_user
-- ----------------------------
CREATE UNIQUE INDEX "uniq_email" ON "public"."sys_user" USING btree (
  "email" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE UNIQUE INDEX "uniq_username" ON "public"."sys_user" USING btree (
  "username" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_user
-- ----------------------------
ALTER TABLE "public"."sys_user" ADD CONSTRAINT "sys_user_pkey" PRIMARY KEY ("user_id");

-- ----------------------------
-- Indexes structure for table sys_users_jobs
-- ----------------------------
CREATE INDEX "idx_job_id" ON "public"."sys_users_jobs" USING btree (
  "job_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
CREATE INDEX "idx_user_id" ON "public"."sys_users_jobs" USING btree (
  "user_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_users_jobs
-- ----------------------------
ALTER TABLE "public"."sys_users_jobs" ADD CONSTRAINT "sys_users_jobs_pkey" PRIMARY KEY ("user_id", "job_id");

-- ----------------------------
-- Primary Key structure for table sys_users_roles
-- ----------------------------
ALTER TABLE "public"."sys_users_roles" ADD CONSTRAINT "sys_users_roles_pkey" PRIMARY KEY ("user_id", "role_id");

-- ----------------------------
-- Primary Key structure for table tool_alipay_config
-- ----------------------------
ALTER TABLE "public"."tool_alipay_config" ADD CONSTRAINT "tool_alipay_config_pkey" PRIMARY KEY ("config_id");

-- ----------------------------
-- Primary Key structure for table tool_email_config
-- ----------------------------
ALTER TABLE "public"."tool_email_config" ADD CONSTRAINT "tool_email_config_pkey" PRIMARY KEY ("config_id");

-- ----------------------------
-- Primary Key structure for table tool_local_storage
-- ----------------------------
ALTER TABLE "public"."tool_local_storage" ADD CONSTRAINT "tool_local_storage_pkey" PRIMARY KEY ("storage_id");

-- ----------------------------
-- Primary Key structure for table tool_s3_storage
-- ----------------------------
ALTER TABLE "public"."tool_s3_storage" ADD CONSTRAINT "tool_s3_storage_pkey" PRIMARY KEY ("storage_id");

-- ----------------------------
-- Foreign Keys structure for table qrtz_blob_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_blob_triggers" ADD CONSTRAINT "qrtz_blob_triggers_ibfk_1" FOREIGN KEY ("sched_name", "trigger_name", "trigger_group") REFERENCES "public"."qrtz_triggers" ("sched_name", "trigger_name", "trigger_group") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table qrtz_cron_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_cron_triggers" ADD CONSTRAINT "qrtz_cron_triggers_ibfk_1" FOREIGN KEY ("sched_name", "trigger_name", "trigger_group") REFERENCES "public"."qrtz_triggers" ("sched_name", "trigger_name", "trigger_group") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table qrtz_simple_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_simple_triggers" ADD CONSTRAINT "qrtz_simple_triggers_ibfk_1" FOREIGN KEY ("sched_name", "trigger_name", "trigger_group") REFERENCES "public"."qrtz_triggers" ("sched_name", "trigger_name", "trigger_group") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table qrtz_simprop_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_simprop_triggers" ADD CONSTRAINT "qrtz_simprop_triggers_ibfk_1" FOREIGN KEY ("sched_name", "trigger_name", "trigger_group") REFERENCES "public"."qrtz_triggers" ("sched_name", "trigger_name", "trigger_group") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table qrtz_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_triggers" ADD CONSTRAINT "qrtz_triggers_ibfk_1" FOREIGN KEY ("sched_name", "job_name", "job_group") REFERENCES "public"."qrtz_job_details" ("sched_name", "job_name", "job_group") ON DELETE NO ACTION ON UPDATE NO ACTION;
