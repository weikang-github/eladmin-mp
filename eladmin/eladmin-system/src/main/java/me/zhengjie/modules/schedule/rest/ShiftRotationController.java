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
package me.zhengjie.modules.schedule.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.schedule.domain.ShiftRotation;
import me.zhengjie.modules.schedule.domain.dto.ShiftRotationQueryCriteria;
import me.zhengjie.modules.schedule.service.ShiftRotationService;
import me.zhengjie.utils.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Zheng Jie
 * @date 2025-04-10
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "排班：轮班规则管理")
@RequestMapping("/api/schedule/rotations")
public class ShiftRotationController {

    private final ShiftRotationService shiftRotationService;
    private static final String ENTITY_NAME = "shiftRotation";

    @ApiOperation("查询轮班规则")
    @GetMapping
    @PreAuthorize("@el.check('rotations:list')")
    public ResponseEntity<PageResult<ShiftRotation>> queryShiftRotation(ShiftRotationQueryCriteria criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(shiftRotationService.queryAll(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("查询所有启用的轮班规则")
    @GetMapping("/all")
    @PreAuthorize("@el.check('rotations:list')")
    public ResponseEntity<List<ShiftRotation>> queryAllShiftRotation() {
        return new ResponseEntity<>(shiftRotationService.findByEnabled(true), HttpStatus.OK);
    }

    @ApiOperation("导出轮班规则数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rotations:list')")
    public void exportShiftRotation(HttpServletResponse response, ShiftRotationQueryCriteria criteria) throws IOException {
        shiftRotationService.download(shiftRotationService.queryAll(criteria), response);
    }

    @ApiOperation("获取单个轮班规则")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('rotations:list')")
    public ResponseEntity<ShiftRotation> findShiftRotationById(@PathVariable Long id) {
        return new ResponseEntity<>(shiftRotationService.findById(id), HttpStatus.OK);
    }

    @Log("新增轮班规则")
    @ApiOperation("新增轮班规则")
    @PostMapping
    @PreAuthorize("@el.check('rotations:add')")
    public ResponseEntity<Object> createShiftRotation(@Validated @RequestBody ShiftRotation resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        shiftRotationService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改轮班规则")
    @ApiOperation("修改轮班规则")
    @PutMapping
    @PreAuthorize("@el.check('rotations:edit')")
    public ResponseEntity<Object> updateShiftRotation(@Validated(ShiftRotation.Update.class) @RequestBody ShiftRotation resources) {
        shiftRotationService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除轮班规则")
    @ApiOperation("删除轮班规则")
    @DeleteMapping
    @PreAuthorize("@el.check('rotations:del')")
    public ResponseEntity<Object> deleteShiftRotation(@RequestBody Set<Long> ids) {
        shiftRotationService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("根据轮班规则生成排班")
    @ApiOperation("根据轮班规则生成排班")
    @PostMapping("/{id}/generate")
    @PreAuthorize("@el.check('rotations:generate')")
    public ResponseEntity<Map<String, Object>> generateSchedule(
            @PathVariable Long id,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return new ResponseEntity<>(shiftRotationService.generateSchedule(id, startDate, endDate), HttpStatus.OK);
    }

    @ApiOperation("验证轮班规则")
    @PostMapping("/validate")
    @PreAuthorize("@el.check('rotations:add','rotations:edit')")
    public ResponseEntity<Map<String, Object>> validateRotation(@RequestBody ShiftRotation resources) {
        return new ResponseEntity<>(shiftRotationService.validateRotation(resources), HttpStatus.OK);
    }
}
