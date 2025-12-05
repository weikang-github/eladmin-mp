package me.zhengjie.modules.forest.controller;

import me.zhengjie.annotation.Log;
import me.zhengjie.modules.forest.domain.ForestResource;
import me.zhengjie.modules.forest.service.ForestResourceService;
import me.zhengjie.modules.forest.service.dto.ForestResourceDto;
import me.zhengjie.modules.forest.service.dto.ForestResourceQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import me.zhengjie.modules.forest.util.GeoJsonUtils;

/**
 * 森林资源Controller类
 * @author [你的名字]
 */
@RestController
@RequestMapping("/api/forest/resources")
public class ForestResourceController {

    @Autowired
    private ForestResourceService forestResourceService;

    /**
     * 新增资源
     * @param resource 森林资源实体
     * @return 新增后的森林资源DTO
     */
    @Log("新增森林资源")
    @PostMapping
    @PreAuthorize("@el.check('forest:resource:add')")
    public ResponseEntity<ForestResourceDto> create(@Validated @RequestBody ForestResource resource) {
        return new ResponseEntity<>(forestResourceService.create(resource), HttpStatus.CREATED);
    }

    /**
     * 更新资源
     * @param id 资源ID
     * @param resource 森林资源实体
     * @return 更新后的森林资源DTO
     */
    @Log("更新森林资源")
    @PutMapping("/{id}")
    @PreAuthorize("@el.check('forest:resource:edit')")
    public ResponseEntity<ForestResourceDto> update(@PathVariable Long id, @Validated @RequestBody ForestResource resource) {
        resource.setId(id);
        return new ResponseEntity<>(forestResourceService.update(resource), HttpStatus.OK);
    }

    /**
     * 按ID删除资源
     * @param id 资源ID
     * @return 响应实体
     */
    @Log("删除森林资源")
    @DeleteMapping("/{id}")
    @PreAuthorize("@el.check('forest:resource:del')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        forestResourceService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 按ID查询资源
     * @param id 资源ID
     * @return 森林资源DTO
     */
    @GetMapping("/{id}")
    @PreAuthorize("@el.check('forest:resource:list')")
    public ResponseEntity<ForestResourceDto> findById(@PathVariable Long id) {
        return new ResponseEntity<>(forestResourceService.findById(id), HttpStatus.OK);
    }

    /**
     * 按地图视窗范围查询资源
     * @param bbox 边界框，格式：minx,miny,maxx,maxy
     * @return 森林资源列表（GeoJSON格式）
     */
    @GetMapping("/bbox")
    @PreAuthorize("@el.check('forest:resource:list')")
    public ResponseEntity<Map<String, Object>> findByBbox(@RequestParam String bbox) {
        Map<String, Object> geoJson = forestResourceService.findByBbox(bbox);
        return new ResponseEntity<>(geoJson, HttpStatus.OK);
    }

    /**
     * 分页查询资源
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return 分页结果
     */
    @GetMapping
    @PreAuthorize("@el.check('forest:resource:list')")
    public ResponseEntity<Object> queryAll(ForestResourceQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(forestResourceService.queryAll(criteria, pageable), HttpStatus.OK);
    }

}