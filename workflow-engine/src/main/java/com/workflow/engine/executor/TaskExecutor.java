package com.workflow.engine.executor;

import com.workflow.engine.dto.DagNode;
import com.workflow.engine.entity.TaskExecution;

/**
 * 任务执行器接口
 * 定义了各种任务类型的执行规范
 * 
 * @author workflow-engine
 * @since 1.0.0
 */
public interface TaskExecutor {

    /**
     * 获取任务类型
     */
    String getTaskType();

    /**
     * 执行任务
     * 
     * @param taskExecution 任务执行记录
     * @param dagNode DAG节点信息
     * @return 任务执行结果
     * @throws Exception 执行异常
     */
    TaskExecutionResult execute(TaskExecution taskExecution, DagNode dagNode) throws Exception;

    /**
     * 验证任务配置
     * 
     * @param dagNode DAG节点信息
     * @throws IllegalArgumentException 配置无效时抛出
     */
    void validateConfig(DagNode dagNode) throws IllegalArgumentException;

    /**
     * 获取任务超时时间（秒）
     * 
     * @param dagNode DAG节点信息
     * @return 超时时间，null表示使用默认超时时间
     */
    Integer getTimeoutSeconds(DagNode dagNode);

    /**
     * 任务执行结果
     */
    class TaskExecutionResult {
        private final boolean success;
        private final Object output;
        private final String errorMessage;

        public TaskExecutionResult(boolean success, Object output, String errorMessage) {
            this.success = success;
            this.output = output;
            this.errorMessage = errorMessage;
        }

        public static TaskExecutionResult success(Object output) {
            return new TaskExecutionResult(true, output, null);
        }

        public static TaskExecutionResult failure(String errorMessage) {
            return new TaskExecutionResult(false, null, errorMessage);
        }

        public boolean isSuccess() {
            return success;
        }

        public Object getOutput() {
            return output;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}