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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.schedule.domain.EmployeeShift;
import me.zhengjie.modules.schedule.domain.dto.*;
import me.zhengjie.utils.PageResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * 员工排班Service
 * @author Zheng Jie
 * @date 2026-02-26
 */
public interface EmployeeShiftService extends IService<EmployeeShift> {

    /**
     * 分页查询
     */
    PageResult<EmployeeShift> queryAll(EmployeeShiftQueryCriteria criteria, Page<Object> page);

    /**
     * 查询全部
     */
    List<EmployeeShift> queryAll(EmployeeShiftQueryCriteria criteria);

    /**
     * 根据ID查询
     */
    EmployeeShift findById(Long id);

    /**
     * 查询员工某日期范围内的排班
     */
    List<EmployeeShift> findByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 查询部门某日期范围内的排班
     */
    List<EmployeeShift> findByDeptAndDateRange(Long deptId, LocalDate startDate, LocalDate endDate);

    /**
     * 获取排班日历
     */
    List<ScheduleCalendarVO> getScheduleCalendar(EmployeeShiftQueryCriteria criteria);

    /**
     * 创建排班
     */
    void create(EmployeeShift resources);

    /**
     * 编辑排班
     */
    void update(EmployeeShift resources);

    /**
     * 批量排班
     */
    void batchSchedule(BatchScheduleDTO dto);

    /**
     * 自动生成排班
     */
    void generateSchedule(GenerateScheduleDTO dto);

    /**
     * 删除排班
     */
    void delete(List<Long> ids);

    /**
     * 导出数据
     */
    void download(List<EmployeeShift> data, HttpServletResponse response) throws IOException;
}
