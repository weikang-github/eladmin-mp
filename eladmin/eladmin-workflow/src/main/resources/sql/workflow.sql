-- 流程定义表
CREATE TABLE IF NOT EXISTS workflow_definition (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    definition_json JSONB NOT NULL,
    version INTEGER DEFAULT 1,
    is_latest BOOLEAN DEFAULT TRUE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 流程执行表
CREATE TABLE IF NOT EXISTS workflow_execution (
    id BIGSERIAL PRIMARY KEY,
    workflow_id BIGINT NOT NULL,
    workflow_version INTEGER NOT NULL,
    status VARCHAR(50) NOT NULL,
    input_params JSONB,
    output_result JSONB,
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_time TIMESTAMP,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (workflow_id) REFERENCES workflow_definition(id)
);

-- 任务执行表
CREATE TABLE IF NOT EXISTS task_execution (
    id BIGSERIAL PRIMARY KEY,
    execution_id BIGINT NOT NULL,
    task_id VARCHAR(255) NOT NULL,
    task_type VARCHAR(255) NOT NULL,
    task_config JSONB NOT NULL,
    status VARCHAR(50) NOT NULL,
    input_data JSONB,
    output_data JSONB,
    error_message TEXT,
    retry_count INTEGER DEFAULT 0,
    max_retries INTEGER DEFAULT 0,
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_time TIMESTAMP,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (execution_id) REFERENCES workflow_execution(id)
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_workflow_definition_name ON workflow_definition(name);
CREATE INDEX IF NOT EXISTS idx_workflow_definition_latest ON workflow_definition(is_latest);
CREATE INDEX IF NOT EXISTS idx_workflow_execution_workflow ON workflow_execution(workflow_id);
CREATE INDEX IF NOT EXISTS idx_workflow_execution_status ON workflow_execution(status);
CREATE INDEX IF NOT EXISTS idx_task_execution_execution ON task_execution(execution_id);
CREATE INDEX IF NOT EXISTS idx_task_execution_status ON task_execution(status);
