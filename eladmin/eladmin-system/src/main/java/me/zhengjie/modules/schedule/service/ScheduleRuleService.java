package me.zhengjie.modules.schedule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.schedule.domain.ScheduleRule;
import me.zhengjie.modules.schedule.domain.dto.ScheduleRuleQueryCriteria;
import me.zhengjie.utils.PageResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ScheduleRuleService extends IService<ScheduleRule> {

    PageResult<ScheduleRule> queryAll(ScheduleRuleQueryCriteria criteria, Page<Object> page);

    List<ScheduleRule> queryAll(ScheduleRuleQueryCriteria criteria);

    void create(ScheduleRule resources);

    void update(ScheduleRule resources);

    void delete(java.util.Set<Long> ids);

    void download(List<ScheduleRule> queryAll, HttpServletResponse response) throws IOException;

    ScheduleRule findByCode(String ruleCode);

    java.util.List<Long> getShiftIds(Long ruleId);
}
