-- 工作流引擎数据库表结构
-- PostgreSQL 版本

-- 流程定义表
CREATE TABLE IF NOT EXISTS workflow_definitions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL COMMENT '流程名称',
    description TEXT COMMENT '流程描述',
    version INTEGER NOT NULL DEFAULT 1 COMMENT '版本号',
    definition_data JSONB NOT NULL COMMENT '流程定义数据(JSON格式)',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE, INACTIVE',
    created_by VARCHAR(100) NOT NULL COMMENT '创建人',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(100) COMMENT '更新人',
    updated_at TIMESTAMP COMMENT '更新时间',
    deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT '逻辑删除标记'
);

-- 流程定义索引
CREATE INDEX IF NOT EXISTS idx_workflow_definitions_name ON workflow_definitions(name);
CREATE INDEX IF NOT EXISTS idx_workflow_definitions_status ON workflow_definitions(status);
CREATE INDEX IF NOT EXISTS idx_workflow_definitions_created_at ON workflow_definitions(created_at);

-- 流程执行实例表
CREATE TABLE IF NOT EXISTS workflow_executions (
    id BIGSERIAL PRIMARY KEY,
    workflow_definition_id BIGINT NOT NULL REFERENCES workflow_definitions(id) COMMENT '流程定义ID',
    execution_name VARCHAR(255) NOT NULL COMMENT '执行实例名称',
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING, RUNNING, COMPLETED, FAILED, CANCELLED, SUSPENDED',
    input_data JSONB COMMENT '输入参数',
    output_data JSONB COMMENT '输出结果',
    error_message TEXT COMMENT '错误信息',
    started_at TIMESTAMP COMMENT '开始时间',
    completed_at TIMESTAMP COMMENT '完成时间',
    created_by VARCHAR(100) NOT NULL COMMENT '创建人',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP COMMENT '更新时间',
    deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT '逻辑删除标记'
);

-- 流程执行实例索引
CREATE INDEX IF NOT EXISTS idx_workflow_executions_workflow_id ON workflow_executions(workflow_definition_id);
CREATE INDEX IF NOT EXISTS idx_workflow_executions_status ON workflow_executions(status);
CREATE INDEX IF NOT EXISTS idx_workflow_executions_created_at ON workflow_executions(created_at);
CREATE INDEX IF NOT EXISTS idx_workflow_executions_created_by ON workflow_executions(created_by);

-- 任务执行记录表
CREATE TABLE IF NOT EXISTS task_executions (
    id BIGSERIAL PRIMARY KEY,
    workflow_execution_id BIGINT NOT NULL REFERENCES workflow_executions(id) ON DELETE CASCADE COMMENT '流程执行实例ID',
    task_id VARCHAR(100) NOT NULL COMMENT '任务节点ID',
    task_name VARCHAR(255) NOT NULL COMMENT '任务名称',
    task_type VARCHAR(100) NOT NULL COMMENT '任务类型: HTTP, SQL, SCRIPT, CONDITION, PARALLEL_GATEWAY, SUB_WORKFLOW',
    task_config JSONB NOT NULL COMMENT '任务配置参数',
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING, RUNNING, COMPLETED, FAILED, SKIPPED, TIMEOUT',
    input_data JSONB COMMENT '任务输入参数',
    output_data JSONB COMMENT '任务输出结果',
    error_message TEXT COMMENT '错误信息',
    retry_count INTEGER NOT NULL DEFAULT 0 COMMENT '重试次数',
    max_retries INTEGER NOT NULL DEFAULT 3 COMMENT '最大重试次数',
    timeout_seconds INTEGER COMMENT '超时时间(秒)',
    started_at TIMESTAMP COMMENT '开始时间',
    completed_at TIMESTAMP COMMENT '完成时间',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP COMMENT '更新时间',
    deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT '逻辑删除标记'
);

-- 任务执行记录索引
CREATE INDEX IF NOT EXISTS idx_task_executions_workflow_execution_id ON task_executions(workflow_execution_id);
CREATE INDEX IF NOT EXISTS idx_task_executions_task_id ON task_executions(task_id);
CREATE INDEX IF NOT EXISTS idx_task_executions_status ON task_executions(status);
CREATE INDEX IF NOT EXISTS idx_task_executions_task_type ON task_executions(task_type);
CREATE INDEX IF NOT EXISTS idx_task_executions_created_at ON task_executions(created_at);

-- 任务依赖关系表（用于记录任务间的依赖关系）
CREATE TABLE IF NOT EXISTS task_dependencies (
    id BIGSERIAL PRIMARY KEY,
    workflow_execution_id BIGINT NOT NULL REFERENCES workflow_executions(id) ON DELETE CASCADE COMMENT '流程执行实例ID',
    from_task_id VARCHAR(100) NOT NULL COMMENT '源任务节点ID',
    to_task_id VARCHAR(100) NOT NULL COMMENT '目标任务节点ID',
    dependency_type VARCHAR(50) NOT NULL DEFAULT 'SEQUENTIAL' COMMENT '依赖类型: SEQUENTIAL, CONDITIONAL, PARALLEL',
    condition_expression TEXT COMMENT '条件表达式（条件分支时使用）',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT '逻辑删除标记',
    UNIQUE(workflow_execution_id, from_task_id, to_task_id)
);

