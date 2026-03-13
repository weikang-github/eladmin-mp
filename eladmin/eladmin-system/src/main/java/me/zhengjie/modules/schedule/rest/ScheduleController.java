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
import me.zhengjie.modules.schedule.domain.Schedule;
import me.zhengjie.modules.schedule.domain.dto.AutoScheduleDTO;
import me.zhengjie.modules.schedule.domain.dto.BatchScheduleDTO;
import me.zhengjie.modules.schedule.domain.dto.ScheduleQueryCriteria;
import me.zhengjie.modules.schedule.service.ScheduleService;
import me.zhengjie.utils.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Zheng Jie
 * @date 2025-04-10
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "排班：排班管理")
@RequestMapping("/api/schedule/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private static final String ENTITY_NAME = "schedule";

    @ApiOperation("查询排班")
    @GetMapping
    @PreAuthorize("@el.check('schedules:list')")
    public ResponseEntity<PageResult<Schedule>> querySchedule(ScheduleQueryCriteria criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(scheduleService.queryAll(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("查询排班日历视图")
    @GetMapping("/calendar")
    @PreAuthorize("@el.check('schedules:list')")
    public ResponseEntity<Map<String, Object>> getCalendarView(ScheduleQueryCriteria criteria) {
        return new ResponseEntity<>(scheduleService.getCalendarView(criteria), HttpStatus.OK);
    }

    @ApiOperation("查询排班统计")
    @GetMapping("/statistics")
    @PreAuthorize("@el.check('schedules:list')")
    public ResponseEntity<Map<String, Object>> getStatistics(ScheduleQueryCriteria criteria) {
        return new ResponseEntity<>(scheduleService.getStatistics(criteria), HttpStatus.OK);
    }

    @ApiOperation("根据用户和日期范围查询排班")
    @GetMapping("/user/{userId}")
    @PreAuthorize("@el.check('schedules:list')")
    public ResponseEntity<List<Schedule>> findByUserIdAndDateRange(
            @PathVariable Long userId,
            @RequestParam Date startDate,
            @RequestParam Date endDate) {
        return new ResponseEntity<>(scheduleService.findByUserIdAndDateRange(userId, startDate, endDate), HttpStatus.OK);
    }

    @ApiOperation("导出排班数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('schedules:list')")
    public void exportSchedule(HttpServletResponse response, ScheduleQueryCriteria criteria) throws IOException {
        scheduleService.download(scheduleService.queryAll(criteria), response);
    }

    @ApiOperation("获取单个排班")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('schedules:list')")
    public ResponseEntity<Schedule> findScheduleById(@PathVariable Long id) {
        return new ResponseEntity<>(scheduleService.findById(id), HttpStatus.OK);
    }

    @ApiOperation("检测排班冲突")
    @GetMapping("/check-conflict")
    @PreAuthorize("@el.check('schedules:add')")
    public ResponseEntity<List<Schedule>> checkConflict(
            @RequestParam Long userId,
            @RequestParam Date scheduleDate,
            @RequestParam Long shiftId,
            @RequestParam(required = false) Long excludeScheduleId) {
        return new ResponseEntity<>(scheduleService.checkConflict(userId, scheduleDate, shiftId, excludeScheduleId), HttpStatus.OK);
    }

    @Log("新增排班")
    @ApiOperation("新增排班")
    @PostMapping
    @PreAuthorize("@el.check('schedules:add')")
    public ResponseEntity<Object> createSchedule(@Validated @RequestBody Schedule resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        scheduleService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("批量排班")
    @ApiOperation("批量排班")
    @PostMapping("/batch")
    @PreAuthorize("@el.check('schedules:add')")
    public ResponseEntity<Map<String, Object>> batchSchedule(@Validated @RequestBody BatchScheduleDTO batchScheduleDTO) {
        return new ResponseEntity<>(scheduleService.batchSchedule(batchScheduleDTO), HttpStatus.OK);
    }

    @Log("自动生成月度排班")
    @ApiOperation("自动生成月度排班")
    @PostMapping("/auto-generate")
    @PreAuthorize("@el.check('schedules:add')")
    public ResponseEntity<Map<String, Object>> autoGenerateMonthlySchedule(@Validated @RequestBody AutoScheduleDTO autoScheduleDTO) {
        return new ResponseEntity<>(scheduleService.autoGenerateMonthlySchedule(autoScheduleDTO), HttpStatus.OK);
    }

    @Log("检测并标记冲突")
    @ApiOperation("检测并标记冲突")
    @PostMapping("/detect-conflicts")
    @PreAuthorize("@el.check('schedules:edit')")
    public ResponseEntity<Map<String, Object>> detectAndMarkConflicts(
            @RequestParam Date startDate,
            @RequestParam Date endDate) {
        int count = scheduleService.detectAndMarkConflicts(startDate, endDate);
        return new ResponseEntity<>(Map.of("conflictCount", count), HttpStatus.OK);
    }

    @Log("修改排班")
    @ApiOperation("修改排班")
    @PutMapping
    @PreAuthorize("@el.check('schedules:edit')")
    public ResponseEntity<Object> updateSchedule(@Validated(Schedule.Update.class) @RequestBody Schedule resources) {
        scheduleService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除排班")
    @ApiOperation("删除排班")
    @DeleteMapping
    @PreAuthorize("@el.check('schedules:del')")
    public ResponseEntity<Object> deleteSchedule(@RequestBody Set<Long> ids) {
        scheduleService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}