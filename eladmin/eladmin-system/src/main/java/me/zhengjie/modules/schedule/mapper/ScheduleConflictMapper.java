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
import me.zhengjie.modules.schedule.domain.ScheduleConflict;
import me.zhengjie.modules.schedule.domain.dto.ScheduleConflictQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 排班冲突Mapper
 * @author Zheng Jie
 * @date 2026-02-26
 */
@Mapper
public interface ScheduleConflictMapper extends BaseMapper<ScheduleConflict> {

    /**
     * 条件查询
     */
    List<ScheduleConflict> findAll(@Param("criteria") ScheduleConflictQueryCriteria criteria);

    /**
     * 查询未解决的冲突
     */
    List<ScheduleConflict> findUnresolved(@Param("userId") Long userId);
}