-- 任务依赖关系索引
CREATE INDEX IF NOT EXISTS idx_task_dependencies_workflow_execution_id ON task_dependencies(workflow_execution_id);
CREATE INDEX IF NOT EXISTS idx_task_dependencies_from_task ON task_dependencies(from_task_id);
CREATE INDEX IF NOT EXISTS idx_task_dependencies_to_task ON task_dependencies(to_task_id);

-- 流程执行日志表（用于审计和调试）
CREATE TABLE IF NOT EXISTS execution_logs (
    id BIGSERIAL PRIMARY KEY,
    workflow_execution_id BIGINT NOT NULL REFERENCES workflow_executions(id) ON DELETE CASCADE COMMENT '流程执行实例ID',
    task_execution_id BIGINT REFERENCES task_executions(id) ON DELETE CASCADE COMMENT '任务执行记录ID',
    log_level VARCHAR(20) NOT NULL COMMENT '日志级别: DEBUG, INFO, WARN, ERROR',
    message TEXT NOT NULL COMMENT '日志消息',
    context_data JSONB COMMENT '上下文数据',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

-- 流程执行日志索引
CREATE INDEX IF NOT EXISTS idx_execution_logs_workflow_execution_id ON execution_logs(workflow_execution_id);
CREATE INDEX IF NOT EXISTS idx_execution_logs_task_execution_id ON execution_logs(task_execution_id);
CREATE INDEX IF NOT EXISTS idx_execution_logs_level ON execution_logs(log_level);
CREATE INDEX IF NOT EXISTS idx_execution_logs_created_at ON execution_logs(created_at);

-- 流程版本历史表（用于版本管理）
CREATE TABLE IF NOT EXISTS workflow_versions (
    id BIGSERIAL PRIMARY KEY,
    workflow_definition_id BIGINT NOT NULL REFERENCES workflow_definitions(id) COMMENT '流程定义ID',
    version INTEGER NOT NULL COMMENT '版本号',
    definition_data JSONB NOT NULL COMMENT '流程定义数据',
    change_description TEXT COMMENT '变更描述',
    created_by VARCHAR(100) NOT NULL COMMENT '创建人',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE(workflow_definition_id, version)
);

-- 流程版本历史索引
CREATE INDEX IF NOT EXISTS idx_workflow_versions_workflow_id ON workflow_versions(workflow_definition_id);
CREATE INDEX IF NOT EXISTS idx_workflow_versions_created_at ON workflow_versions(created_at);

-- 流程执行快照表（用于状态持久化和断点续跑）
CREATE TABLE IF NOT EXISTS workflow_execution_snapshots (
    id BIGSERIAL PRIMARY KEY,
    workflow_execution_id BIGINT NOT NULL REFERENCES workflow_executions(id) ON DELETE CASCADE COMMENT '流程执行实例ID',
    workflow_definition_id BIGINT NOT NULL REFERENCES workflow_definitions(id) COMMENT '流程定义ID',
    workflow_name VARCHAR(255) NOT NULL COMMENT '流程名称',
    status VARCHAR(50) NOT NULL COMMENT '快照状态: PENDING, RUNNING, COMPLETED, FAILED',
    current_node_id VARCHAR(100) COMMENT '当前节点ID',
    input_parameters JSONB COMMENT '输入参数',
    context_data JSONB COMMENT '上下文数据',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT '逻辑删除标记'
);

-- 流程执行快照索引
CREATE INDEX IF NOT EXISTS idx_workflow_execution_snapshots_workflow_execution_id ON workflow_execution_snapshots(workflow_execution_id);
CREATE INDEX IF NOT EXISTS idx_workflow_execution_snapshots_workflow_definition_id ON workflow_execution_snapshots(workflow_definition_id);
CREATE INDEX IF NOT EXISTS idx_workflow_execution_snapshots_created_at ON workflow_execution_snapshots(created_at);
CREATE INDEX IF NOT EXISTS idx_workflow_execution_snapshots_status ON workflow_execution_snapshots(status);

-- 添加表注释
COMMENT ON TABLE workflow_definitions IS '流程定义表';
COMMENT ON TABLE workflow_executions IS '流程执行实例表';
COMMENT ON TABLE task_executions IS '任务执行记录表';
COMMENT ON TABLE task_dependencies IS '任务依赖关系表';
COMMENT ON TABLE execution_logs IS '流程执行日志表';
COMMENT ON TABLE workflow_versions IS '流程版本历史表';
COMMENT ON TABLE workflow_execution_snapshots IS '流程执行快照表';