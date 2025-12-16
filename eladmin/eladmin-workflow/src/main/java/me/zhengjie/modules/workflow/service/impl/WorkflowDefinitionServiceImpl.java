package me.zhengjie.modules.workflow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.modules.workflow.domain.WorkflowDefinition;
import me.zhengjie.modules.workflow.mapper.WorkflowDefinitionMapper;
import me.zhengjie.modules.workflow.service.WorkflowDefinitionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 流程定义服务实现
 * @author workflow-engine
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkflowDefinitionServiceImpl extends ServiceImpl<WorkflowDefinitionMapper, WorkflowDefinition>
        implements WorkflowDefinitionService {

    @Override
    public WorkflowDefinition saveWithVersioning(WorkflowDefinition workflowDefinition) {
        // 如果是新流程，直接保存
        if (workflowDefinition.getId() == null) {
            workflowDefinition.setVersion(1);
            workflowDefinition.setIsLatest(true);
            this.save(workflowDefinition);
            return workflowDefinition;
        }

        // 如果是现有流程，创建新版本
        WorkflowDefinition existing = this.getById(workflowDefinition.getId());
        if (existing != null) {
            // 将当前版本标记为非最新版本
            existing.setIsLatest(false);
            this.updateById(existing);

            // 创建新版本
            WorkflowDefinition newVersion = new WorkflowDefinition();
            newVersion.setName(workflowDefinition.getName());
            newVersion.setDescription(workflowDefinition.getDescription());
            newVersion.setDefinitionJson(workflowDefinition.getDefinitionJson());
            newVersion.setVersion(existing.getVersion() + 1);
            newVersion.setEnabled(workflowDefinition.getEnabled() != null ? workflowDefinition.getEnabled() : existing.getEnabled());
            newVersion.setIsLatest(true);
            
            this.save(newVersion);
            return newVersion;
        }

        return null;
    }

    @Override
    public WorkflowDefinition getLatestVersion(Long id) {
        List<WorkflowDefinition> list = this.lambdaQuery()
                .eq(WorkflowDefinition::getId, id)
                .eq(WorkflowDefinition::getIsLatest, true)
                .list();

        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<WorkflowDefinition> getVersionHistory(Long id) {
        return this.lambdaQuery()
                .eq(WorkflowDefinition::getId, id)
                .orderByDesc(WorkflowDefinition::getVersion)
                .list();
    }

    @Override
    public void deleteDefinition(Long id) {
        this.lambdaUpdate()
                .eq(WorkflowDefinition::getId, id)
                .remove();
    }
}
