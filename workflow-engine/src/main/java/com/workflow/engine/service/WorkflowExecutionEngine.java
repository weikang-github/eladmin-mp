package com.workflow.engine.service;

import com.workflow.engine.dto.DagGraph;
import com.workflow.engine.dto.DagNode;
import com.workflow.engine.entity.*;
import com.workflow.engine.exception.WorkflowValidationException;
import com.workflow.engine.executor.TaskExecutor;
import com.workflow.engine.repository.*;
import com.workflow.engine.utils.DagGraphBuilder;
import com.workflow.engine.config.MonitoringConfig.WorkflowMetrics;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class WorkflowExecutionEngine {
    
    private static final Logger logger = LoggerFactory.getLogger(WorkflowExecutionEngine.class);
    
    @Autowired
    private WorkflowDefinitionRepository workflowDefinitionRepository;
    
    @Autowired
    private WorkflowExecutionRepository workflowExecutionRepository;
    
    @Autowired
    private TaskExecutionRepository taskExecutionRepository;
    
    @Autowired
    private TaskDependencyRepository taskDependencyRepository;
    
    @Autowired
    private ExecutionLogRepository executionLogRepository;
    
    @Autowired
    private Map<String, TaskExecutor> taskExecutors;
    
    @Autowired
    private WorkflowMetrics workflowMetrics;
    
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
    
    /**
     * 启动流程执行
     */
    @Transactional
    public WorkflowExecution startWorkflowExecution(Long workflowDefinitionId, Map<String, Object> inputParameters, String startedBy) {
        logger.info("Starting workflow execution for definition: {}", workflowDefinitionId);
        
        // 获取流程定义
        WorkflowDefinition definition = workflowDefinitionRepository.findById(workflowDefinitionId)
                .orElseThrow(() -> new IllegalArgumentException("Workflow definition not found: " + workflowDefinitionId));
        
        // 记录指标
        workflowMetrics.recordWorkflowStarted();
        Timer.Sample workflowTimer = workflowMetrics.startWorkflowTimer();
        
        // 创建流程执行实例
        WorkflowExecution execution = new WorkflowExecution();
        execution.setWorkflowDefinitionId(workflowDefinitionId);
        execution.setWorkflowName(definition.getName());
        execution.setStatus(WorkflowExecutionStatus.RUNNING);
        execution.setInputParameters(inputParameters);
        execution.setStartedBy(startedBy);
        execution.setStartedAt(LocalDateTime.now());
        execution.setCurrentNodeId("START");
        execution = workflowExecutionRepository.save(execution);
        
        // 记录执行日志
        logExecution(execution.getId(), "WORKFLOW", "START", "Workflow execution started", null);
        
        // 异步执行流程，传递计时器
        Timer.Sample finalWorkflowTimer = workflowTimer;
        CompletableFuture.runAsync(() -> executeWorkflow(execution, finalWorkflowTimer), executorService);
        
        // 异步执行流程
        CompletableFuture.runAsync(() -> executeWorkflow(execution), executorService);
        
        return execution;
    }
    
    /**
     * 执行流程
     */
    private void executeWorkflow(WorkflowExecution execution, Timer.Sample workflowTimer) {
        try {
            logger.info("Executing workflow: {}", execution.getId());
            
            // 构建DAG图
            DagGraph dagGraph = buildDagGraph(execution.getWorkflowDefinitionId());
            
            // 验证DAG是否有环
            if (dagGraph.hasCycle()) {
                throw new WorkflowValidationException("Workflow contains cycles");
            }
            
            // 获取拓扑排序
            List<String> topologicalOrder = dagGraph.topologicalSort();
            logger.info("Topological order: {}", topologicalOrder);
            
            // 获取并行执行组
            List<Set<String>> parallelGroups = dagGraph.getParallelGroups();
            logger.info("Parallel groups: {}", parallelGroups);
            
            // 执行流程
            Map<String, TaskExecution> taskExecutions = new ConcurrentHashMap<>();
            Map<String, CompletableFuture<TaskExecutionResult>> runningTasks = new ConcurrentHashMap<>();
            
            for (Set<String> parallelGroup : parallelGroups) {
                // 等待前置任务完成
                waitForPredecessors(parallelGroup, taskExecutions);
                
                // 并行执行当前组任务
                List<CompletableFuture<TaskExecutionResult>> groupFutures = new ArrayList<>();
                
                for (String nodeId : parallelGroup) {
                    DagNode node = dagGraph.getNode(nodeId);
                    if (node == null || "START".equals(nodeId) || "END".equals(nodeId)) {
                        continue;
                    }
                    
                    // 创建任务执行实例
                    TaskExecution taskExecution = createTaskExecution(execution, node);
                    taskExecutions.put(nodeId, taskExecution);
                    
                    // 异步执行任务
                    CompletableFuture<TaskExecutionResult> future = executeTaskAsync(taskExecution, node);
                    runningTasks.put(nodeId, future);
                    groupFutures.add(future);
                }
                
                // 等待当前组所有任务完成
                CompletableFuture.allOf(groupFutures.toArray(new CompletableFuture[0])).join();
                
                // 检查是否有任务失败
                for (String nodeId : parallelGroup) {
                    TaskExecutionResult result = runningTasks.get(nodeId).get();
                    if (result != null && !result.isSuccess()) {
                        handleTaskFailure(execution, taskExecutions.get(nodeId), result.getErrorMessage());
                        return;
                    }
                }
            }
            
            // 流程执行成功
            completeWorkflowExecution(execution, WorkflowExecutionStatus.COMPLETED, "Workflow completed successfully");
            workflowMetrics.recordWorkflowCompleted();
            
        } catch (Exception e) {
            logger.error("Workflow execution failed: {}", execution.getId(), e);
            completeWorkflowExecution(execution, WorkflowExecutionStatus.FAILED, e.getMessage());
            workflowMetrics.recordWorkflowFailed();
        } finally {
            // 记录流程执行时间
            workflowMetrics.recordWorkflowDuration(workflowTimer);
        }
    }
    
    /**
     * 异步执行任务
     */
    private CompletableFuture<TaskExecutionResult> executeTaskAsync(TaskExecution taskExecution, DagNode node) {
        return CompletableFuture.supplyAsync(() -> {
            // 记录任务指标
            workflowMetrics.recordTaskExecuted();
            Timer.Sample taskTimer = workflowMetrics.startTaskTimer();
            
            try {
                logger.info("Executing task: {} (type: {})", taskExecution.getTaskName(), taskExecution.getTaskType());
                
                // 获取任务执行器
                TaskExecutor executor = taskExecutors.get(taskExecution.getTaskType());
                if (executor == null) {
                    throw new IllegalArgumentException("No executor found for task type: " + taskExecution.getTaskType());
                }
                
                // 验证任务配置
                executor.validateConfig(node);
                
                // 获取超时时间
                Integer timeoutSeconds = executor.getTimeoutSeconds(node);
                if (timeoutSeconds == null) {
                    timeoutSeconds = 300; // 默认5分钟
                }
                
                // 执行任务
                TaskExecutionResult result;
                try {
                    result = executor.execute(taskExecution, node);
                } catch (Exception e) {
                    logger.error("Task execution failed: {}", taskExecution.getId(), e);
                    result = TaskExecutionResult.failure(e.getMessage());
                }
                
                // 更新任务状态
                updateTaskExecutionStatus(taskExecution, result);
                
                // 记录任务完成指标
                if (result.isSuccess()) {
                    workflowMetrics.recordTaskCompleted();
                } else {
                    workflowMetrics.recordTaskFailed();
                }
                
                return result;
                
            } catch (Exception e) {
                logger.error("Task execution error: {}", taskExecution.getId(), e);
                updateTaskExecutionStatus(taskExecution, TaskExecutionResult.failure(e.getMessage()));
                workflowMetrics.recordTaskFailed();
                return TaskExecutionResult.failure(e.getMessage());
            } finally {
                // 记录任务执行时间
                workflowMetrics.recordTaskDuration(taskTimer);
            }
        }, executorService);
    }
    
    /**
     * 等待前置任务完成
     */
    private void waitForPredecessors(Set<String> parallelGroup, Map<String, TaskExecution> taskExecutions) {
        // 实现等待逻辑
        // 这里简化处理，实际应该检查所有前置任务的状态
    }
    
    /**
     * 创建任务执行实例
     */
    private TaskExecution createTaskExecution(WorkflowExecution workflowExecution, DagNode node) {
        TaskExecution taskExecution = new TaskExecution();
        taskExecution.setWorkflowExecutionId(workflowExecution.getId());
        taskExecution.setTaskId(node.getId());
        taskExecution.setTaskName(node.getName());
        taskExecution.setTaskType(node.getType());
        taskExecution.setStatus(TaskExecutionStatus.PENDING);
        taskExecution.setTaskConfig(node.getProperties());
        taskExecution.setInputParameters(workflowExecution.getInputParameters());
        taskExecution.setStartedAt(LocalDateTime.now());
        
        return taskExecutionRepository.save(taskExecution);
    }
    
    /**
     * 更新任务状态
     */
    private void updateTaskExecutionStatus(TaskExecution taskExecution, TaskExecutionResult result) {
        taskExecution.setStatus(result.isSuccess() ? TaskExecutionStatus.COMPLETED : TaskExecutionStatus.FAILED);
        taskExecution.setOutput(result.getOutput());
        taskExecution.setErrorMessage(result.getErrorMessage());
        taskExecution.setCompletedAt(LocalDateTime.now());
        
        taskExecutionRepository.save(taskExecution);
        
        // 记录执行日志
        String logLevel = result.isSuccess() ? "INFO" : "ERROR";
        String message = result.isSuccess() ? 
                "Task completed successfully" : 
                "Task failed: " + result.getErrorMessage();
        logExecution(taskExecution.getWorkflowExecutionId(), "TASK", logLevel, 
                String.format("Task %s: %s", taskExecution.getTaskName(), message), taskExecution.getId());
    }
    
    /**
     * 处理任务失败
     */
    private void handleTaskFailure(WorkflowExecution workflowExecution, TaskExecution taskExecution, String errorMessage) {
        logger.error("Task {} failed: {}", taskExecution.getTaskName(), errorMessage);
        
        // 更新流程状态
        completeWorkflowExecution(workflowExecution, WorkflowExecutionStatus.FAILED, 
                String.format("Task %s failed: %s", taskExecution.getTaskName(), errorMessage));
    }
    
    /**
     * 完成流程执行
     */
    private void completeWorkflowExecution(WorkflowExecution execution, WorkflowExecutionStatus status, String message) {
        execution.setStatus(status);
        execution.setCompletedAt(LocalDateTime.now());
        execution.setErrorMessage(status == WorkflowExecutionStatus.FAILED ? message : null);
        
        workflowExecutionRepository.save(execution);
        
        // 记录执行日志
        String logLevel = status == WorkflowExecutionStatus.COMPLETED ? "INFO" : "ERROR";
        logExecution(execution.getId(), "WORKFLOW", logLevel, message, null);
    }
    
    /**
     * 构建DAG图
     */
    private DagGraph buildDagGraph(Long workflowDefinitionId) {
        WorkflowDefinition definition = workflowDefinitionRepository.findById(workflowDefinitionId)
                .orElseThrow(() -> new IllegalArgumentException("Workflow definition not found"));
        
        return DagGraphBuilder.buildFromDefinition(definition);
    }
    
    /**
     * 记录执行日志
     */
    private void logExecution(Long workflowExecutionId, String logType, String logLevel, String message, Long taskExecutionId) {
        ExecutionLog log = new ExecutionLog();
        log.setWorkflowExecutionId(workflowExecutionId);
        log.setTaskExecutionId(taskExecutionId);
        log.setLogType(logType);
        log.setLogLevel(logLevel);
        log.setMessage(message);
        log.setLoggedAt(LocalDateTime.now());
        
        executionLogRepository.save(log);
    }
    
    /**
     * 获取流程执行状态
     */
    public WorkflowExecution getWorkflowExecution(Long executionId) {
        return workflowExecutionRepository.findById(executionId)
                .orElseThrow(() -> new IllegalArgumentException("Workflow execution not found: " + executionId));
    }
    
    /**
     * 获取任务执行列表
     */
    public List<TaskExecution> getTaskExecutions(Long workflowExecutionId) {
        return taskExecutionRepository.findByWorkflowExecutionIdOrderByStartedAtAsc(workflowExecutionId);
    }
    
    /**
     * 获取执行日志
     */
    public List<ExecutionLog> getExecutionLogs(Long workflowExecutionId) {
        return executionLogRepository.findByWorkflowExecutionIdOrderByLoggedAtAsc(workflowExecutionId);
    }
}