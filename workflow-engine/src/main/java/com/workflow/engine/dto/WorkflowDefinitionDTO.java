package com.workflow.engine.dto;

import com.fasterxml.jackson.databind.JsonNode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 流程定义数据传输对象
 * 
 * @author workflow-engine
 * @since 1.0.0
 */
public class WorkflowDefinitionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "流程名称不能为空")
    @Size(max = 255, message = "流程名称长度不能超过255个字符")
    private String name;

    private String description;

    @NotNull(message = "版本号不能为空")
    private Integer version = 1;

    @NotNull(message = "流程定义数据不能为空")
    private WorkflowDefinitionData definitionData;

    private String status = "ACTIVE";

    private String createdBy;

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

    public WorkflowDefinitionData getDefinitionData() {
        return definitionData;
    }

    public void setDefinitionData(WorkflowDefinitionData definitionData) {
        this.definitionData = definitionData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * 流程定义数据结构
     */
    public static class WorkflowDefinitionData implements Serializable {
        private static final long serialVersionUID = 1L;

        private List<Node> nodes;
        private List<Edge> edges;
        private Map<String, Object> properties;

        // Getters and Setters
        public List<Node> getNodes() {
            return nodes;
        }

        public void setNodes(List<Node> nodes) {
            this.nodes = nodes;
        }

        public List<Edge> getEdges() {
            return edges;
        }

        public void setEdges(List<Edge> edges) {
            this.edges = edges;
        }

        public Map<String, Object> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }
    }

    /**
     * 节点定义
     */
    public static class Node implements Serializable {
        private static final long serialVersionUID = 1L;

        @NotBlank(message = "节点ID不能为空")
        private String id;

        @NotBlank(message = "节点类型不能为空")
        private String type; // START, END, TASK, CONDITION, PARALLEL_GATEWAY, SUB_WORKFLOW

        @NotBlank(message = "节点名称不能为空")
        private String name;

        private Integer x;
        private Integer y;
        private Integer width;
        private Integer height;
        private Map<String, Object> properties; // 节点特定配置

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getX() {
            return x;
        }

        public void setX(Integer x) {
            this.x = x;
        }

        public Integer getY() {
            return y;
        }

        public void setY(Integer y) {
            this.y = y;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public Map<String, Object> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }
    }

    /**
     * 连线定义
     */
    public static class Edge implements Serializable {
        private static final long serialVersionUID = 1L;

        @NotBlank(message = "连线ID不能为空")
        private String id;

        @NotBlank(message = "源节点ID不能为空")
        private String source;

        @NotBlank(message = "目标节点ID不能为空")
        private String target;

        private String sourceAnchor;
        private String targetAnchor;
        private Map<String, Object> properties; // 连线特定配置，如条件表达式

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getSourceAnchor() {
            return sourceAnchor;
        }

        public void setSourceAnchor(String sourceAnchor) {
            this.sourceAnchor = sourceAnchor;
        }

        public String getTargetAnchor() {
            return targetAnchor;
        }

        public void setTargetAnchor(String targetAnchor) {
            this.targetAnchor = targetAnchor;
        }

        public Map<String, Object> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }
    }
}