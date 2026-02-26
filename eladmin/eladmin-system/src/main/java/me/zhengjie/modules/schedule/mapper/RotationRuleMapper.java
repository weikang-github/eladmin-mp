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
import me.zhengjie.modules.schedule.domain.RotationRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 轮班规则Mapper
 * @author Zheng Jie
 * @date 2026-02-26
 */
@Mapper
public interface RotationRuleMapper extends BaseMapper<RotationRule> {

    /**
     * 根据编码查询
     */
    @Select("SELECT * FROM schedule_rotation_rule WHERE code = #{code}")
    RotationRule findByCode(@Param("code") String code);

    /**
     * 查询所有启用的轮班规则
     */
    @Select("SELECT * FROM schedule_rotation_rule WHERE is_enabled = 1")
    List<RotationRule> findAllEnabled();
}
