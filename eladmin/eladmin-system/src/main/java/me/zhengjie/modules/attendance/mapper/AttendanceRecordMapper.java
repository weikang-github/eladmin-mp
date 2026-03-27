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
import me.zhengjie.modules.attendance.domain.AttendanceRecord;
import me.zhengjie.modules.attendance.domain.dto.AttendanceRecordQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

/**
 * 考勤记录Mapper
 * @author Zheng Jie
 * @date 2025-03-27
 */
@Mapper
public interface AttendanceRecordMapper extends BaseMapper<AttendanceRecord> {

    /**
     * 分页查询考勤记录
     * @param page 分页对象
     * @param criteria 查询条件
     * @return 考勤记录分页结果
     */
    IPage<AttendanceRecord> findAll(Page<AttendanceRecord> page, @Param("criteria") AttendanceRecordQueryCriteria criteria);

    /**
     * 查询所有考勤记录
     * @param criteria 查询条件
     * @return 考勤记录列表
     */
    List<AttendanceRecord> findAll(@Param("criteria") AttendanceRecordQueryCriteria criteria);

    /**
     * 查询用户某天的考勤记录
     * @param userId 用户ID
     * @param attendanceDate 考勤日期
     * @return 考勤记录
     */
    AttendanceRecord findByUserAndDate(@Param("userId") Long userId, @Param("attendanceDate") Date attendanceDate);

    /**
     * 查询用户某月的考勤记录
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 考勤记录列表
     */
    List<AttendanceRecord> findByUserAndDateRange(@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 查询部门某月的考勤记录
     * @param deptId 部门ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 考勤记录列表
     */
    List<AttendanceRecord> findByDeptAndDateRange(@Param("deptId") Long deptId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
