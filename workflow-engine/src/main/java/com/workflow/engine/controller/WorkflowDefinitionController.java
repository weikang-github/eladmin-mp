package com.workflow.engine.controller;

import com.workflow.engine.dto.WorkflowDefinitionDTO;
import com.workflow.engine.entity.WorkflowDefinition;
import com.workflow.engine.entity.WorkflowDefinitionStatus;
import com.workflow.engine.service.WorkflowDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workflow-definitions")
public class WorkflowDefinitionController {
    
    @Autowired
    private WorkflowDefinitionService workflowDefinitionService;
    
    /**
     * 创建流程定义
     */
    @PostMapping
    public ResponseEntity<WorkflowDefinition> createWorkflowDefinition(@Valid @RequestBody WorkflowDefinitionDTO dto) {
        WorkflowDefinition definition = workflowDefinitionService.createWorkflowDefinition(dto);
        return ResponseEntity.ok(definition);
    }
    
    /**
     * 更新流程定义
     */
    @PutMapping("/{id}")
    public ResponseEntity<WorkflowDefinition> updateWorkflowDefinition(
            @PathVariable Long id,
            @Valid @RequestBody WorkflowDefinitionDTO dto) {
        WorkflowDefinition definition = workflowDefinitionService.updateWorkflowDefinition(id, dto);
        return ResponseEntity.ok(definition);
    }
    
    /**
     * 删除流程定义
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkflowDefinition(@PathVariable Long id) {
        workflowDefinitionService.deleteWorkflowDefinition(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * 获取流程定义
     */
    @GetMapping("/{id}")
    public ResponseEntity<WorkflowDefinition> getWorkflowDefinition(@PathVariable Long id) {
        WorkflowDefinition definition = workflowDefinitionService.getWorkflowDefinition(id);
        return ResponseEntity.ok(definition);
    }
    
    /**
     * 根据名称获取最新版本的流程定义
     */
    @GetMapping("/by-name/{name}")
    public ResponseEntity<WorkflowDefinition> getLatestWorkflowDefinition(@PathVariable String name) {
        WorkflowDefinition definition = workflowDefinitionService.getLatestWorkflowDefinition(name);
        return ResponseEntity.ok(definition);
    }
    
    /**
     * 分页查询流程定义
     */
    @GetMapping
    public ResponseEntity<Page<WorkflowDefinition>> listWorkflowDefinitions(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        Page<WorkflowDefinition> page = workflowDefinitionService.listWorkflowDefinitions(name, status, pageable);
        return ResponseEntity.ok(page);
    }
    
    /**
     * 获取流程定义的所有版本
     */
    @GetMapping("/versions/{name}")
    public ResponseEntity<List<WorkflowDefinition>> getWorkflowDefinitionVersions(@PathVariable String name) {
        List<WorkflowDefinition> versions = workflowDefinitionService.getWorkflowDefinitionVersions(name);
        return ResponseEntity.ok(versions);
    }
    
    /**
     * 激活流程定义版本
     */
    @PutMapping("/{id}/activate")
    public ResponseEntity<WorkflowDefinition> activateWorkflowDefinitionVersion(@PathVariable Long id) {
        WorkflowDefinition definition = workflowDefinitionService.activateWorkflowDefinitionVersion(id);
        return ResponseEntity.ok(definition);
    }
    
    /**
     * 测试流程定义
     */
    @PostMapping("/test")
    public ResponseEntity<Map<String, Object>> testWorkflowDefinition(@Valid @RequestBody WorkflowDefinitionDTO dto) {
        Map<String, Object> result = workflowDefinitionService.testWorkflowDefinition(dto);
        return ResponseEntity.ok(result);
    }
}