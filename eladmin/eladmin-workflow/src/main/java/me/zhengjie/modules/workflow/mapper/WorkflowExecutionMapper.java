package me.zhengjie.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.modules.workflow.domain.WorkflowExecution;
import org.springframework.stereotype.Repository;

/**
 * 流程执行实例Mapper
 * @author workflow-engine
 */
@Repository
public interface WorkflowExecutionMapper extends BaseMapper<WorkflowExecution> {
}
