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
import me.zhengjie.modules.schedule.domain.EmployeeShift;
import me.zhengjie.modules.schedule.domain.dto.EmployeeShiftQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * 员工排班Mapper
 * @author Zheng Jie
 * @date 2026-02-26
 */
@Mapper
public interface EmployeeShiftMapper extends BaseMapper<EmployeeShift> {

    /**
     * 条件查询
     */
    List<EmployeeShift> findAll(@Param("criteria") EmployeeShiftQueryCriteria criteria);

    /**
     * 根据员工和日期查询
     */
    @Select("SELECT * FROM schedule_employee_shift WHERE user_id = #{userId} AND shift_date = #{date}")
    EmployeeShift findByUserAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    /**
     * 查询员工某日期范围内的排班
     */
    List<EmployeeShift> findByUserAndDateRange(@Param("userId") Long userId, 
                                                @Param("startDate") LocalDate startDate, 
                                                @Param("endDate") LocalDate endDate);

    /**
     * 查询部门某日期范围内的排班
     */
    List<EmployeeShift> findByDeptAndDateRange(@Param("deptId") Long deptId, 
                                                @Param("startDate") LocalDate startDate, 
                                                @Param("endDate") LocalDate endDate);

    /**
     * 批量插入
     */
    void batchInsert(@Param("list") List<EmployeeShift> list);

    /**
     * 删除某日期范围内的排班
     */
    void deleteByDateRange(@Param("userId") Long userId, 
                           @Param("startDate") LocalDate startDate, 
                           @Param("endDate") LocalDate endDate);
}
