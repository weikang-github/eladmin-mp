package me.zhengjie.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.modules.workflow.domain.WorkflowDefinition;
import org.springframework.stereotype.Repository;

/**
 * 流程定义Mapper
 * @author workflow-engine
 */
@Repository
public interface WorkflowDefinitionMapper extends BaseMapper<WorkflowDefinition> {
}
