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
import me.zhengjie.modules.schedule.domain.ScheduleConflict;
import me.zhengjie.modules.schedule.domain.dto.ScheduleConflictQueryCriteria;
import me.zhengjie.utils.PageResult;

import java.util.List;

/**
 * 排班冲突Service
 * @author Zheng Jie
 * @date 2026-02-26
 */
public interface ScheduleConflictService extends IService<ScheduleConflict> {

    /**
     * 分页查询
     */
    PageResult<ScheduleConflict> queryAll(ScheduleConflictQueryCriteria criteria, Page<Object> page);

    /**
     * 查询全部
     */
    List<ScheduleConflict> queryAll(ScheduleConflictQueryCriteria criteria);

    /**
     * 根据ID查询
     */
    ScheduleConflict findById(Long id);

    /**
     * 查询未解决的冲突
     */
    List<ScheduleConflict> findUnresolved(Long userId);

    /**
     * 创建冲突记录
     */
    void create(ScheduleConflict resources);

    /**
     * 解决冲突
     */
    void resolve(Long id, String remark);

    /**
     * 忽略冲突
     */
    void ignore(Long id, String remark);

    /**
     * 删除
     */
    void delete(List<Long> ids);
}
