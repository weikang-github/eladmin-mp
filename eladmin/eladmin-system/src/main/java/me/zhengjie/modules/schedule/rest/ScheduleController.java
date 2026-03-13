package me.zhengjie.modules.schedule.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.schedule.domain.Schedule;
import me.zhengjie.modules.schedule.domain.dto.AutoScheduleRequest;
import me.zhengjie.modules.schedule.domain.dto.BatchScheduleRequest;
import me.zhengjie.modules.schedule.domain.dto.ScheduleConflictResult;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Api(tags = "排班管理：排班管理")
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    @ApiOperation("查询排班")
    @PreAuthorize("@el.check('schedule:list')")
    public ResponseEntity<PageResult<Schedule>> querySchedule(ScheduleQueryCriteria criteria, Page<Object> page) {
        return new ResponseEntity<>(scheduleService.queryAll(criteria, page), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    @ApiOperation("查询员工排班")
    public ResponseEntity<List<Schedule>> queryByUserId(@PathVariable Long userId,
                                                        @RequestParam LocalDate startDate,
                                                        @RequestParam LocalDate endDate) {
        return new ResponseEntity<>(scheduleService.findByUserIdAndDateRange(userId, startDate, endDate), HttpStatus.OK);
    }

    @GetMapping("/dept/{deptId}")
    @ApiOperation("查询部门排班")
    public ResponseEntity<List<Schedule>> queryByDeptId(@PathVariable Long deptId,
                                                        @RequestParam LocalDate startDate,
                                                        @RequestParam LocalDate endDate) {
        return new ResponseEntity<>(scheduleService.findByDeptIdAndDateRange(deptId, startDate, endDate), HttpStatus.OK);
    }

    @Log("导出排班数据")
    @ApiOperation("导出排班数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('schedule:list')")
    public void exportSchedule(HttpServletResponse response, ScheduleQueryCriteria criteria) throws IOException {
        scheduleService.download(scheduleService.queryAll(criteria), response);
    }

    @PostMapping
    @Log("新增排班")
    @ApiOperation("新增排班")
    @PreAuthorize("@el.check('schedule:add')")
    public ResponseEntity<Object> createSchedule(@Validated @RequestBody Schedule resources) {
        scheduleService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改排班")
    @ApiOperation("修改排班")
    @PreAuthorize("@el.check('schedule:edit')")
    public ResponseEntity<Object> updateSchedule(@Validated @RequestBody Schedule resources) {
        scheduleService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除排班")
    @ApiOperation("删除排班")
    @PreAuthorize("@el.check('schedule:del')")
    public ResponseEntity<Object> deleteSchedule(@RequestBody Set<Long> ids) {
        scheduleService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/range")
    @Log("删除指定日期范围的排班")
    @ApiOperation("删除指定日期范围的排班")
    @PreAuthorize("@el.check('schedule:del')")
    public ResponseEntity<Object> deleteByDateRange(@RequestParam LocalDate startDate,
                                                    @RequestParam LocalDate endDate,
                                                    @RequestParam(required = false) Long userId) {
        scheduleService.deleteByDateRange(startDate, endDate, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/detect-conflict")
    @ApiOperation("检测排班冲突")
    public ResponseEntity<ScheduleConflictResult> detectConflict(@RequestBody Schedule schedule) {
        return new ResponseEntity<>(scheduleService.detectConflicts(schedule), HttpStatus.OK);
    }

    @PostMapping("/batch/detect-conflict")
    @ApiOperation("检测批量排班冲突")
    public ResponseEntity<ScheduleConflictResult> detectBatchConflict(@RequestBody BatchScheduleRequest request) {
        return new ResponseEntity<>(scheduleService.detectConflicts(request), HttpStatus.OK);
    }

    @PostMapping("/batch")
    @Log("批量排班")
    @ApiOperation("批量排班")
    @PreAuthorize("@el.check('schedule:batch')")
    public ResponseEntity<Object> batchSchedule(@RequestBody BatchScheduleRequest request) {
        scheduleService.batchSchedule(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/auto")
    @Log("自动排班")
    @ApiOperation("自动排班")
    @PreAuthorize("@el.check('schedule:auto')")
    public ResponseEntity<Object> autoSchedule(@RequestBody AutoScheduleRequest request) {
        scheduleService.autoSchedule(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/generate-monthly")
    @Log("生成月度排班")
    @ApiOperation("生成月度排班")
    @PreAuthorize("@el.check('schedule:auto')")
    public ResponseEntity<Object> generateMonthlySchedule(@RequestParam Integer year,
                                                          @RequestParam Integer month,
                                                          @RequestParam(required = false) Long groupId) {
        scheduleService.generateMonthlySchedule(year, month, groupId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
