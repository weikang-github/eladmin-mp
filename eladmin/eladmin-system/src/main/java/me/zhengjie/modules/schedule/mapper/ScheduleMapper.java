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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.schedule.domain.Schedule;
import me.zhengjie.modules.schedule.domain.dto.ScheduleQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.sql.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Zheng Jie
 * @date 2025-04-10
 */
@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {

    Long countAll(@Param("criteria") ScheduleQueryCriteria criteria);

    List<Schedule> findAll(@Param("criteria") ScheduleQueryCriteria criteria);

    IPage<Schedule> findAll(@Param("criteria") ScheduleQueryCriteria criteria, Page<Object> page);

    List<Schedule> findConflictSchedules(@Param("userId") Long userId, 
                                         @Param("scheduleDate") Date scheduleDate,
                                         @Param("excludeScheduleId") Long excludeScheduleId);

    List<Schedule> findByUserIdAndDateRange(@Param("userId") Long userId,
                                            @Param("startDate") Date startDate,
                                            @Param("endDate") Date endDate);

    List<Schedule> findByUserIdsAndDateRange(@Param("userIds") Set<Long> userIds,
                                             @Param("startDate") Date startDate,
                                             @Param("endDate") Date endDate);

    @Select("SELECT COUNT(*) FROM sch_schedule WHERE user_id = #{userId} AND schedule_date = #{scheduleDate} AND schedule_id != #{excludeScheduleId}")
    int countByUserIdAndDate(@Param("userId") Long userId, 
                             @Param("scheduleDate") Date scheduleDate,
                             @Param("excludeScheduleId") Long excludeScheduleId);

    void batchInsert(@Param("schedules") List<Schedule> schedules);

    void deleteByUserIdAndDateRange(@Param("userId") Long userId,
                                    @Param("startDate") Date startDate,
                                    @Param("endDate") Date endDate);
}
