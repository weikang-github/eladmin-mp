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
import me.zhengjie.modules.attendance.domain.AttendanceGroupDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Set;

/**
 * 考勤组部门关联Mapper
 * @author Zheng Jie
 * @date 2025-03-27
 */
@Mapper
public interface AttendanceGroupDeptMapper extends BaseMapper<AttendanceGroupDept> {

    /**
     * 根据考勤组ID删除关联
     * @param groupId 考勤组ID
     */
    void deleteByGroupId(@Param("groupId") Long groupId);

    /**
     * 根据考勤组ID查询部门ID列表
     * @param groupId 考勤组ID
     * @return 部门ID列表
     */
    Set<Long> findDeptIdsByGroupId(@Param("groupId") Long groupId);

    /**
     * 根据部门ID查询考勤组ID列表
     * @param deptId 部门ID
     * @return 考勤组ID列表
     */
    List<Long> findGroupIdsByDeptId(@Param("deptId") Long deptId);
}
