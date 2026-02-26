package me.zhengjie.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.modules.workflow.domain.TaskExecution;
import org.springframework.stereotype.Repository;

/**
 * 任务执行记录Mapper
 * @author workflow-engine
 */
@Repository
public interface TaskExecutionMapper extends BaseMapper<TaskExecution> {
}
