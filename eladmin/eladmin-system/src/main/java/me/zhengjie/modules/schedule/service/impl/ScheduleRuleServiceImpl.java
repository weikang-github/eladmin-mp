package me.zhengjie.modules.schedule.service.impl;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.exception.EntityNotFoundException;
import me.zhengjie.modules.schedule.domain.ScheduleRule;
import me.zhengjie.modules.schedule.domain.dto.ScheduleRuleQueryCriteria;
import me.zhengjie.modules.schedule.mapper.ScheduleRuleMapper;
import me.zhengjie.modules.schedule.service.ScheduleRuleService;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleRuleServiceImpl implements ScheduleRuleService {

    private final ScheduleRuleMapper scheduleRuleMapper;

    @Override
    public PageResult<ScheduleRule> queryAll(ScheduleRuleQueryCriteria criteria, Page<Object> page) {
        return PageUtil.toPage(scheduleRuleMapper.findAll(criteria, page));
    }

    @Override
    public List<ScheduleRule> queryAll(ScheduleRuleQueryCriteria criteria) {
        return scheduleRuleMapper.findAll(criteria, new Page<>(0, Integer.MAX_VALUE)).getRecords();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ScheduleRule resources) {
        if (scheduleRuleMapper.findByCode(resources.getRuleCode()) != null) {
            throw new EntityExistException(ScheduleRule.class, "ruleCode", resources.getRuleCode());
        }
        scheduleRuleMapper.insert(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScheduleRule resources) {
        ScheduleRule rule = scheduleRuleMapper.selectById(resources.getId());
        if (rule == null) {
            throw new EntityNotFoundException(ScheduleRule.class, "id", resources.getId());
        }
        ScheduleRule ruleByCode = scheduleRuleMapper.findByCode(resources.getRuleCode());
        if (ruleByCode != null && !ruleByCode.getId().equals(resources.getId())) {
            throw new EntityExistException(ScheduleRule.class, "ruleCode", resources.getRuleCode());
        }
        scheduleRuleMapper.updateById(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        scheduleRuleMapper.deleteBatchIds(ids);
    }

    @Override
    public void download(List<ScheduleRule> queryAll, HttpServletResponse response) throws IOException {
        
    }

    @Override
    public ScheduleRule findByCode(String ruleCode) {
        return scheduleRuleMapper.findByCode(ruleCode);
    }

    @Override
    public List<Long> getShiftIds(Long ruleId) {
        ScheduleRule rule = scheduleRuleMapper.selectById(ruleId);
        if (rule == null || StrUtil.isBlank(rule.getShiftIds())) {
            return null;
        }
        return Arrays.stream(rule.getShiftIds().split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
