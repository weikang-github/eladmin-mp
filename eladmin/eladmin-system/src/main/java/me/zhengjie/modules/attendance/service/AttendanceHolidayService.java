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
package me.zhengjie.modules.attendance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.attendance.domain.AttendanceHoliday;
import me.zhengjie.modules.attendance.domain.dto.AttendanceHolidayQueryCriteria;
import me.zhengjie.utils.PageResult;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 考勤节假日Service
 * @author Zheng Jie
 * @date 2025-03-27
 */
public interface AttendanceHolidayService extends IService<AttendanceHoliday> {

    /**
     * 分页查询节假日
     * @param criteria 查询条件
     * @param page 分页对象
     * @return 节假日分页结果
     */
    PageResult<AttendanceHoliday> queryAll(AttendanceHolidayQueryCriteria criteria, Page<Object> page);

    /**
     * 查询所有节假日
     * @param criteria 查询条件
     * @return 节假日列表
     */
    List<AttendanceHoliday> queryAll(AttendanceHolidayQueryCriteria criteria);

    /**
     * 根据ID查询节假日
     * @param id 节假日ID
     * @return 节假日
     */
    AttendanceHoliday findById(Long id);

    /**
     * 创建节假日
     * @param resources 节假日信息
     */
    void create(AttendanceHoliday resources);

    /**
     * 编辑节假日
     * @param resources 节假日信息
     */
    void update(AttendanceHoliday resources);

    /**
     * 删除节假日
     * @param ids 节假日ID集合
     */
    void delete(Set<Long> ids);

    /**
     * 查询日期是否为节假日
     * @param holidayDate 日期
     * @return 节假日信息
     */
    AttendanceHoliday findByDate(Date holidayDate);

    /**
     * 查询某年的节假日
     * @param year 年份
     * @return 节假日列表
     */
    List<AttendanceHoliday> findByYear(Integer year);

    /**
     * 判断日期是否需要上班
     * @param date 日期
     * @return 是否需要上班
     */
    boolean isWorkDay(Date date, String workDays);

    /**
     * 导出节假日数据
     * @param list 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<AttendanceHoliday> list, HttpServletResponse response) throws IOException;
}
