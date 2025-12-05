package me.zhengjie.modules.forest.service;

import me.zhengjie.modules.forest.domain.ForestResource;
import me.zhengjie.modules.forest.service.dto.ForestResourceDto;
import me.zhengjie.modules.forest.service.dto.ForestResourceQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 森林资源Service接口
 * @author [你的名字]
 */
public interface ForestResourceService {

    /**
     * 新增资源
     * @param resource 森林资源实体
     * @return 新增后的森林资源
     */
    ForestResourceDto create(ForestResource resource);

    /**
     * 更新资源
     * @param resource 森林资源实体
     * @return 更新后的森林资源
     */
    ForestResourceDto update(ForestResource resource);

    /**
     * 按ID删除资源
     * @param id 资源ID
     */
    void delete(Long id);

    /**
     * 按ID查询资源
     * @param id 资源ID
     * @return 森林资源DTO
     */
    ForestResourceDto findById(Long id);

    /**
     * 按地图视窗范围查询资源
     * @param bbox 边界框，格式：minx,miny,maxx,maxy
     * @return GeoJSON格式的森林资源数据
     */
    Map<String, Object> findByBbox(String bbox);

    /**
     * 分页查询资源
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return 分页结果
     */
    Object queryAll(ForestResourceQueryCriteria criteria, Pageable pageable);

}