package me.zhengjie.modules.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.workflow.domain.WorkflowExecution;

/**
 * 流程执行实例服务接口
 * @author workflow-engine
 */
public interface WorkflowExecutionService extends IService<WorkflowExecution> {
    
    /**
     * 启动流程执行
     * @param workflowId 流程ID
     * @param inputParams 输入参数
     * @return 执行实例
     */
    WorkflowExecution startExecution(Long workflowId, String inputParams);
    
    /**
     * 获取执行状态
     * @param id 执行ID
     * @return 执行状态
     */
    WorkflowExecution getExecutionStatus(Long id);
    
    /**
     * 获取执行轨迹
     * @param id 执行ID
     * @return 执行轨迹
     */
    String getExecutionTrace(Long id);

    /**
     * 暂停流程执行
     * @param id 执行ID
     */
    void pauseExecution(Long id);

    /**
     * 取消流程执行
     * @param id 执行ID
     */
    void cancelExecution(Long id);
}
