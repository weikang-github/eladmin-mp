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
import me.zhengjie.modules.schedule.domain.ScheduleRule;
import me.zhengjie.modules.schedule.domain.dto.ScheduleRuleQueryCriteria;
import me.zhengjie.modules.schedule.service.ScheduleRuleService;
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
@Api(tags = "排班：排班规则管理")
@RequestMapping("/api/schedule/rules")
public class ScheduleRuleController {

    private final ScheduleRuleService scheduleRuleService;
    private static final String ENTITY_NAME = "scheduleRule";

    @ApiOperation("查询排班规则")
    @GetMapping
    @PreAuthorize("@el.check('scheduleRules:list')")
    public ResponseEntity<PageResult<ScheduleRule>> queryScheduleRule(ScheduleRuleQueryCriteria criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(scheduleRuleService.queryAll(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("查询所有启用的排班规则")
    @GetMapping("/all")
    @PreAuthorize("@el.check('scheduleRules:list')")
    public ResponseEntity<List<ScheduleRule>> queryAllEnabled() {
        return new ResponseEntity<>(scheduleRuleService.findAllEnabled(), HttpStatus.OK);
    }

    @ApiOperation("根据类型查询排班规则")
    @GetMapping("/type/{type}")
    @PreAuthorize("@el.check('scheduleRules:list')")
    public ResponseEntity<List<ScheduleRule>> queryByType(@PathVariable String type) {
        return new ResponseEntity<>(scheduleRuleService.findEnabledByType(type), HttpStatus.OK);
    }

    @ApiOperation("导出排班规则数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('scheduleRules:list')")
    public void exportScheduleRule(HttpServletResponse response, ScheduleRuleQueryCriteria criteria) throws IOException {
        scheduleRuleService.download(scheduleRuleService.queryAll(criteria), response);
    }

    @ApiOperation("获取单个排班规则")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('scheduleRules:list')")
    public ResponseEntity<ScheduleRule> findScheduleRuleById(@PathVariable Long id) {
        return new ResponseEntity<>(scheduleRuleService.findById(id), HttpStatus.OK);
    }

    @Log("新增排班规则")
    @ApiOperation("新增排班规则")
    @PostMapping
    @PreAuthorize("@el.check('scheduleRules:add')")
    public ResponseEntity<Object> createScheduleRule(@Validated @RequestBody ScheduleRule resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        scheduleRuleService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改排班规则")
    @ApiOperation("修改排班规则")
    @PutMapping
    @PreAuthorize("@el.check('scheduleRules:edit')")
    public ResponseEntity<Object> updateScheduleRule(@Validated(ScheduleRule.Update.class) @RequestBody ScheduleRule resources) {
        scheduleRuleService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除排班规则")
    @ApiOperation("删除排班规则")
    @DeleteMapping
    @PreAuthorize("@el.check('scheduleRules:del')")
    public ResponseEntity<Object> deleteScheduleRule(@RequestBody Set<Long> ids) {
        scheduleRuleService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("验证排班规则")
    @ApiOperation("验证排班规则")
    @PostMapping("/validate")
    @PreAuthorize("@el.check('scheduleRules:add')")
    public ResponseEntity<Object> validateRule(@RequestBody ScheduleRule resources) {
        scheduleRuleService.validateRule(resources);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
