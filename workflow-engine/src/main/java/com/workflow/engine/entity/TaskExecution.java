package com.workflow.engine.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 任务执行记录实体
 * 
 * @author workflow-engine
 * @since 1.0.0
 */
@Entity
@Table(name = "task_executions")
@TableName("task_executions")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class TaskExecution extends BaseEntity {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 流程执行实例ID
     */
    @NotNull(message = "流程执行实例ID不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_execution_id", nullable = false)
    private WorkflowExecution workflowExecution;

    /**
     * 任务节点ID
     */
    @NotBlank(message = "任务节点ID不能为空")
    @Size(max = 100, message = "任务节点ID长度不能超过100个字符")
    @Column(name = "task_id", nullable = false, length = 100)
    private String taskId;

    /**
     * 任务名称
     */
    @NotBlank(message = "任务名称不能为空")
    @Size(max = 255, message = "任务名称长度不能超过255个字符")
    @Column(name = "task_name", nullable = false, length = 255)
    private String taskName;

    /**
     * 任务类型
     */
    @NotBlank(message = "任务类型不能为空")
    @Size(max = 100, message = "任务类型长度不能超过100个字符")
    @Column(name = "task_type", nullable = false, length = 100)
    private String taskType;

    /**
     * 任务配置参数
     */
    @NotNull(message = "任务配置参数不能为空")
    @Type(type = "jsonb")
    @Column(name = "task_config", columnDefinition = "jsonb", nullable = false)
    private JsonNode taskConfig;

    /**
     * 状态
     */
    @NotBlank(message = "状态不能为空")
    @Size(max = 50, message = "状态长度不能超过50个字符")
    @Column(name = "status", nullable = false, length = 50)
    private String status = "PENDING";

    /**
     * 任务输入参数
     */
    @Type(type = "jsonb")
    @Column(name = "input_data", columnDefinition = "jsonb")
    private JsonNode inputData;

    /**
     * 任务输出结果
     */
    @Type(type = "jsonb")
    @Column(name = "output_data", columnDefinition = "jsonb")
    private JsonNode outputData;

    /**
     * 错误信息
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    /**
     * 重试次数
     */
    @Column(name = "retry_count", nullable = false)
    private Integer retryCount = 0;

    /**
     * 最大重试次数
     */
    @Column(name = "max_retries", nullable = false)
    private Integer maxRetries = 3;

    /**
     * 超时时间（秒）
     */
    @Column(name = "timeout_seconds")
    private Integer timeoutSeconds;

    /**
     * 开始时间
     */
    @Column(name = "started_at")
    private LocalDateTime startedAt;

    /**
     * 完成时间
     */
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // 任务类型枚举
    public enum TaskType {
        HTTP("HTTP请求"),
        SQL("SQL查询"),
        SCRIPT("脚本执行"),
        CONDITION("条件分支"),
        PARALLEL_GATEWAY("并行网关"),
        SUB_WORKFLOW("子流程");

        private final String description;

        TaskType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // 状态枚举
    public enum Status {
        PENDING("待执行"),
        RUNNING("运行中"),
        COMPLETED("已完成"),
        FAILED("失败"),
        SKIPPED("已跳过"),
        TIMEOUT("超时");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkflowExecution getWorkflowExecution() {
        return workflowExecution;
    }

    public void setWorkflowExecution(WorkflowExecution workflowExecution) {
        this.workflowExecution = workflowExecution;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public JsonNode getTaskConfig() {
        return taskConfig;
    }

    public void setTaskConfig(JsonNode taskConfig) {
        this.taskConfig = taskConfig;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JsonNode getInputData() {
        return inputData;
    }

    public void setInputData(JsonNode inputData) {
        this.inputData = inputData;
    }

    public JsonNode getOutputData() {
        return outputData;
    }

    public void setOutputData(JsonNode outputData) {
        this.outputData = outputData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public Integer getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(Integer timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    /**
     * 获取执行耗时（毫秒）
     */
    public Long getDuration() {
        if (startedAt == null || completedAt == null) {
            return null;
        }
        return java.time.Duration.between(startedAt, completedAt).toMillis();
    }

    /**
     * 是否可以重试
     */
    public boolean canRetry() {
        return retryCount < maxRetries;
    }

    /**
     * 增加重试次数
     */
    public void incrementRetryCount() {
        this.retryCount++;
    }

    @Override
    public String toString() {
        return "TaskExecution{" +
                "id=" + id +
                ", taskId='" + taskId + '\'' +
                ", taskName='" + taskName + '\'' +
                ", taskType='" + taskType + '\'' +
                ", status='" + status + '\'' +
                ", retryCount=" + retryCount +
                ", startedAt=" + startedAt +
                ", completedAt=" + completedAt +
                ", createdAt=" + getCreatedAt() +
                '}';
    }
}