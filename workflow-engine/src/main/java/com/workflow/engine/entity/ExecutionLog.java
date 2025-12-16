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
 * 流程执行日志实体
 * 
 * @author workflow-engine
 * @since 1.0.0
 */
@Entity
@Table(name = "execution_logs")
@TableName("execution_logs")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ExecutionLog extends BaseEntity {

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
     * 任务执行记录ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_execution_id")
    private TaskExecution taskExecution;

    /**
     * 日志级别
     */
    @NotBlank(message = "日志级别不能为空")
    @Size(max = 20, message = "日志级别长度不能超过20个字符")
    @Column(name = "log_level", nullable = false, length = 20)
    private String logLevel;

    /**
     * 日志消息
     */
    @NotBlank(message = "日志消息不能为空")
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    /**
     * 上下文数据
     */
    @Type(type = "jsonb")
    @Column(name = "context_data", columnDefinition = "jsonb")
    private JsonNode contextData;

    // 日志级别枚举
    public enum LogLevel {
        DEBUG("调试"),
        INFO("信息"),
        WARN("警告"),
        ERROR("错误");

        private final String description;

        LogLevel(String description) {
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

    public TaskExecution getTaskExecution() {
        return taskExecution;
    }

    public void setTaskExecution(TaskExecution taskExecution) {
        this.taskExecution = taskExecution;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonNode getContextData() {
        return contextData;
    }

    public void setContextData(JsonNode contextData) {
        this.contextData = contextData;
    }

    @Override
    public String toString() {
        return "ExecutionLog{" +
                "id=" + id +
                ", logLevel='" + logLevel + '\'' +
                ", message='" + message + '\'' +
                ", createdAt=" + getCreatedAt() +
                '}';
    }
}