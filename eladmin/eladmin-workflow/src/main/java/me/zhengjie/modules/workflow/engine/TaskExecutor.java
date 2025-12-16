package me.zhengjie.modules.workflow.engine;

import me.zhengjie.modules.workflow.domain.TaskExecution;
import me.zhengjie.modules.workflow.engine.model.Node;

import java.util.Map;

/**
 * 任务执行器接口
 * @author workflow-engine
 */
public interface TaskExecutor {

    /**
     * 获取任务类型
     * @return 任务类型
     */
    String getTaskType();

    /**
     * 执行任务
     * @param node 任务节点
     * @param executionContext 执行上下文
     * @param taskExecution 任务执行记录
     * @return 执行结果
     */
    Map<String, Object> execute(Node node, Map<String, Object> executionContext, TaskExecution taskExecution);

    /**
     * 验证任务配置
     * @param node 任务节点
     * @return 是否验证通过
     */
    boolean validate(Node node);
}
