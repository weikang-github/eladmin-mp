package com.workflow.engine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workflow.engine.entity.*;
import com.workflow.engine.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkflowStateService {
    
    private static final Logger logger = LoggerFactory.getLogger(WorkflowStateService.class);
    
    @Autowired
    private WorkflowExecutionRepository workflowExecutionRepository;
    
    @Autowired
    private TaskExecutionRepository taskExecutionRepository;
    
    @Autowired
    private ExecutionLogRepository executionLogRepository;
    
    @Autowired
    private WorkflowExecutionSnapshotRepository snapshotRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * 创建流程执行状态快照
     */
    @Transactional
    public com.workflow.engine.entity.WorkflowExecutionSnapshot createSnapshot(Long workflowExecutionId) {
        logger.info("Creating snapshot for workflow execution: {}", workflowExecutionId);
        
        WorkflowExecution execution = workflowExecutionRepository.findById(workflowExecutionId)
                .orElseThrow(() -> new IllegalArgumentException("Workflow execution not found: " + workflowExecutionId));
        
        // 获取所有任务执行状态
        List<TaskExecution> taskExecutions = taskExecutionRepository.findByWorkflowExecutionIdOrderByStartedAtAsc(workflowExecutionId);
        
        // 创建状态快照
        com.workflow.engine.entity.WorkflowExecutionSnapshot snapshot = new com.workflow.engine.entity.WorkflowExecutionSnapshot();
        snapshot.setWorkflowExecutionId(workflowExecutionId);
        snapshot.setWorkflowDefinitionId(execution.getWorkflowDefinitionId());
        snapshot.setWorkflowName(execution.getWorkflowName());
        snapshot.setStatus(execution.getStatus());
        snapshot.setCurrentNodeId(execution.getCurrentNodeId());
        snapshot.setInputParametersFromMap(execution.getInputParameters());
        snapshot.setContextDataFromMap(buildContextData(taskExecutions));
        snapshot.setCreatedAt(LocalDateTime.now());
        
        return snapshotRepository.save(snapshot);
    }
    
    /**
     * 恢复流程执行状态
     */
    @Transactional
    public WorkflowExecution restoreFromSnapshot(com.workflow.engine.entity.WorkflowExecutionSnapshot snapshot) {
        logger.info("Restoring workflow execution from snapshot: {}", snapshot.getId());
        
        // 创建新的流程执行实例
        WorkflowExecution execution = new WorkflowExecution();
        execution.setWorkflowDefinitionId(snapshot.getWorkflowDefinitionId());
        execution.setExecutionName("Restored execution of " + snapshot.getWorkflowName());
        execution.setStatus(WorkflowExecutionStatus.RUNNING); // 重新设置为运行状态
        execution.setInputParameters(snapshot.getInputParametersAsMap());
        execution.setCurrentNodeId(snapshot.getCurrentNodeId());
        execution.setStartedBy("system-restore");
        execution.setStartedAt(LocalDateTime.now());
        
        execution = workflowExecutionRepository.save(execution);
        
        // 恢复任务执行状态
        restoreTaskExecutions(execution.getId(), snapshot.getContextDataAsMap());
        
        // 记录恢复日志
        ExecutionLog log = new ExecutionLog();
        log.setWorkflowExecutionId(execution.getId());
        log.setLogType("RESTORE");
        log.setLogLevel("INFO");
        log.setMessage("Workflow execution restored from snapshot: " + snapshot.getId());
        log.setLoggedAt(LocalDateTime.now());
        executionLogRepository.save(log);
        
        return execution;
    }
    
    /**
     * 根据ID获取状态快照
     */
    public com.workflow.engine.entity.WorkflowExecutionSnapshot getSnapshotById(Long snapshotId) {
        logger.info("Getting snapshot by ID: {}", snapshotId);
        return snapshotRepository.findById(snapshotId).orElse(null);
    }
    
    /**
     * 获取最新的状态快照
     */
    public com.workflow.engine.entity.WorkflowExecutionSnapshot getLatestSnapshot(Long workflowExecutionId) {
        logger.info("Getting latest snapshot for workflow execution: {}", workflowExecutionId);
        return snapshotRepository.findFirstByWorkflowExecutionIdOrderByCreatedAtDesc(workflowExecutionId)
                .orElse(null);
    }
    
    /**
     * 获取所有状态快照
     */
    public List<com.workflow.engine.entity.WorkflowExecutionSnapshot> getSnapshots(Long workflowExecutionId) {
        logger.info("Getting all snapshots for workflow execution: {}", workflowExecutionId);
        return snapshotRepository.findByWorkflowExecutionIdOrderByCreatedAtDesc(workflowExecutionId);
    }
    
    /**
     * 删除状态快照
     */
    @Transactional
    public void deleteSnapshot(Long snapshotId) {
        logger.info("Deleting workflow execution snapshot: {}", snapshotId);
        
        if (snapshotRepository.existsById(snapshotId)) {
            snapshotRepository.deleteById(snapshotId);
            logger.info("Successfully deleted snapshot: {}", snapshotId);
        } else {
            logger.warn("Snapshot not found: {}", snapshotId);
            throw new IllegalArgumentException("Snapshot not found: " + snapshotId);
        }
    }
    
    /**
     * 清理过期的状态快照
     */
    @Transactional
    public void cleanupExpiredSnapshots(int retentionDays) {
        logger.info("Cleaning up expired workflow execution snapshots older than {} days", retentionDays);
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(retentionDays);
        
        // 获取过期的快照
        List<com.workflow.engine.entity.WorkflowExecutionSnapshot> expiredSnapshots = 
            snapshotRepository.findByCreatedAtBefore(cutoffDate);
        
        logger.info("Found {} expired snapshots to cleanup", expiredSnapshots.size());
        
        // 删除过期快照
        snapshotRepository.deleteAll(expiredSnapshots);
        
        logger.info("Successfully cleaned up {} expired snapshots", expiredSnapshots.size());
    }
    
    /**
     * 保存流程执行检查点
     */
    @Transactional
    public void saveCheckpoint(Long workflowExecutionId, String nodeId, Map<String, Object> contextData) {
        logger.info("Saving checkpoint for workflow execution: {} at node: {}", workflowExecutionId, nodeId);
        
        WorkflowExecution execution = workflowExecutionRepository.findById(workflowExecutionId)
                .orElseThrow(() -> new IllegalArgumentException("Workflow execution not found: " + workflowExecutionId));
        
        // 更新当前节点
        execution.setCurrentNodeId(nodeId);
        execution.setUpdatedAt(LocalDateTime.now());
        workflowExecutionRepository.save(execution);
        
        // 记录检查点日志
        ExecutionLog log = new ExecutionLog();
        log.setWorkflowExecutionId(workflowExecutionId);
        log.setLogType("CHECKPOINT");
        log.setLogLevel("INFO");
        log.setMessage("Checkpoint saved at node: " + nodeId);
        log.setLoggedAt(LocalDateTime.now());
        executionLogRepository.save(log);
    }
    
    /**
     * 获取流程执行状态
     */
    public WorkflowExecutionState getExecutionState(Long workflowExecutionId) {
        WorkflowExecution execution = workflowExecutionRepository.findById(workflowExecutionId)
                .orElseThrow(() -> new IllegalArgumentException("Workflow execution not found: " + workflowExecutionId));
        
        List<TaskExecution> taskExecutions = taskExecutionRepository.findByWorkflowExecutionIdOrderByStartedAtAsc(workflowExecutionId);
        
        WorkflowExecutionState state = new WorkflowExecutionState();
        state.setExecutionId(workflowExecutionId);
        state.setWorkflowName(execution.getWorkflowName());
        state.setStatus(execution.getStatus());
        state.setCurrentNodeId(execution.getCurrentNodeId());
        state.setStartedAt(execution.getStartedAt());
        state.setCompletedAt(execution.getCompletedAt());
        state.setProgress(calculateProgress(taskExecutions));
        state.setTaskStates(buildTaskStates(taskExecutions));
        
        return state;
    }
    
    /**
     * 获取可恢复的执行列表
     */
    public List<WorkflowExecution> getResumableExecutions() {
        // 获取所有失败或中断的流程执行
        return workflowExecutionRepository.findByStatusIn(Arrays.asList(
                WorkflowExecutionStatus.FAILED,
                WorkflowExecutionStatus.CANCELLED,
                WorkflowExecutionStatus.SUSPENDED
        ));
    }
    
    /**
     * 构建上下文数据
     */
    private Map<String, Object> buildContextData(List<TaskExecution> taskExecutions) {
        Map<String, Object> contextData = new HashMap<>();
        
        // 按任务ID分组
        Map<String, List<TaskExecution>> taskGroups = taskExecutions.stream()
                .collect(Collectors.groupingBy(TaskExecution::getTaskId));
        
        for (Map.Entry<String, List<TaskExecution>> entry : taskGroups.entrySet()) {
            String taskId = entry.getKey();
            List<TaskExecution> executions = entry.getValue();
            
            // 获取最新的任务执行
            TaskExecution latestExecution = executions.stream()
                    .max(Comparator.comparing(TaskExecution::getStartedAt))
                    .orElse(null);
            
            if (latestExecution != null) {
                Map<String, Object> taskContext = new HashMap<>();
                taskContext.put("status", latestExecution.getStatus());
                taskContext.put("output", latestExecution.getOutput());
                taskContext.put("errorMessage", latestExecution.getErrorMessage());
                taskContext.put("startedAt", latestExecution.getStartedAt());
                taskContext.put("completedAt", latestExecution.getCompletedAt());
                
                contextData.put(taskId, taskContext);
            }
        }
        
        return contextData;
    }
    
    /**
     * 恢复任务执行状态
     */
    private void restoreTaskExecutions(Long workflowExecutionId, Map<String, Object> contextData) {
        logger.info("Restoring task executions for workflow execution: {}", workflowExecutionId);
        
        if (contextData == null || contextData.isEmpty()) {
            logger.warn("No context data available for restoring task executions");
            return;
        }
        
        // 遍历上下文数据，为每个任务创建执行记录
        for (Map.Entry<String, Object> entry : contextData.entrySet()) {
            String taskId = entry.getKey();
            Object taskData = entry.getValue();
            
            if (taskData instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> taskContext = (Map<String, Object>) taskData;
                
                // 创建任务执行记录
                TaskExecution taskExecution = new TaskExecution();
                taskExecution.setWorkflowExecutionId(workflowExecutionId);
                taskExecution.setTaskId(taskId);
                taskExecution.setTaskName((String) taskContext.getOrDefault("taskName", taskId));
                taskExecution.setTaskType("RESTORED"); // 标记为恢复的任务
                taskExecution.setTaskConfig(Map.of("restored", true));
                taskExecution.setStatus(TaskExecutionStatus.valueOf((String) taskContext.getOrDefault("status", "PENDING")));
                taskExecution.setInputParameters(taskContext.get("input"));
                taskExecution.setOutput(taskContext.get("output"));
                taskExecution.setErrorMessage((String) taskContext.get("errorMessage"));
                taskExecution.setStartedAt((LocalDateTime) taskContext.get("startedAt"));
                taskExecution.setCompletedAt((LocalDateTime) taskContext.get("completedAt"));
                taskExecution.setRetryCount(0);
                taskExecution.setMaxRetries(3);
                
                taskExecutionRepository.save(taskExecution);
                
                logger.info("Restored task execution: {} for workflow execution: {}", taskId, workflowExecutionId);
            }
        }
        
        logger.info("Successfully restored {} task executions", contextData.size());
    }
    
    /**
     * 计算执行进度
     */
    private double calculateProgress(List<TaskExecution> taskExecutions) {
        if (taskExecutions.isEmpty()) {
            return 0.0;
        }
        
        long completedCount = taskExecutions.stream()
                .filter(task -> TaskExecutionStatus.COMPLETED.equals(task.getStatus()))
                .count();
        
        return (double) completedCount / taskExecutions.size() * 100;
    }
    
    /**
     * 构建任务状态列表
     */
    private List<TaskState> buildTaskStates(List<TaskExecution> taskExecutions) {
        return taskExecutions.stream()
                .map(task -> {
                    TaskState state = new TaskState();
                    state.setTaskId(task.getTaskId());
                    state.setTaskName(task.getTaskName());
                    state.setStatus(task.getStatus());
                    state.setStartedAt(task.getStartedAt());
                    state.setCompletedAt(task.getCompletedAt());
                    state.setOutput(task.getOutput());
                    state.setErrorMessage(task.getErrorMessage());
                    return state;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * 工作流执行状态
     */
    public static class WorkflowExecutionState {
        private Long executionId;
        private String workflowName;
        private WorkflowExecutionStatus status;
        private String currentNodeId;
        private LocalDateTime startedAt;
        private LocalDateTime completedAt;
        private double progress;
        private List<TaskState> taskStates;
        
        // Getters and setters
        public Long getExecutionId() { return executionId; }
        public void setExecutionId(Long executionId) { this.executionId = executionId; }
        
        public String getWorkflowName() { return workflowName; }
        public void setWorkflowName(String workflowName) { this.workflowName = workflowName; }
        
        public WorkflowExecutionStatus getStatus() { return status; }
        public void setStatus(WorkflowExecutionStatus status) { this.status = status; }
        
        public String getCurrentNodeId() { return currentNodeId; }
        public void setCurrentNodeId(String currentNodeId) { this.currentNodeId = currentNodeId; }
        
        public LocalDateTime getStartedAt() { return startedAt; }
        public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
        
        public LocalDateTime getCompletedAt() { return completedAt; }
        public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
        
        public double getProgress() { return progress; }
        public void setProgress(double progress) { this.progress = progress; }
        
        public List<TaskState> getTaskStates() { return taskStates; }
        public void setTaskStates(List<TaskState> taskStates) { this.taskStates = taskStates; }
    }
    
    /**
     * 任务状态
     */
    public static class TaskState {
        private String taskId;
        private String taskName;
        private TaskExecutionStatus status;
        private LocalDateTime startedAt;
        private LocalDateTime completedAt;
        private Object output;
        private String errorMessage;
        
        // Getters and setters
        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        
        public String getTaskName() { return taskName; }
        public void setTaskName(String taskName) { this.taskName = taskName; }
        
        public TaskExecutionStatus getStatus() { return status; }
        public void setStatus(TaskExecutionStatus status) { this.status = status; }
        
        public LocalDateTime getStartedAt() { return startedAt; }
        public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
        
        public LocalDateTime getCompletedAt() { return completedAt; }
        public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
        
        public Object getOutput() { return output; }
        public void setOutput(Object output) { this.output = output; }
        
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }
    
    /**
     * 工作流执行快照
     */
    public static class WorkflowExecutionSnapshot {
        private Long id;
        private Long workflowExecutionId;
        private Long workflowDefinitionId;
        private String workflowName;
        private WorkflowExecutionStatus status;
        private String currentNodeId;
        private Map<String, Object> inputParameters;
        private Map<String, Object> contextData;
        private LocalDateTime createdAt;
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public Long getWorkflowExecutionId() { return workflowExecutionId; }
        public void setWorkflowExecutionId(Long workflowExecutionId) { this.workflowExecutionId = workflowExecutionId; }
        
        public Long getWorkflowDefinitionId() { return workflowDefinitionId; }
        public void setWorkflowDefinitionId(Long workflowDefinitionId) { this.workflowDefinitionId = workflowDefinitionId; }
        
        public String getWorkflowName() { return workflowName; }
        public void setWorkflowName(String workflowName) { this.workflowName = workflowName; }
        
        public WorkflowExecutionStatus getStatus() { return status; }
        public void setStatus(WorkflowExecutionStatus status) { this.status = status; }
        
        public String getCurrentNodeId() { return currentNodeId; }
        public void setCurrentNodeId(String currentNodeId) { this.currentNodeId = currentNodeId; }
        
        public Map<String, Object> getInputParameters() { return inputParameters; }
        public void setInputParameters(Map<String, Object> inputParameters) { this.inputParameters = inputParameters; }
        
        public Map<String, Object> getContextData() { return contextData; }
        public void setContextData(Map<String, Object> contextData) { this.contextData = contextData; }
        
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }
}