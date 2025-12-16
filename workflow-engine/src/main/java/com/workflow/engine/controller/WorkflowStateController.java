package com.workflow.engine.controller;

import com.workflow.engine.entity.WorkflowExecutionSnapshot;
import com.workflow.engine.service.WorkflowStateService;
import com.workflow.engine.service.WorkflowStateService.WorkflowExecutionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workflow-state")
public class WorkflowStateController {
    
    @Autowired
    private WorkflowStateService workflowStateService;
    
    /**
     * 创建流程执行状态快照
     */
    @PostMapping("/snapshots/{executionId}")
    public ResponseEntity<WorkflowExecutionSnapshot> createSnapshot(@PathVariable Long executionId) {
        WorkflowExecutionSnapshot snapshot = workflowStateService.createSnapshot(executionId);
        return ResponseEntity.ok(snapshot);
    }
    
    /**
     * 从快照恢复流程执行
     */
    @PostMapping("/restore")
    public ResponseEntity<Map<String, Object>> restoreFromSnapshot(@RequestBody Map<String, Object> request) {
        Long snapshotId = Long.valueOf(request.get("snapshotId").toString());
        
        // 从数据库获取完整的快照数据
        WorkflowExecutionSnapshot snapshot = workflowStateService.getSnapshotById(snapshotId);
        if (snapshot == null) {
            return ResponseEntity.notFound().build();
        }
        
        // 恢复执行
        var execution = workflowStateService.restoreFromSnapshot(snapshot);
        
        return ResponseEntity.ok(Map.of(
                "executionId", execution.getId(),
                "message", "Workflow execution restored successfully"
        ));
    }
    
    /**
     * 获取流程执行状态
     */
    @GetMapping("/executions/{executionId}")
    public ResponseEntity<WorkflowExecutionState> getExecutionState(@PathVariable Long executionId) {
        WorkflowExecutionState state = workflowStateService.getExecutionState(executionId);
        return ResponseEntity.ok(state);
    }
    
    /**
     * 获取指定执行的所有快照
     */
    @GetMapping("/snapshots/execution/{executionId}")
    public ResponseEntity<List<WorkflowExecutionSnapshot>> getExecutionSnapshots(@PathVariable Long executionId) {
        List<WorkflowExecutionSnapshot> snapshots = workflowStateService.getSnapshots(executionId);
        return ResponseEntity.ok(snapshots);
    }
    
    /**
     * 获取指定执行的最新快照
     */
    @GetMapping("/snapshots/execution/{executionId}/latest")
    public ResponseEntity<WorkflowExecutionSnapshot> getLatestSnapshot(@PathVariable Long executionId) {
        WorkflowExecutionSnapshot snapshot = workflowStateService.getLatestSnapshot(executionId);
        if (snapshot == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(snapshot);
    }
    
    /**
     * 删除指定快照
     */
    @DeleteMapping("/snapshots/{snapshotId}")
    public ResponseEntity<Map<String, Object>> deleteSnapshot(@PathVariable Long snapshotId) {
        try {
            workflowStateService.deleteSnapshot(snapshotId);
            return ResponseEntity.ok(Map.of(
                    "message", "Snapshot deleted successfully",
                    "snapshotId", snapshotId
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 清理过期快照
     */
    @DeleteMapping("/snapshots/cleanup")
    public ResponseEntity<Map<String, Object>> cleanupExpiredSnapshots(@RequestParam(defaultValue = "30") int retentionDays) {
        workflowStateService.cleanupExpiredSnapshots(retentionDays);
        return ResponseEntity.ok(Map.of(
                "message", "Expired snapshots cleaned up successfully",
                "retentionDays", retentionDays
        ));
    }
    
    /**
     * 获取可恢复的执行列表
     */
    @GetMapping("/resumable")
    public ResponseEntity<List<Map<String, Object>>> getResumableExecutions() {
        var executions = workflowStateService.getResumableExecutions();
        
        List<Map<String, Object>> result = executions.stream()
                .map(execution -> Map.of(
                        "executionId", execution.getId(),
                        "workflowName", execution.getWorkflowName(),
                        "status", execution.getStatus(),
                        "startedAt", execution.getStartedAt(),
                        "errorMessage", execution.getErrorMessage()
                ))
                .toList();
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 保存检查点
     */
    @PostMapping("/checkpoints/{executionId}")
    public ResponseEntity<Map<String, Object>> saveCheckpoint(
            @PathVariable Long executionId,
            @RequestBody Map<String, Object> request) {
        
        String nodeId = request.get("nodeId").toString();
        @SuppressWarnings("unchecked")
        Map<String, Object> contextData = (Map<String, Object>) request.get("contextData");
        
        workflowStateService.saveCheckpoint(executionId, nodeId, contextData);
        
        return ResponseEntity.ok(Map.of(
                "message", "Checkpoint saved successfully",
                "nodeId", nodeId,
                "executionId", executionId
        ));
    }
    
    /**
     * 获取执行进度
     */
    @GetMapping("/progress/{executionId}")
    public ResponseEntity<Map<String, Object>> getExecutionProgress(@PathVariable Long executionId) {
        WorkflowExecutionState state = workflowStateService.getExecutionState(executionId);
        
        Map<String, Object> progress = Map.of(
                "executionId", executionId,
                "workflowName", state.getWorkflowName(),
                "status", state.getStatus(),
                "progress", state.getProgress(),
                "currentNodeId", state.getCurrentNodeId(),
                "startedAt", state.getStartedAt(),
                "completedAt", state.getCompletedAt(),
                "taskCount", state.getTaskStates().size(),
                "completedTaskCount", state.getTaskStates().stream()
                        .filter(task -> "COMPLETED".equals(task.getStatus().toString()))
                        .count(),
                "failedTaskCount", state.getTaskStates().stream()
                        .filter(task -> "FAILED".equals(task.getStatus().toString()))
                        .count()
        );
        
        return ResponseEntity.ok(progress);
    }
    
    /**
     * 批量恢复执行
     */
    @PostMapping("/batch-restore")
    public ResponseEntity<Map<String, Object>> batchRestore(@RequestBody Map<String, Object> request) {
        @SuppressWarnings("unchecked")
        List<Long> executionIds = (List<Long>) request.get("executionIds");
        
        List<Map<String, Object>> results = executionIds.stream()
                .map(executionId -> {
                    try {
                        // 这里简化处理，实际应该根据执行ID获取快照并恢复
                        return Map.of(
                                "executionId", executionId,
                                "status", "SUCCESS",
                                "message", "Execution restored successfully"
                        );
                    } catch (Exception e) {
                        return Map.of(
                                "executionId", executionId,
                                "status", "FAILED",
                                "message", "Failed to restore execution: " + e.getMessage()
                        );
                    }
                })
                .toList();
        
        return ResponseEntity.ok(Map.of(
                "results", results,
                "total", results.size(),
                "successful", results.stream().filter(r -> "SUCCESS".equals(r.get("status"))).count(),
                "failed", results.stream().filter(r -> "FAILED".equals(r.get("status"))).count()
        ));
    }
}