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
import me.zhengjie.modules.schedule.domain.Shift;
import me.zhengjie.modules.schedule.domain.dto.ShiftQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author Zheng Jie
 * @date 2025-04-10
 */
@Mapper
public interface ShiftMapper extends BaseMapper<Shift> {

    Long countAll(@Param("criteria") ShiftQueryCriteria criteria);

    List<Shift> findAll(@Param("criteria") ShiftQueryCriteria criteria);

    IPage<Shift> findAll(@Param("criteria") ShiftQueryCriteria criteria, Page<Object> page);

    Shift findByCode(@Param("code") String code);

    List<Shift> findByType(@Param("type") String type);

    List<Shift> findByEnabled(@Param("enabled") Boolean enabled);
}
