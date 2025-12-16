package com.workflow.engine.dto;

import java.util.Map;

/**
 * DAG节点定义
 * 
 * @author workflow-engine
 * @since 1.0.0
 */
public class DagNode {

    private String id;
    private String type; // START, END, TASK, CONDITION, PARALLEL_GATEWAY, SUB_WORKFLOW
    private String name;
    private Map<String, Object> properties;

    public DagNode() {
    }

    public DagNode(String id, String type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public DagNode(String id, String type, String name, Map<String, Object> properties) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.properties = properties;
    }

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

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    /**
     * 是否是开始节点
     */
    public boolean isStartNode() {
        return "START".equals(type);
    }

    /**
     * 是否是结束节点
     */
    public boolean isEndNode() {
        return "END".equals(type);
    }

    /**
     * 是否是任务节点
     */
    public boolean isTaskNode() {
        return "TASK".equals(type);
    }

    /**
     * 是否是条件节点
     */
    public boolean isConditionNode() {
        return "CONDITION".equals(type);
    }

    /**
     * 是否是并行网关节点
     */
    public boolean isParallelGatewayNode() {
        return "PARALLEL_GATEWAY".equals(type);
    }

    /**
     * 是否是子流程节点
     */
    public boolean isSubWorkflowNode() {
        return "SUB_WORKFLOW".equals(type);
    }

    @Override
    public String toString() {
        return "DagNode{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}