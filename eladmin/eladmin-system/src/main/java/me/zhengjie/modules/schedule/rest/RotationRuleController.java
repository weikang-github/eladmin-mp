/*
 *  Copyright 2019-2025 Zheng Jie
 */
package me.zhengjie.modules.schedule.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.schedule.domain.RotationRule;
import me.zhengjie.modules.schedule.domain.dto.RotationRuleQueryCriteria;
import me.zhengjie.modules.schedule.service.RotationRuleService;
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
@Api(tags = "排班管理：轮班规则管理")
@RequestMapping("/api/schedule/rotationRule")
public class RotationRuleController {

    private final RotationRuleService rotationRuleService;

    @ApiOperation("查询轮班规则")
    @GetMapping
    public ResponseEntity<PageResult<RotationRule>> query(RotationRuleQueryCriteria criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(rotationRuleService.queryAll(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("查询所有轮班规则")
    @GetMapping("/all")
    public ResponseEntity<List<RotationRule>> queryAll() {
        return new ResponseEntity<>(rotationRuleService.findAll(), HttpStatus.OK);
    }

    @ApiOperation("根据ID查询轮班规则")
    @GetMapping("/{id}")
    public ResponseEntity<RotationRule> queryById(@PathVariable Long id) {
        return new ResponseEntity<>(rotationRuleService.findById(id), HttpStatus.OK);
    }

    @Log("新增轮班规则")
    @ApiOperation("新增轮班规则")
    @PostMapping
    public ResponseEntity<Object> create(@Validated @RequestBody RotationRule resources) {
        rotationRuleService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改轮班规则")
    @ApiOperation("修改轮班规则")
    @PutMapping
    public ResponseEntity<Object> update(@Validated(RotationRule.Update.class) @RequestBody RotationRule resources) {
        rotationRuleService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除轮班规则")
    @ApiOperation("删除轮班规则")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody List<Long> ids) {
        rotationRuleService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("导出轮班规则数据")
    @ApiOperation("导出轮班规则数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, RotationRuleQueryCriteria criteria) throws IOException {
        rotationRuleService.download(rotationRuleService.queryAll(criteria), response);
    }
}