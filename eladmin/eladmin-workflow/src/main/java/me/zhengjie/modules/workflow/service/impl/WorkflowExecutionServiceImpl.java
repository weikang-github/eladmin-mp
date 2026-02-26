package me.zhengjie.modules.workflow.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.modules.workflow.domain.TaskExecution;
import me.zhengjie.modules.workflow.domain.WorkflowDefinition;
import me.zhengjie.modules.workflow.domain.WorkflowExecution;
import me.zhengjie.modules.workflow.engine.WorkflowEngine;
import me.zhengjie.modules.workflow.enums.ExecutionStatus;
import me.zhengjie.modules.workflow.mapper.TaskExecutionMapper;
import me.zhengjie.modules.workflow.mapper.WorkflowExecutionMapper;
import me.zhengjie.modules.workflow.service.WorkflowDefinitionService;
import me.zhengjie.modules.workflow.service.WorkflowExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 流程执行实例服务实现
 * @author workflow-engine
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkflowExecutionServiceImpl extends ServiceImpl<WorkflowExecutionMapper, WorkflowExecution>
        implements WorkflowExecutionService {

    @Autowired
    private WorkflowDefinitionService workflowDefinitionService;

    @Autowired
    private WorkflowEngine workflowEngine;

    @Autowired
    private TaskExecutionMapper taskExecutionMapper;

    @Override
    public WorkflowExecution startExecution(Long workflowId, String inputParams) {
        // 获取最新版本的流程定义
        WorkflowDefinition workflowDefinition = workflowDefinitionService.getById(workflowId);
        if (workflowDefinition == null || !workflowDefinition.getEnabled()) {
            throw new RuntimeException("Workflow definition not found or disabled");
        }

        // 创建执行实例
        WorkflowExecution execution = new WorkflowExecution();
        execution.setWorkflowId(workflowId);
        execution.setName(workflowDefinition.getName() + "-" + System.currentTimeMillis());
        execution.setStatus(ExecutionStatus.RUNNING.getValue());
        execution.setInputParams(inputParams);
        execution.setStartTime(new Timestamp(System.currentTimeMillis()));

        // 保存执行实例
        this.save(execution);

        // 异步执行流程
        new Thread(() -> {
            try {
                workflowEngine.execute(workflowDefinition, execution);
            } catch (Exception e) {
                // 更新执行状态为失败
                execution.setStatus(ExecutionStatus.FAILED.getValue());
                execution.setErrorMessage(e.getMessage());
                execution.setEndTime(new Timestamp(System.currentTimeMillis()));
                this.updateById(execution);
            }
        }).start();

        return execution;
    }

    @Override
    public WorkflowExecution getExecutionStatus(Long id) {
        return this.getById(id);
    }

    @Override
    public String getExecutionTrace(Long id) {
        // 获取执行实例
        WorkflowExecution execution = this.getById(id);
        if (execution == null) {
            return "{}";
        }

        // 获取任务执行记录
        List<TaskExecution> taskExecutions = taskExecutionMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<TaskExecution>()
                        .eq("execution_id", id)
                        .orderByAsc("start_time")
        );

        // 构建执行轨迹
        Map<String, Object> trace = JSON.parseObject(JSON.toJSONString(execution), Map.class);
        trace.put("taskExecutions", taskExecutions);

        return JSON.toJSONString(trace);
    }

    @Override
    public void pauseExecution(Long id) {
        WorkflowExecution execution = this.getById(id);
        if (execution != null && ExecutionStatus.RUNNING.getValue().equals(execution.getStatus())) {
            execution.setStatus(ExecutionStatus.PAUSED.getValue());
            this.updateById(execution);
        }
    }

    @Override
    public void cancelExecution(Long id) {
        WorkflowExecution execution = this.getById(id);
        if (execution != null && 
            (ExecutionStatus.RUNNING.getValue().equals(execution.getStatus()) || 
             ExecutionStatus.PAUSED.getValue().equals(execution.getStatus()))) {
            execution.setStatus(ExecutionStatus.CANCELLED.getValue());
            execution.setEndTime(new Timestamp(System.currentTimeMillis()));
            this.updateById(execution);
        }
    }
}
