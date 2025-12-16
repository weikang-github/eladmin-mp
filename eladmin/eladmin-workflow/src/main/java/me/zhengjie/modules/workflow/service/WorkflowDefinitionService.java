package me.zhengjie.modules.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.workflow.domain.WorkflowDefinition;

import java.util.List;

/**
 * 流程定义服务接口
 * @author workflow-engine
 */
public interface WorkflowDefinitionService extends IService<WorkflowDefinition> {
    
    /**
     * 保存流程定义并版本化
     * @param workflowDefinition 流程定义
     * @return 保存后的流程定义
     */
    WorkflowDefinition saveWithVersioning(WorkflowDefinition workflowDefinition);
    
    /**
     * 获取最新版本的流程定义
     * @param id 流程ID
     * @return 最新版本的流程定义
     */
    WorkflowDefinition getLatestVersion(Long id);

    /**
     * 获取流程定义版本历史
     * @param id 流程ID
     * @return 版本历史列表
     */
    List<WorkflowDefinition> getVersionHistory(Long id);

    /**
     * 删除流程定义
     * @param id 流程ID
     */
    void deleteDefinition(Long id);
}
