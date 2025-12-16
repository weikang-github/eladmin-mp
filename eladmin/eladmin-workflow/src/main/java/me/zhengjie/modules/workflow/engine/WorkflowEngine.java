package me.zhengjie.modules.workflow.engine;

import com.alibaba.fastjson2.JSON;
import me.zhengjie.modules.workflow.domain.TaskExecution;
import me.zhengjie.modules.workflow.domain.WorkflowDefinition;
import me.zhengjie.modules.workflow.domain.WorkflowExecution;
import me.zhengjie.modules.workflow.engine.model.Node;
import me.zhengjie.modules.workflow.engine.model.WorkflowGraph;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Future;

/**
 * 工作流执行引擎核心类
 * @author workflow-engine
 */
@Component
public class WorkflowEngine {

    @Autowired
    private List<TaskExecutor> taskExecutors;

    @Autowired
    private DagResolver dagResolver;

    private final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

    public WorkflowEngine() {
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(50);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.initialize();
    }

    /**
     * 执行工作流
     * @param workflowDefinition 流程定义
     * @param execution 执行实例
     */
    public void execute(WorkflowDefinition workflowDefinition, WorkflowExecution execution) {
        // 解析工作流程图
        WorkflowGraph workflowGraph = dagResolver.parseFromJson(workflowDefinition.getDefinitionJson());

        // 拓扑排序获取执行顺序
        List<Node> sortedNodes = dagResolver.topologicalSort(workflowGraph);

        // 创建执行上下文
        Map<String, Object> executionContext = new HashMap<>();
        if (StringUtils.isNotBlank(execution.getInputParams())) {
            executionContext.putAll(JSON.parseObject(execution.getInputParams(), Map.class));
        }

        // 执行任务
        for (Node node : sortedNodes) {
            // 创建任务执行记录
            TaskExecution taskExecution = new TaskExecution();
            taskExecution.setExecutionId(execution.getId());
            taskExecution.setTaskId(node.getId());
            taskExecution.setTaskType(node.getType());
            taskExecution.setTaskName(node.getName());
            taskExecution.setTaskConfig(JSON.toJSONString(node.getConfig()));
            taskExecution.setInputParams(JSON.toJSONString(executionContext));
            taskExecution.setStatus("RUNNING");
            taskExecution.setStartTime(new Timestamp(System.currentTimeMillis()));

            try {
                // 执行任务
                Map<String, Object> taskResult = executeTask(node, executionContext, taskExecution);

                // 更新任务执行记录
                taskExecution.setEndTime(new Timestamp(System.currentTimeMillis()));
                taskExecution.setDuration(taskExecution.getEndTime().getTime() - taskExecution.getStartTime().getTime());
                taskExecution.setOutputResult(JSON.toJSONString(taskResult));
                taskExecution.setStatus("COMPLETED");
                taskExecution.setIsCompleted(true);
                taskExecution.setIsSuccess(true);

                // 更新执行上下文
                executionContext.putAll(taskResult);

            } catch (Exception e) {
                // 处理任务失败
                taskExecution.setEndTime(new Timestamp(System.currentTimeMillis()));
                taskExecution.setDuration(taskExecution.getEndTime().getTime() - taskExecution.getStartTime().getTime());
                taskExecution.setStatus("FAILED");
                taskExecution.setErrorMessage(e.getMessage());
                taskExecution.setIsCompleted(true);
                taskExecution.setIsSuccess(false);

                // 更新执行实例状态
                execution.setStatus("FAILED");
                execution.setErrorMessage(e.getMessage());
                execution.setEndTime(new Timestamp(System.currentTimeMillis()));
                execution.setLastNodeId(node.getId());
                
                throw new RuntimeException("Workflow execution failed at node: " + node.getId(), e);
            }

            // 保存任务执行记录到数据库
            // taskExecutionService.save(taskExecution);
        }

        // 更新执行实例状态
        execution.setStatus("COMPLETED");
        execution.setEndTime(new Timestamp(System.currentTimeMillis()));
        execution.setOutputResult(JSON.toJSONString(executionContext));

        // 保存执行实例到数据库
        // workflowExecutionService.updateById(execution);
    }

    /**
     * 执行单个任务
     * @param node 任务节点
     * @param executionContext 执行上下文
     * @param taskExecution 任务执行记录
     * @return 任务执行结果
     */
    private Map<String, Object> executeTask(Node node, Map<String, Object> executionContext, TaskExecution taskExecution) {
        // 查找对应的任务执行器
        TaskExecutor executor = taskExecutors.stream()
                .filter(e -> e.getTaskType().equals(node.getType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No executor found for task type: " + node.getType()));

        // 验证任务配置
        if (!executor.validate(node)) {
            throw new RuntimeException("Invalid task configuration for node: " + node.getId());
        }

        // 执行任务
        return executor.execute(node, executionContext, taskExecution);
    }

    /**
     * 并行执行任务
     * @param nodes 任务节点列表
     * @param executionContext 执行上下文
     * @param executionId 执行实例ID
     * @return 任务执行结果列表
     */
    public List<Map<String, Object>> executeParallel(List<Node> nodes, Map<String, Object> executionContext, Long executionId) {
        List<Future<Map<String, Object>>> futures = new ArrayList<>();
        List<Map<String, Object>> results = new ArrayList<>();

        for (Node node : nodes) {
            Future<Map<String, Object>> future = taskExecutor.submit(() -> {
                // 创建任务执行记录
                TaskExecution taskExecution = new TaskExecution();
                taskExecution.setExecutionId(executionId);
                taskExecution.setTaskId(node.getId());
                taskExecution.setTaskType(node.getType());
                taskExecution.setTaskName(node.getName());
                taskExecution.setTaskConfig(JSON.toJSONString(node.getConfig()));
                taskExecution.setInputParams(JSON.toJSONString(executionContext));
                taskExecution.setStatus("RUNNING");
                taskExecution.setStartTime(new Timestamp(System.currentTimeMillis()));

                try {
                    Map<String, Object> taskResult = executeTask(node, executionContext, taskExecution);
                    taskExecution.setEndTime(new Timestamp(System.currentTimeMillis()));
                    taskExecution.setDuration(taskExecution.getEndTime().getTime() - taskExecution.getStartTime().getTime());
                    taskExecution.setOutputResult(JSON.toJSONString(taskResult));
                    taskExecution.setStatus("COMPLETED");
                    taskExecution.setIsCompleted(true);
                    taskExecution.setIsSuccess(true);

                    // 保存任务执行记录到数据库
                    // taskExecutionService.save(taskExecution);

                    return taskResult;
                } catch (Exception e) {
                    taskExecution.setEndTime(new Timestamp(System.currentTimeMillis()));
                    taskExecution.setDuration(taskExecution.getEndTime().getTime() - taskExecution.getStartTime().getTime());
                    taskExecution.setStatus("FAILED");
                    taskExecution.setErrorMessage(e.getMessage());
                    taskExecution.setIsCompleted(true);
                    taskExecution.setIsSuccess(false);

                    // 保存任务执行记录到数据库
                    // taskExecutionService.save(taskExecution);

                    throw new RuntimeException("Parallel task execution failed: " + node.getId(), e);
                }
            });

            futures.add(future);
        }

        // 收集结果
        for (Future<Map<String, Object>> future : futures) {
            try {
                results.add(future.get());
            } catch (Exception e) {
                throw new RuntimeException("Parallel task execution failed", e);
            }
        }

        return results;
    }
}
