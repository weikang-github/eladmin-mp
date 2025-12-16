package com.workflow.engine.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "workflow_execution_snapshots")
public class WorkflowExecutionSnapshot extends BaseEntity {
    
    @Column(name = "workflow_execution_id", nullable = false)
    private Long workflowExecutionId;
    
    @Column(name = "workflow_definition_id", nullable = false)
    private Long workflowDefinitionId;
    
    @Column(name = "workflow_name", nullable = false, length = 255)
    private String workflowName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private WorkflowExecutionStatus status;
    
    @Column(name = "current_node_id", length = 100)
    private String currentNodeId;
    
    @Column(name = "input_parameters", columnDefinition = "TEXT")
    private String inputParameters;
    
    @Column(name = "context_data", columnDefinition = "TEXT")
    private String contextData;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    // Constructors
    public WorkflowExecutionSnapshot() {}
    
    // Getters and Setters
    public Long getWorkflowExecutionId() {
        return workflowExecutionId;
    }
    
    public void setWorkflowExecutionId(Long workflowExecutionId) {
        this.workflowExecutionId = workflowExecutionId;
    }
    
    public Long getWorkflowDefinitionId() {
        return workflowDefinitionId;
    }
    
    public void setWorkflowDefinitionId(Long workflowDefinitionId) {
        this.workflowDefinitionId = workflowDefinitionId;
    }
    
    public String getWorkflowName() {
        return workflowName;
    }
    
    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }
    
    public WorkflowExecutionStatus getStatus() {
        return status;
    }
    
    public void setStatus(WorkflowExecutionStatus status) {
        this.status = status;
    }
    
    public String getCurrentNodeId() {
        return currentNodeId;
    }
    
    public void setCurrentNodeId(String currentNodeId) {
        this.currentNodeId = currentNodeId;
    }
    
    public String getInputParameters() {
        return inputParameters;
    }
    
    public void setInputParameters(String inputParameters) {
        this.inputParameters = inputParameters;
    }
    
    public String getContextData() {
        return contextData;
    }
    
    public void setContextData(String contextData) {
        this.contextData = contextData;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    // Helper methods for JSON conversion
    public Map<String, Object> getInputParametersAsMap() {
        try {
            return com.fasterxml.jackson.databind.ObjectMapper
                    .builder()
                    .findAndAddModules()
                    .build()
                    .readValue(inputParameters, Map.class);
        } catch (Exception e) {
            return null;
        }
    }
    
    public void setInputParametersFromMap(Map<String, Object> params) {
        try {
            this.inputParameters = com.fasterxml.jackson.databind.ObjectMapper
                    .builder()
                    .findAndAddModules()
                    .build()
                    .writeValueAsString(params);
        } catch (Exception e) {
            this.inputParameters = null;
        }
    }
    
    public Map<String, Object> getContextDataAsMap() {
        try {
            return com.fasterxml.jackson.databind.ObjectMapper
                    .builder()
                    .findAndAddModules()
                    .build()
                    .readValue(contextData, Map.class);
        } catch (Exception e) {
            return null;
        }
    }
    
    public void setContextDataFromMap(Map<String, Object> data) {
        try {
            this.contextData = com.fasterxml.jackson.databind.ObjectMapper
                    .builder()
                    .findAndAddModules()
                    .build()
                    .writeValueAsString(data);
        } catch (Exception e) {
            this.contextData = null;
        }
    }
}