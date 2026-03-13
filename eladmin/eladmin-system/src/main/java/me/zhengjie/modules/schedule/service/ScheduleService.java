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
import me.zhengjie.modules.schedule.domain.Schedule;
import me.zhengjie.modules.schedule.domain.dto.*;
import me.zhengjie.utils.PageResult;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Zheng Jie
 * @date 2025-04-10
 */
public interface ScheduleService extends IService<Schedule> {

    /**
     * 查询所有排班
     * @param criteria 查询条件
     * @param page 分页参数
     * @return /
     */
    PageResult<Schedule> queryAll(ScheduleQueryCriteria criteria, Page<Object> page);

    /**
     * 查询所有排班
     * @param criteria 查询条件
     * @return /
     */
    List<Schedule> queryAll(ScheduleQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    Schedule findById(Long id);

    /**
     * 创建
     * @param resources /
     */
    void create(Schedule resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(Schedule resources);

    /**
     * 删除
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 导出数据
     * @param schedules 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<Schedule> schedules, HttpServletResponse response) throws IOException;

    /**
     * 批量排班
     * @param batchScheduleDTO /
     * @return 排班结果
     */
    Map<String, Object> batchSchedule(BatchScheduleDTO batchScheduleDTO);

    /**
     * 检测排班冲突
     * @param userId 用户ID
     * @param scheduleDate 排班日期
     * @param shiftId 班次ID
     * @param excludeScheduleId 排除的排班ID
     * @return 冲突列表
     */
    List<Schedule> checkConflict(Long userId, Date scheduleDate, Long shiftId, Long excludeScheduleId);

    /**
     * 自动生成月度排班
     * @param autoScheduleDTO /
     * @return 生成结果
     */
    Map<String, Object> autoGenerateMonthlySchedule(AutoScheduleDTO autoScheduleDTO);

    /**
     * 根据用户和日期范围查询排班
     * @param userId /
     * @param startDate /
     * @param endDate /
     * @return /
     */
    List<Schedule> findByUserIdAndDateRange(Long userId, Date startDate, Date endDate);

    /**
     * 按日历格式查询排班
     * @param criteria 查询条件
     * @return 日历格式数据
     */
    Map<String, Object> getCalendarView(ScheduleQueryCriteria criteria);

    /**
     * 检测并标记所有冲突
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 冲突数量
     */
    int detectAndMarkConflicts(Date startDate, Date endDate);

    /**
     * 获取排班统计信息
     * @param criteria 查询条件
     * @return 统计数据
     */
    Map<String, Object> getStatistics(ScheduleQueryCriteria criteria);
}
