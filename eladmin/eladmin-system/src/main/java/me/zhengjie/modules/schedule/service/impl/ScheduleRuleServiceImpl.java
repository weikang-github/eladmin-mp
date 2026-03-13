/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.schedule.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.schedule.domain.ScheduleRule;
import me.zhengjie.modules.schedule.domain.dto.ScheduleRuleQueryCriteria;
import me.zhengjie.modules.schedule.mapper.ScheduleRuleMapper;
import me.zhengjie.modules.schedule.service.ScheduleRuleService;
import me.zhengjie.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Zheng Jie
 * @date 2025-04-10
 */
@Service
@RequiredArgsConstructor
public class ScheduleRuleServiceImpl extends ServiceImpl<ScheduleRuleMapper, ScheduleRule> implements ScheduleRuleService {

    private final ScheduleRuleMapper scheduleRuleMapper;
    private final RedisUtils redisUtils;

    @Override
    public PageResult<ScheduleRule> queryAll(ScheduleRuleQueryCriteria criteria, Page<Object> page) {
        return PageUtil.toPage(scheduleRuleMapper.findAll(criteria, page));
    }

    @Override
    public List<ScheduleRule> queryAll(ScheduleRuleQueryCriteria criteria) {
        return scheduleRuleMapper.findAll(criteria);
    }

    @Override
    public ScheduleRule findById(Long id) {
        String key = CacheKey.SCHEDULE_RULE_ID + id;
        ScheduleRule scheduleRule = redisUtils.get(key, ScheduleRule.class);
        if(scheduleRule == null){
            scheduleRule = scheduleRuleMapper.selectById(id);
            redisUtils.set(key, scheduleRule, 1, TimeUnit.DAYS);
        }
        return scheduleRule;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ScheduleRule resources) {
        validateRule(resources);
        save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScheduleRule resources) {
        ScheduleRule scheduleRule = getById(resources.getId());
        if(scheduleRule == null){
            throw new BadRequestException("规则不存在");
        }
        validateRule(resources);
        saveOrUpdate(resources);
        // 清理缓存
        redisUtils.del(CacheKey.SCHEDULE_RULE_ID + resources.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            redisUtils.del(CacheKey.SCHEDULE_RULE_ID + id);
        }
        removeBatchByIds(ids);
    }

    @Override
    public void download(List<ScheduleRule> rules, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ScheduleRule rule : rules) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("规则名称", rule.getName());
            map.put("规则类型", getTypeText(rule.getType()));
            map.put("规则表达式", rule.getExpression());
            map.put("优先级", rule.getPriority());
            map.put("状态", rule.getEnabled() ? "启用" : "停用");
            map.put("备注", rule.getRemark());
            map.put("创建日期", rule.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<ScheduleRule> findEnabledByType(String type) {
        return scheduleRuleMapper.findByType(type);
    }

    @Override
    public List<ScheduleRule> findAllEnabled() {
        return scheduleRuleMapper.findEnabledRules();
    }

    @Override
    public void validateRule(ScheduleRule resources) {
        // 验证规则表达式
        if(StringUtils.isBlank(resources.getExpression())){
            throw new BadRequestException("规则表达式不能为空");
        }
        // 验证优先级
        if(resources.getPriority() == null){
            resources.setPriority(0);
        }
        // 验证状态
        if(resources.getEnabled() == null){
            resources.setEnabled(true);
        }
    }

    private String getTypeText(String type) {
        switch (type) {
            case "WEEKLY": return "每周";
            case "MONTHLY": return "每月";
            case "HOLIDAY": return "节假日";
            case "CUSTOM": return "自定义";
            default: return type;
        }
    }
}
