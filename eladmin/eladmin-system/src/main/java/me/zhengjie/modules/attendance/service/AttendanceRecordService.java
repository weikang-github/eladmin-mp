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
import me.zhengjie.modules.attendance.domain.AttendanceRecord;
import me.zhengjie.modules.attendance.domain.AttendanceStatistics;
import me.zhengjie.modules.attendance.domain.dto.AttendanceCheckInDto;
import me.zhengjie.modules.attendance.domain.dto.AttendanceRecordQueryCriteria;
import me.zhengjie.utils.PageResult;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 考勤记录Service
 * @author Zheng Jie
 * @date 2025-03-27
 */
public interface AttendanceRecordService extends IService<AttendanceRecord> {

    /**
     * 分页查询考勤记录
     * @param criteria 查询条件
     * @param page 分页对象
     * @return 考勤记录分页结果
     */
    PageResult<AttendanceRecord> queryAll(AttendanceRecordQueryCriteria criteria, Page<Object> page);

    /**
     * 查询所有考勤记录
     * @param criteria 查询条件
     * @return 考勤记录列表
     */
    List<AttendanceRecord> queryAll(AttendanceRecordQueryCriteria criteria);

    /**
     * 根据ID查询考勤记录
     * @param id 考勤记录ID
     * @return 考勤记录
     */
    AttendanceRecord findById(Long id);

    /**
     * 打卡
     * @param checkInDto 打卡信息
     * @return 打卡结果
     */
    AttendanceRecord checkIn(AttendanceCheckInDto checkInDto);

    /**
     * 查询用户某天的考勤记录
     * @param userId 用户ID
     * @param attendanceDate 考勤日期
     * @return 考勤记录
     */
    AttendanceRecord findByUserAndDate(Long userId, Date attendanceDate);

    /**
     * 查询用户某月的考勤记录
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 考勤记录列表
     */
    List<AttendanceRecord> findByUserAndDateRange(Long userId, Date startDate, Date endDate);

    /**
     * 查询部门某月的考勤记录
     * @param deptId 部门ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 考勤记录列表
     */
    List<AttendanceRecord> findByDeptAndDateRange(Long deptId, Date startDate, Date endDate);

    /**
     * 按日统计考勤
     * @param userId 用户ID
     * @param date 日期
     * @return 考勤统计
     */
    AttendanceStatistics statisticsByDay(Long userId, Date date);

    /**
     * 按周统计考勤
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 考勤统计
     */
    AttendanceStatistics statisticsByWeek(Long userId, Date startDate, Date endDate);

    /**
     * 按月统计考勤
     * @param userId 用户ID
     * @param year 年份
     * @param month 月份
     * @return 考勤统计
     */
    AttendanceStatistics statisticsByMonth(Long userId, Integer year, Integer month);

    /**
     * 导出考勤记录数据
     * @param list 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<AttendanceRecord> list, HttpServletResponse response) throws IOException;

    /**
     * 导出考勤统计数据
     * @param list 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void downloadStatistics(List<AttendanceStatistics> list, HttpServletResponse response) throws IOException;
}
