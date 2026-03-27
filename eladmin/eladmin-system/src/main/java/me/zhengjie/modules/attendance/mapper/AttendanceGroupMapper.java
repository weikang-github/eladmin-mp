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
import me.zhengjie.modules.attendance.domain.AttendanceGroup;
import me.zhengjie.modules.attendance.domain.dto.AttendanceGroupQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Set;

/**
 * 考勤组Mapper
 * @author Zheng Jie
 * @date 2025-03-27
 */
@Mapper
public interface AttendanceGroupMapper extends BaseMapper<AttendanceGroup> {

    /**
     * 分页查询考勤组
     * @param page 分页对象
     * @param criteria 查询条件
     * @return 考勤组分页结果
     */
    IPage<AttendanceGroup> findAll(Page<AttendanceGroup> page, @Param("criteria") AttendanceGroupQueryCriteria criteria);

    /**
     * 查询所有考勤组
     * @param criteria 查询条件
     * @return 考勤组列表
     */
    List<AttendanceGroup> findAll(@Param("criteria") AttendanceGroupQueryCriteria criteria);

    /**
     * 根据用户ID查询考勤组
     * @param userId 用户ID
     * @return 考勤组
     */
    AttendanceGroup findByUserId(@Param("userId") Long userId);

    /**
     * 根据部门ID查询考勤组
     * @param deptId 部门ID
     * @return 考勤组列表
     */
    List<AttendanceGroup> findByDeptId(@Param("deptId") Long deptId);
}
