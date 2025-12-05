package me.zhengjie.modules.forest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.modules.forest.domain.ForestResource;
import me.zhengjie.modules.forest.mapper.ForestResourceMapper;
import me.zhengjie.modules.forest.service.ForestResourceService;
import me.zhengjie.modules.forest.service.dto.ForestResourceDto;
import me.zhengjie.modules.forest.service.dto.ForestResourceQueryCriteria;
import me.zhengjie.modules.forest.service.mapstruct.ForestResourceMapperStruct;
import me.zhengjie.modules.forest.util.GeoJsonUtils;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 森林资源Service实现类
 * @author [你的名字]
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ForestResourceServiceImpl extends ServiceImpl<ForestResourceMapper, ForestResource> implements ForestResourceService {

    @Autowired
    private ForestResourceMapperStruct forestResourceMapperStruct;

    @Override
    public ForestResourceDto create(ForestResource resource) {
        // 几何数据合法性校验
        validateGeometry(resource.getGeometry());
        save(resource);
        return forestResourceMapperStruct.toDto(resource);
    }

    @Override
    public ForestResourceDto update(ForestResource resource) {
        // 几何数据合法性校验
        validateGeometry(resource.getGeometry());
        updateById(resource);
        return forestResourceMapperStruct.toDto(resource);
    }

    @Override
    public void delete(Long id) {
        removeById(id);
    }

    @Override
    public ForestResourceDto findById(Long id) {
        ForestResource resource = getById(id);
        return forestResourceMapperStruct.toDto(resource);
    }

    @Override
    public Map<String, Object> findByBbox(String bbox) {
        // 解析bbox参数，格式为minx,miny,maxx,maxy
        String[] coordinates = bbox.split(",");
        if (coordinates.length != 4) {
            throw new IllegalArgumentException("bbox参数格式错误，应为minx,miny,maxx,maxy");
        }

        double minx = Double.parseDouble(coordinates[0]);
        double miny = Double.parseDouble(coordinates[1]);
        double maxx = Double.parseDouble(coordinates[2]);
        double maxy = Double.parseDouble(coordinates[3]);

        List<ForestResource> resourcesList = baseMapper.findByBbox(minx, miny, maxx, maxy);
        return GeoJsonUtils.convertToGeoJSON(resourcesList);
    }

    @Override
    public Object queryAll(ForestResourceQueryCriteria criteria, Pageable pageable) {
        IPage<ForestResource> page = new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize());
        QueryWrapper<ForestResource> queryWrapper = QueryHelp.getQueryWrapper(criteria, ForestResource.class);
        page = baseMapper.selectPage(page, queryWrapper);
        return PageUtil.toPage(page.map(forestResourceMapperStruct::toDto));
    }

    /**
     * 验证几何数据合法性
     * @param geometry 几何数据
     */
    private void validateGeometry(Geometry geometry) {
        if (geometry == null) {
            throw new IllegalArgumentException("几何数据不能为空");
        }

        if (!GeoJsonUtils.isValidPolygon(geometry)) {
            throw new IllegalArgumentException("几何数据必须为有效的多边形");
        }
    }

}