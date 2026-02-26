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
package me.zhengjie.modules.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.modules.schedule.domain.EmployeeRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * 员工轮班规则Mapper
 * @author Zheng Jie
 * @date 2026-02-26
 */
@Mapper
public interface EmployeeRuleMapper extends BaseMapper<EmployeeRule> {

    /**
     * 查询员工生效的规则
     */
    @Select("SELECT * FROM schedule_employee_rule WHERE user_id = #{userId} AND is_enabled = 1 " +
            "AND (start_date <= #{date} AND (end_date IS NULL OR end_date >= #{date}))")
    EmployeeRule findActiveRuleByUser(@Param("userId") Long userId, @Param("date") LocalDate date);

    /**
     * 查询员工的所有规则
     */
    @Select("SELECT * FROM schedule_employee_rule WHERE user_id = #{userId} ORDER BY start_date DESC")
    List<EmployeeRule> findByUserId(@Param("userId") Long userId);

    /**
     * 查询使用某规则的所有员工
     */
    @Select("SELECT * FROM schedule_employee_rule WHERE rule_id = #{ruleId} AND is_enabled = 1")
    List<EmployeeRule> findByRuleId(@Param("ruleId") Long ruleId);

    /**
     * 查询所有启用的员工规则
     */
    @Select("SELECT * FROM schedule_employee_rule WHERE is_enabled = 1")
    List<EmployeeRule> findAllEnabled();
}
