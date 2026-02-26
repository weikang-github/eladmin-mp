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
package me.zhengjie.modules.schedule.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.schedule.domain.ScheduleConflict;
import me.zhengjie.modules.schedule.domain.dto.ScheduleConflictQueryCriteria;
import me.zhengjie.modules.schedule.mapper.ScheduleConflictMapper;
import me.zhengjie.modules.schedule.service.ScheduleConflictService;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 排班冲突Service实现
 * @author Zheng Jie
 * @date 2026-02-26
 */
@Service
@RequiredArgsConstructor
public class ScheduleConflictServiceImpl extends ServiceImpl<ScheduleConflictMapper, ScheduleConflict> implements ScheduleConflictService {

    private final ScheduleConflictMapper scheduleConflictMapper;

    @Override
    public PageResult<ScheduleConflict> queryAll(ScheduleConflictQueryCriteria criteria, Page<Object> page) {
        List<ScheduleConflict> list = scheduleConflictMapper.findAll(criteria);
        return PageUtil.toPage(list, (long) list.size());
    }

    @Override
    public List<ScheduleConflict> queryAll(ScheduleConflictQueryCriteria criteria) {
        return scheduleConflictMapper.findAll(criteria);
    }

    @Override
    public ScheduleConflict findById(Long id) {
        return getById(id);
    }

    @Override
    public List<ScheduleConflict> findUnresolved(Long userId) {
        return scheduleConflictMapper.findUnresolved(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ScheduleConflict resources) {
        save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resolve(Long id, String remark) {
        ScheduleConflict conflict = getById(id);
        if (conflict != null) {
            conflict.setStatus("RESOLVED");
            conflict.setResolvedBy(SecurityUtils.getCurrentUsername());
            conflict.setResolvedTime(LocalDateTime.now());
            conflict.setRemark(remark);
            updateById(conflict);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ignore(Long id, String remark) {
        ScheduleConflict conflict = getById(id);
        if (conflict != null) {
            conflict.setStatus("IGNORED");
            conflict.setResolvedBy(SecurityUtils.getCurrentUsername());
            conflict.setResolvedTime(LocalDateTime.now());
            conflict.setRemark(remark);
            updateById(conflict);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids) {
        removeByIds(ids);
    }
}
