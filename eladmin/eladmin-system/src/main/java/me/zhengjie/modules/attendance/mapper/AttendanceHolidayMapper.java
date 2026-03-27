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
package me.zhengjie.modules.attendance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.attendance.domain.AttendanceHoliday;
import me.zhengjie.modules.attendance.domain.dto.AttendanceHolidayQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

/**
 * 考勤节假日Mapper
 * @author Zheng Jie
 * @date 2025-03-27
 */
@Mapper
public interface AttendanceHolidayMapper extends BaseMapper<AttendanceHoliday> {

    /**
     * 分页查询节假日
     * @param page 分页对象
     * @param criteria 查询条件
     * @return 节假日分页结果
     */
    IPage<AttendanceHoliday> findAll(Page<AttendanceHoliday> page, @Param("criteria") AttendanceHolidayQueryCriteria criteria);

    /**
     * 查询所有节假日
     * @param criteria 查询条件
     * @return 节假日列表
     */
    List<AttendanceHoliday> findAll(@Param("criteria") AttendanceHolidayQueryCriteria criteria);

    /**
     * 查询日期是否为节假日
     * @param holidayDate 日期
     * @return 节假日信息
     */
    AttendanceHoliday findByDate(@Param("holidayDate") Date holidayDate);

    /**
     * 查询某年的节假日
     * @param year 年份
     * @return 节假日列表
     */
    List<AttendanceHoliday> findByYear(@Param("year") Integer year);
}
