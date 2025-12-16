package com.workflow.engine.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 任务依赖关系实体
 * 
 * @author workflow-engine
 * @since 1.0.0
 */
@Entity
@Table(name = "task_dependencies")
@TableName("task_dependencies")
public class TaskDependency extends BaseEntity {

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
     * 源任务节点ID
     */
    @NotBlank(message = "源任务节点ID不能为空")
    @Size(max = 100, message = "源任务节点ID长度不能超过100个字符")
    @Column(name = "from_task_id", nullable = false, length = 100)
    private String fromTaskId;

    /**
     * 目标任务节点ID
     */
    @NotBlank(message = "目标任务节点ID不能为空")
    @Size(max = 100, message = "目标任务节点ID长度不能超过100个字符")
    @Column(name = "to_task_id", nullable = false, length = 100)
    private String toTaskId;

    /**
     * 依赖类型
     */
    @NotBlank(message = "依赖类型不能为空")
    @Size(max = 50, message = "依赖类型长度不能超过50个字符")
    @Column(name = "dependency_type", nullable = false, length = 50)
    private String dependencyType = "SEQUENTIAL";

    /**
     * 条件表达式（条件分支时使用）
     */
    @Column(name = "condition_expression", columnDefinition = "TEXT")
    private String conditionExpression;

    // 依赖类型枚举
    public enum DependencyType {
        SEQUENTIAL("串行"),
        CONDITIONAL("条件"),
        PARALLEL("并行");

        private final String description;

        DependencyType(String description) {
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

    public String getFromTaskId() {
        return fromTaskId;
    }

    public void setFromTaskId(String fromTaskId) {
        this.fromTaskId = fromTaskId;
    }

    public String getToTaskId() {
        return toTaskId;
    }

    public void setToTaskId(String toTaskId) {
        this.toTaskId = toTaskId;
    }

    public String getDependencyType() {
        return dependencyType;
    }

    public void setDependencyType(String dependencyType) {
        this.dependencyType = dependencyType;
    }

    public String getConditionExpression() {
        return conditionExpression;
    }

    public void setConditionExpression(String conditionExpression) {
        this.conditionExpression = conditionExpression;
    }

    @Override
    public String toString() {
        return "TaskDependency{" +
                "id=" + id +
                ", fromTaskId='" + fromTaskId + '\'' +
                ", toTaskId='" + toTaskId + '\'' +
                ", dependencyType='" + dependencyType + '\'' +
                ", conditionExpression='" + conditionExpression + '\'' +
                ", createdAt=" + getCreatedAt() +
                '}';
    }
}