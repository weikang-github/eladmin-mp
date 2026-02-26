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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.schedule.domain.EmployeeRule;
import me.zhengjie.modules.schedule.mapper.EmployeeRuleMapper;
import me.zhengjie.modules.schedule.service.EmployeeRuleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 员工轮班规则Service实现
 * @author Zheng Jie
 * @date 2026-02-26
 */
@Service
@RequiredArgsConstructor
public class EmployeeRuleServiceImpl extends ServiceImpl<EmployeeRuleMapper, EmployeeRule> implements EmployeeRuleService {

    private final EmployeeRuleMapper employeeRuleMapper;

    @Override
    public EmployeeRule findById(Long id) {
        return getById(id);
    }

    @Override
    public EmployeeRule findActiveRuleByUser(Long userId, LocalDate date) {
        return employeeRuleMapper.findActiveRuleByUser(userId, date);
    }

    @Override
    public List<EmployeeRule> findByUserId(Long userId) {
        return employeeRuleMapper.findByUserId(userId);
    }

    @Override
    public List<EmployeeRule> findByRuleId(Long ruleId) {
        return employeeRuleMapper.findByRuleId(ruleId);
    }

    @Override
    public List<EmployeeRule> findAllEnabled() {
        return employeeRuleMapper.findAllEnabled();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(EmployeeRule resources) {
        save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EmployeeRule resources) {
        updateById(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        removeById(id);
    }
}
