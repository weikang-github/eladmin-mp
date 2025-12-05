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
package me.zhengjie.modules.forest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.forest.domain.ForestResource;
import me.zhengjie.modules.forest.domain.dto.ForestResourceQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 森林资源Mapper接口
 * @author Zheng Jie
 * @date 2025-01-01
 */
@Mapper
public interface ForestResourceMapper extends BaseMapper<ForestResource> {

    /**
     * 分页查询森林资源
     * @param criteria 查询条件
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<ForestResource> findAll(@Param("criteria") ForestResourceQueryCriteria criteria, Page<Object> page);

    /**
     * 查询所有森林资源
     * @param criteria 查询条件
     * @return 森林资源列表
     */
    List<ForestResource> findAll(@Param("criteria") ForestResourceQueryCriteria criteria);

    /**
     * 根据地图视窗范围查询森林资源
     * @param bbox 地图视窗范围，格式：minX,minY,maxX,maxY
     * @return 森林资源列表
     */
    @Select("SELECT * FROM forest_resource WHERE ST_Within(geometry, ST_MakeEnvelope(#{bbox}, 4326))")
    List<ForestResource> findByBbox(@Param("bbox") String bbox);
}