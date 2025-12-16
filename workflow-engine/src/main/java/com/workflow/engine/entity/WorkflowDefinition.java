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

/**
 * 流程定义实体
 * 
 * @author workflow-engine
 * @since 1.0.0
 */
@Entity
@Table(name = "workflow_definitions")
@TableName("workflow_definitions")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class WorkflowDefinition extends BaseEntity {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 流程名称
     */
    @NotBlank(message = "流程名称不能为空")
    @Size(max = 255, message = "流程名称长度不能超过255个字符")
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * 流程描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 版本号
     */
    @NotNull(message = "版本号不能为空")
    @Column(name = "version", nullable = false)
    private Integer version = 1;

    /**
     * 流程定义数据（JSON格式）
     * 包含节点定义、连线定义等
     */
    @NotNull(message = "流程定义数据不能为空")
    @Type(type = "jsonb")
    @Column(name = "definition_data", columnDefinition = "jsonb", nullable = false)
    private JsonNode definitionData;

    /**
     * 状态
     */
    @NotBlank(message = "状态不能为空")
    @Size(max = 50, message = "状态长度不能超过50个字符")
    @Column(name = "status", nullable = false, length = 50)
    private String status = "ACTIVE";

    // 状态枚举
    public enum Status {
        ACTIVE("活跃"),
        INACTIVE("非活跃");

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public JsonNode getDefinitionData() {
        return definitionData;
    }

    public void setDefinitionData(JsonNode definitionData) {
        this.definitionData = definitionData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "WorkflowDefinition{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", version=" + version +
                ", status='" + status + '\'' +
                ", createdAt=" + getCreatedAt() +
                '}';
    }
}