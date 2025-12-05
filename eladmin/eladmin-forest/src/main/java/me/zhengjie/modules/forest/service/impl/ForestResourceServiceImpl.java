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
package me.zhengjie.modules.forest.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.forest.domain.ForestResource;
import me.zhengjie.modules.forest.domain.dto.ForestResourceQueryCriteria;
import me.zhengjie.modules.forest.mapper.ForestResourceMapper;
import me.zhengjie.modules.forest.service.ForestResourceService;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils .QueryHelp;
import me.zhengjie.utils.FileUtil;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 森林资源Service实现类
 * @author Zheng Jie
 * @date 2025-01-01
 */
@Service
@RequiredArgsConstructor
public class ForestResourceServiceImpl extends ServiceImpl<ForestResourceMapper, ForestResource> implements ForestResourceService {

    private final ForestResourceMapper forestResourceMapper;

    @Override
    public ForestResource findById(Long id) {
        return forestResourceMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ForestResource resources) {
        // 验证几何数据是否为多边形
        validateGeometry(resources.getGeometry());
        forestResourceMapper.insert(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ForestResource resources) {
        // 验证几何数据是否为多边形
        validateGeometry(resources.getGeometry());
        forestResourceMapper.updateById(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        forestResourceMapper.deleteBatchIds(ids);
    }

    @Override
    public PageResult<ForestResource> queryAll(ForestResourceQueryCriteria criteria, Page<Object> page) {
        IPage<ForestResource> forestResourcePage = forestResourceMapper.findAll(criteria, page);
        return new PageResult<>(forestResourcePage.getRecords(), forestResourcePage.getTotal());
    }

    @Override
    public List<ForestResource> queryAll(ForestResourceQueryCriteria criteria) {
        return forestResourceMapper.findAll(criteria);
    }

    @Override
    public List<ForestResource> findByBbox(String bbox) {
        return forestResourceMapper.findByBbox(bbox);
    }

    @Override
    public void download(List<ForestResource> resources, HttpServletResponse response) throws IOException {
        FileUtil.downloadExcel(resources, response);
    }

    /**
     * 验证几何数据是否为多边形
     * @param geometry 几何数据
     */
    private void validateGeometry(Geometry geometry) {
        if (geometry == null) {
            throw new IllegalArgumentException("几何数据不能为空");
        }
        if (!(geometry instanceof Polygon)) {
            throw new IllegalArgumentException("几何数据必须是多边形");
        }
        if (!geometry.isValid()) {
            throw new IllegalArgumentException("几何数据无效");
        }
    }
}