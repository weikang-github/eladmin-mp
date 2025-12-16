package com.workflow.engine.controller;

import com.workflow.engine.entity.*;
import com.workflow.engine.service.WorkflowExecutionEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workflow-executions")
public class WorkflowExecutionController {
    
    @Autowired
    private WorkflowExecutionEngine workflowExecutionEngine;
    
    /**
     * 启动流程执行
     */
    @PostMapping("/start")
    public ResponseEntity<WorkflowExecution> startWorkflowExecution(
            @Valid @RequestBody StartWorkflowRequest request) {
        WorkflowExecution execution = workflowExecutionEngine.startWorkflowExecution(
                request.getWorkflowDefinitionId(),
                request.getInputParameters(),
                request.getStartedBy()
        );
        return ResponseEntity.ok(execution);
    }
    
    /**
     * 获取流程执行状态
     */
    @GetMapping("/{executionId}")
    public ResponseEntity<WorkflowExecution> getWorkflowExecution(@PathVariable Long executionId) {
        WorkflowExecution execution = workflowExecutionEngine.getWorkflowExecution(executionId);
        return ResponseEntity.ok(execution);
    }
    
    /**
     * 获取任务执行列表
     */
    @GetMapping("/{executionId}/tasks")
    public ResponseEntity<List<TaskExecution>> getTaskExecutions(@PathVariable Long executionId) {
        List<TaskExecution> tasks = workflowExecutionEngine.getTaskExecutions(executionId);
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * 获取执行日志
     */
    @GetMapping("/{executionId}/logs")
    public ResponseEntity<List<ExecutionLog>> getExecutionLogs(@PathVariable Long executionId) {
        List<ExecutionLog> logs = workflowExecutionEngine.getExecutionLogs(executionId);
        return ResponseEntity.ok(logs);
    }
    
    /**
     * 根据流程定义ID查询执行记录
     */
    @GetMapping
    public ResponseEntity<Page<WorkflowExecution>> listWorkflowExecutions(
            @RequestParam(required = false) Long workflowDefinitionId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startedBy,
            Pageable pageable) {
        
        // 这里需要根据参数构建查询条件
        // 简化处理，实际应该使用Specification或QueryDSL
        Page<WorkflowExecution> page = Page.empty(pageable); // TODO: 实现查询逻辑
        return ResponseEntity.ok(page);
    }
    
    /**
     * 启动流程请求DTO
     */
    public static class StartWorkflowRequest {
        @NotNull(message = "流程定义ID不能为空")
        private Long workflowDefinitionId;
        
        private Map<String, Object> inputParameters;
        
        @NotBlank(message = "启动人不能为空")
        private String startedBy;
        
        public Long getWorkflowDefinitionId() {
            return workflowDefinitionId;
        }
        
        public void setWorkflowDefinitionId(Long workflowDefinitionId) {
            this.workflowDefinitionId = workflowDefinitionId;
        }
        
        public Map<String, Object> getInputParameters() {
            return inputParameters;
        }
        
        public void setInputParameters(Map<String, Object> inputParameters) {
            this.inputParameters = inputParameters;
        }
        
        public String getStartedBy() {
            return startedBy;
        }
        
        public void setStartedBy(String startedBy) {
            this.startedBy = startedBy;
        }
    }
}