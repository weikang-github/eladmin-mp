/*
 *  Copyright 2019-2025 Zheng Jie
 */
package me.zhengjie.modules.schedule.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.schedule.domain.ShiftType;
import me.zhengjie.modules.schedule.domain.dto.ShiftTypeQueryCriteria;
import me.zhengjie.modules.schedule.service.ShiftTypeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "排班管理：班次类型管理")
@RequestMapping("/api/schedule/shiftType")
public class ShiftTypeController {

    private final ShiftTypeService shiftTypeService;

    @ApiOperation("查询班次类型")
    @GetMapping
    public ResponseEntity<PageResult<ShiftType>> query(ShiftTypeQueryCriteria criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(shiftTypeService.queryAll(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("查询所有启用的班次类型")
    @GetMapping("/enabled")
    public ResponseEntity<List<ShiftType>> queryEnabled() {
        return new ResponseEntity<>(shiftTypeService.findAllEnabled(), HttpStatus.OK);
    }

    @ApiOperation("根据ID查询班次类型")
    @GetMapping("/{id}")
    public ResponseEntity<ShiftType> queryById(@PathVariable Long id) {
        return new ResponseEntity<>(shiftTypeService.findById(id), HttpStatus.OK);
    }

    @Log("新增班次类型")
    @ApiOperation("新增班次类型")
    @PostMapping
    public ResponseEntity<Object> create(@Validated @RequestBody ShiftType resources) {
        shiftTypeService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改班次类型")
    @ApiOperation("修改班次类型")
    @PutMapping
    public ResponseEntity<Object> update(@Validated(ShiftType.Update.class) @RequestBody ShiftType resources) {
        shiftTypeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除班次类型")
    @ApiOperation("删除班次类型")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody List<Long> ids) {
        shiftTypeService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("导出班次类型数据")
    @ApiOperation("导出班次类型数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, ShiftTypeQueryCriteria criteria) throws IOException {
        shiftTypeService.download(shiftTypeService.queryAll(criteria), response);
    }
}
