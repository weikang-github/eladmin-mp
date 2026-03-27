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
package me.zhengjie.modules.attendance.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.attendance.domain.AttendanceGroup;
import me.zhengjie.modules.attendance.domain.AttendanceGroupDept;
import me.zhengjie.modules.attendance.domain.dto.AttendanceGroupQueryCriteria;
import me.zhengjie.modules.attendance.domain.vo.AttendanceGroupExcelVo;
import me.zhengjie.modules.attendance.mapper.AttendanceGroupDeptMapper;
import me.zhengjie.modules.attendance.mapper.AttendanceGroupMapper;
import me.zhengjie.modules.attendance.service.AttendanceGroupService;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 考勤组ServiceImpl
 * @author Zheng Jie
 * @date 2025-03-27
 */
@Service
@RequiredArgsConstructor
public class AttendanceGroupServiceImpl extends ServiceImpl<AttendanceGroupMapper, AttendanceGroup> implements AttendanceGroupService {

    private final AttendanceGroupMapper attendanceGroupMapper;
    private final AttendanceGroupDeptMapper attendanceGroupDeptMapper;

    @Override
    public PageResult<AttendanceGroup> queryAll(AttendanceGroupQueryCriteria criteria, Page<Object> page) {
        Page<AttendanceGroup> pageResult = attendanceGroupMapper.findAll(page, criteria);
        for (AttendanceGroup group : pageResult.getRecords()) {
            group.setDeptIds(attendanceGroupDeptMapper.findDeptIdsByGroupId(group.getId()));
        }
        return PageUtil.toPage(pageResult);
    }

    @Override
    public List<AttendanceGroup> queryAll(AttendanceGroupQueryCriteria criteria) {
        List<AttendanceGroup> list = attendanceGroupMapper.findAll(criteria);
        for (AttendanceGroup group : list) {
            group.setDeptIds(attendanceGroupDeptMapper.findDeptIdsByGroupId(group.getId()));
        }
        return list;
    }

    @Override
    public AttendanceGroup findById(Long id) {
        AttendanceGroup group = attendanceGroupMapper.selectById(id);
        if (group != null) {
            group.setDeptIds(attendanceGroupDeptMapper.findDeptIdsByGroupId(group.getId()));
        }
        return group;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(AttendanceGroup resources) {
        attendanceGroupMapper.insert(resources);
        if (CollectionUtil.isNotEmpty(resources.getDeptIds())) {
            for (Long deptId : resources.getDeptIds()) {
                AttendanceGroupDept groupDept = new AttendanceGroupDept();
                groupDept.setGroupId(resources.getId());
                groupDept.setDeptId(deptId);
                attendanceGroupDeptMapper.insert(groupDept);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AttendanceGroup resources) {
        AttendanceGroup attendanceGroup = attendanceGroupMapper.selectById(resources.getId());
        if (attendanceGroup == null) {
            throw new BadRequestException("考勤组不存在");
        }
        attendanceGroupMapper.updateById(resources);
        attendanceGroupDeptMapper.deleteByGroupId(resources.getId());
        if (CollectionUtil.isNotEmpty(resources.getDeptIds())) {
            for (Long deptId : resources.getDeptIds()) {
                AttendanceGroupDept groupDept = new AttendanceGroupDept();
                groupDept.setGroupId(resources.getId());
                groupDept.setDeptId(deptId);
                attendanceGroupDeptMapper.insert(groupDept);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            attendanceGroupDeptMapper.deleteByGroupId(id);
            attendanceGroupMapper.deleteById(id);
        }
    }

    @Override
    public AttendanceGroup findByUserId(Long userId) {
        return attendanceGroupMapper.findByUserId(userId);
    }

    @Override
    public List<AttendanceGroup> findByDeptId(Long deptId) {
        return attendanceGroupMapper.findByDeptId(deptId);
    }

    @Override
    public void download(List<AttendanceGroup> list, HttpServletResponse response) throws IOException {
        List<AttendanceGroupExcelVo> excelVoList = list.stream().map(group -> {
            AttendanceGroupExcelVo vo = new AttendanceGroupExcelVo();
            BeanUtils.copyProperties(group, vo);
            vo.setEnabled(group.getEnabled() ? "是" : "否");
            return vo;
        }).collect(Collectors.toList());

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("考勤组", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), AttendanceGroupExcelVo.class)
                .sheet("考勤组")
                .doWrite(excelVoList);
    }
}
