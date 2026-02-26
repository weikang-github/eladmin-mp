/*
 *  Copyright 2019-2025 Zheng Jie
 */
package me.zhengjie.modules.schedule.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.schedule.domain.EmployeeRule;
import me.zhengjie.modules.schedule.domain.dto.EmployeeRuleQueryCriteria;
import me.zhengjie.modules.schedule.service.EmployeeRuleService;
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
@Api(tags = "排班管理：员工轮班规则")
@RequestMapping("/api/schedule/employeeRule")
public class EmployeeRuleController {

    private final EmployeeRuleService employeeRuleService;

    @ApiOperation("查询员工轮班规则")
    @GetMapping
    public ResponseEntity<PageResult<EmployeeRule>> query(EmployeeRuleQueryCriteria criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(employeeRuleService.queryAll(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("查询指定员工的轮班规则")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EmployeeRule>> queryByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(employeeRuleService.findByUserId(userId), HttpStatus.OK);
    }

    @ApiOperation("根据ID查询员工轮班规则")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeRule> queryById(@PathVariable Long id) {
        return new ResponseEntity<>(employeeRuleService.findById(id), HttpStatus.OK);
    }

    @Log("新增员工轮班规则")
    @ApiOperation("新增员工轮班规则")
    @PostMapping
    public ResponseEntity<Object> create(@Validated @RequestBody EmployeeRule resources) {
        employeeRuleService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改员工轮班规则")
    @ApiOperation("修改员工轮班规则")
    @PutMapping
    public ResponseEntity<Object> update(@Validated(EmployeeRule.Update.class) @RequestBody EmployeeRule resources) {
        employeeRuleService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除员工轮班规则")
    @ApiOperation("删除员工轮班规则")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody List<Long> ids) {
        employeeRuleService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("导出员工轮班规则数据")
    @ApiOperation("导出员工轮班规则数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, EmployeeRuleQueryCriteria criteria) throws IOException {
        employeeRuleService.download(employeeRuleService.queryAll(criteria), response);
    }
}