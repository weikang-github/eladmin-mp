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
package me.zhengjie.modules.schedule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.schedule.domain.EmployeeRule;

import java.time.LocalDate;
import java.util.List;

/**
 * 员工轮班规则Service
 * @author Zheng Jie
 * @date 2026-02-26
 */
public interface EmployeeRuleService extends IService<EmployeeRule> {

    /**
     * 根据ID查询
     */
    EmployeeRule findById(Long id);

    /**
     * 查询员工生效的规则
     */
    EmployeeRule findActiveRuleByUser(Long userId, LocalDate date);

    /**
     * 查询员工的所有规则
     */
    List<EmployeeRule> findByUserId(Long userId);

    /**
     * 查询使用某规则的所有员工
     */
    List<EmployeeRule> findByRuleId(Long ruleId);

    /**
     * 查询所有启用的员工规则
     */
    List<EmployeeRule> findAllEnabled();

    /**
     * 创建
     */
    void create(EmployeeRule resources);

    /**
     * 编辑
     */
    void update(EmployeeRule resources);

    /**
     * 删除
     */
    void delete(Long id);
}
