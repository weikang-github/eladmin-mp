package me.zhengjie.modules.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.schedule.domain.ScheduleRule;
import me.zhengjie.modules.schedule.domain.dto.ScheduleRuleQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ScheduleRuleMapper extends BaseMapper<ScheduleRule> {

    Long countAll(@Param("criteria") ScheduleRuleQueryCriteria criteria);

    IPage<ScheduleRule> findAll(@Param("criteria") ScheduleRuleQueryCriteria criteria, Page<Object> page);

    ScheduleRule findByCode(@Param("ruleCode") String ruleCode);
}
