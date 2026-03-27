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
import me.zhengjie.modules.attendance.domain.AttendanceGroup;
import me.zhengjie.modules.attendance.domain.dto.AttendanceGroupQueryCriteria;
import me.zhengjie.utils.PageResult;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 考勤组Service
 * @author Zheng Jie
 * @date 2025-03-27
 */
public interface AttendanceGroupService extends IService<AttendanceGroup> {

    /**
     * 分页查询考勤组
     * @param criteria 查询条件
     * @param page 分页对象
     * @return 考勤组分页结果
     */
    PageResult<AttendanceGroup> queryAll(AttendanceGroupQueryCriteria criteria, Page<Object> page);

    /**
     * 查询所有考勤组
     * @param criteria 查询条件
     * @return 考勤组列表
     */
    List<AttendanceGroup> queryAll(AttendanceGroupQueryCriteria criteria);

    /**
     * 根据ID查询考勤组
     * @param id 考勤组ID
     * @return 考勤组
     */
    AttendanceGroup findById(Long id);

    /**
     * 创建考勤组
     * @param resources 考勤组信息
     */
    void create(AttendanceGroup resources);

    /**
     * 编辑考勤组
     * @param resources 考勤组信息
     */
    void update(AttendanceGroup resources);

    /**
     * 删除考勤组
     * @param ids 考勤组ID集合
     */
    void delete(Set<Long> ids);

    /**
     * 根据用户ID查询考勤组
     * @param userId 用户ID
     * @return 考勤组
     */
    AttendanceGroup findByUserId(Long userId);

    /**
     * 根据部门ID查询考勤组
     * @param deptId 部门ID
     * @return 考勤组列表
     */
    List<AttendanceGroup> findByDeptId(Long deptId);

    /**
     * 导出考勤组数据
     * @param list 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<AttendanceGroup> list, HttpServletResponse response) throws IOException;
}
