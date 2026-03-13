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
import me.zhengjie.modules.schedule.domain.Shift;
import me.zhengjie.modules.schedule.domain.dto.ShiftQueryCriteria;
import me.zhengjie.modules.schedule.service.ShiftService;
import me.zhengjie.utils.PageResult;
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
 * @author Zheng Jie
 * @date 2025-04-10
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "排班：班次管理")
@RequestMapping("/api/schedule/shifts")
public class ShiftController {

    private final ShiftService shiftService;
    private static final String ENTITY_NAME = "shift";

    @ApiOperation("查询班次")
    @GetMapping
    @PreAuthorize("@el.check('shifts:list')")
    public ResponseEntity<PageResult<Shift>> queryShift(ShiftQueryCriteria criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(shiftService.queryAll(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("查询所有启用的班次")
    @GetMapping("/all")
    @PreAuthorize("@el.check('shifts:list')")
    public ResponseEntity<List<Shift>> queryAllShift() {
        return new ResponseEntity<>(shiftService.findByEnabled(true), HttpStatus.OK);
    }

    @ApiOperation("根据类型查询班次")
    @GetMapping("/type/{type}")
    @PreAuthorize("@el.check('shifts:list')")
    public ResponseEntity<List<Shift>> queryByType(@PathVariable String type) {
        return new ResponseEntity<>(shiftService.findByType(type), HttpStatus.OK);
    }

    @ApiOperation("导出班次数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('shifts:list')")
    public void exportShift(HttpServletResponse response, ShiftQueryCriteria criteria) throws IOException {
        shiftService.download(shiftService.queryAll(criteria), response);
    }

    @ApiOperation("获取单个班次")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('shifts:list')")
    public ResponseEntity<Shift> findShiftById(@PathVariable Long id) {
        return new ResponseEntity<>(shiftService.findById(id), HttpStatus.OK);
    }

    @Log("新增班次")
    @ApiOperation("新增班次")
    @PostMapping
    @PreAuthorize("@el.check('shifts:add')")
    public ResponseEntity<Object> createShift(@Validated @RequestBody Shift resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        shiftService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改班次")
    @ApiOperation("修改班次")
    @PutMapping
    @PreAuthorize("@el.check('shifts:edit')")
    public ResponseEntity<Object> updateShift(@Validated(Shift.Update.class) @RequestBody Shift resources) {
        shiftService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除班次")
    @ApiOperation("删除班次")
    @DeleteMapping
    @PreAuthorize("@el.check('shifts:del')")
    public ResponseEntity<Object> deleteShift(@RequestBody Set<Long> ids) {
        shiftService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
