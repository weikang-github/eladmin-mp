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
package me.zhengjie.modules.forest.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.forest.domain.ForestResource;
import me.zhengjie.modules.forest.domain.dto.ForestResourceQueryCriteria;
import me.zhengjie.modules.forest.service.ForestResourceService;
import me.zhengjie.utils.PageResult;
import org.geotools.geojson.feature.FeatureJSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.geotools.referencing.CRS;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import java.io.StringWriter;
import java.math.BigDecimal;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 森林资源Controller类
 * @author Zheng Jie
 * @date 2025-01-01
 */
@Api(tags = "森林资源管理")
@RestController
@RequestMapping("/api/forest/resources")
@RequiredArgsConstructor
public class ForestResourceController {

    private final ForestResourceService forestResourceService;

    @ApiOperation("导出森林资源数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('forest:list')")
    public void exportForestResource(HttpServletResponse response, ForestResourceQueryCriteria criteria) throws IOException {
        forestResourceService.download(forestResourceService.queryAll(criteria), response);
    }

    @ApiOperation("查询森林资源")
    @GetMapping
    @PreAuthorize("@el.check('forest:list')")
    public ResponseEntity<PageResult<ForestResource>> queryForestResource(ForestResourceQueryCriteria criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        PageResult<ForestResource> result = forestResourceService.queryAll(criteria, page);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation("根据地图视窗范围查询森林资源")
    @GetMapping(value = "/bbox")
    @PreAuthorize("@el.check('forest:list')")
    public ResponseEntity<String> queryForestResourceByBbox(@RequestParam String bbox) throws IOException {
        List<ForestResource> resources = forestResourceService.findByBbox(bbox);
        // 将结果转换为GeoJSON格式
        FeatureJSON featureJSON = new FeatureJSON();
        FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection = convertToFeatureCollection(resources);
        StringWriter writer = new StringWriter();
        featureJSON.writeFeatureCollection(featureCollection, writer);
        return new ResponseEntity<>(writer.toString(), HttpStatus.OK);
    }

    /**
     * 将ForestResource列表转换为GeoTools FeatureCollection
     * @param resources ForestResource列表
     * @return FeatureCollection
     */
    private FeatureCollection<SimpleFeatureType, SimpleFeature> convertToFeatureCollection(List<ForestResource> resources) {
        try {
            // 创建要素类型
            SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
            typeBuilder.setName("ForestResource");
            // 设置坐标参考系统（WGS84）
            CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
            typeBuilder.setCRS(crs);
            // 添加属性
            typeBuilder.add("id", Long.class);
            typeBuilder.add("forestClassNumber", String.class);
            typeBuilder.add("subClassNumber", String.class);
            typeBuilder.add("treeSpecies", String.class);
            typeBuilder.add("area", BigDecimal.class);
            typeBuilder.add("volume", BigDecimal.class);
            typeBuilder.add("geometry", Geometry.class);
            SimpleFeatureType featureType = typeBuilder.buildFeatureType();

            // 创建要素集合
            DefaultFeatureCollection featureCollection = new DefaultFeatureCollection("forestResources", featureType);
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);

            // 将ForestResource转换为Feature并添加到集合中
            for (ForestResource resource : resources) {
                featureBuilder.set("id", resource.getId());
                featureBuilder.set("forestClassNumber", resource.getForestClassNumber());
                featureBuilder.set("subClassNumber", resource.getSubClassNumber());
                featureBuilder.set("treeSpecies", resource.getTreeSpecies());
                featureBuilder.set("area", resource.getArea());
                featureBuilder.set("volume", resource.getVolume());
                featureBuilder.set("geometry", resource.getGeometry());
                SimpleFeature feature = featureBuilder.buildFeature(null);
                featureCollection.add(feature);
            }

            return featureCollection;
        } catch (Exception e) {
            throw new RuntimeException("转换为FeatureCollection失败", e);
        }
    }

    @Log("新增森林资源")
    @ApiOperation("新增森林资源")
    @PostMapping
    @PreAuthorize("@el.check('forest:add')")
    public ResponseEntity<Object> createForestResource(@Validated @RequestBody ForestResource resources) {
        forestResourceService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改森林资源")
    @ApiOperation("修改森林资源")
    @PutMapping
    @PreAuthorize("@el.check('forest:edit')")
    public ResponseEntity<Object> updateForestResource(@Validated(ForestResource.Update.class) @RequestBody ForestResource resources) {
        forestResourceService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除森林资源")
    @ApiOperation("删除森林资源")
    @DeleteMapping
    @PreAuthorize("@el.check('forest:del')")
    public ResponseEntity<Object> deleteForestResource(@RequestBody Set<Long> ids) {
        forestResourceService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}