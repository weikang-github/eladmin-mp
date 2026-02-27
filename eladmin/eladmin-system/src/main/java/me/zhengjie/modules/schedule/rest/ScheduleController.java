package me.zhengjie.modules.schedule.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.schedule.domain.Schedule;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Api(tags = "排班管理：排班记录管理")
@RestController
@RequestMapping("/api/schedule/records")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @ApiOperation("查询排班记录")
    @GetMapping
    @PreAuthorize("@el.check('schedule:list')")
    public ResponseEntity<PageResult<Schedule>> querySchedule(ScheduleQueryCriteria criteria) {
        return new ResponseEntity<>(scheduleService.queryAll(criteria, null), HttpStatus.OK);
    }

    @ApiOperation("查询日历数据")
    @GetMapping("/calendar")
    @PreAuthorize("@el.check('schedule:list')")
    public ResponseEntity<List<Schedule>> getCalendarData(
            @RequestParam @JsonFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @JsonFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return new ResponseEntity<>(scheduleService.getCalendarData(startDate, endDate), HttpStatus.OK);
    }

    @ApiOperation("查询员工排班")
    @GetMapping("/user/{userId}")
    @PreAuthorize("@el.check('schedule:list')")
    public ResponseEntity<List<Schedule>> getUserSchedule(
            @PathVariable Long userId,
            @RequestParam @JsonFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @JsonFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return new ResponseEntity<>(scheduleService.findByUserAndDateRange(userId, startDate, endDate), HttpStatus.OK);
    }

    @ApiOperation("查询部门排班")
    @GetMapping("/dept/{deptId}")
    @PreAuthorize("@el.check('schedule:list')")
    public ResponseEntity<List<Schedule>> getDeptSchedule(
            @PathVariable Long deptId,
            @RequestParam @JsonFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @JsonFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return new ResponseEntity<>(scheduleService.findByDeptAndDateRange(deptId, startDate, endDate), HttpStatus.OK);
    }

    @Log("新增排班记录")
    @ApiOperation("新增排班记录")
    @PostMapping
    @PreAuthorize("@el.check('schedule:add')")
    public ResponseEntity<Object> createSchedule(@Validated @RequestBody Schedule resources) {
        scheduleService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改排班记录")
    @ApiOperation("修改排班记录")
    @PutMapping
    @PreAuthorize("@el.check('schedule:edit')")
    public ResponseEntity<Object> updateSchedule(@Validated(Schedule.Update.class) @RequestBody Schedule resources) {
        scheduleService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除排班记录")
    @ApiOperation("删除排班记录")
    @DeleteMapping
    @PreAuthorize("@el.check('schedule:del')")
    public ResponseEntity<Object> deleteSchedule(@RequestBody Set<Long> ids) {
        scheduleService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("删除日期范围内的排班")
    @ApiOperation("删除日期范围内的排班")
    @DeleteMapping("/range")
    @PreAuthorize("@el.check('schedule:del')")
    public ResponseEntity<Object> deleteByDateRange(
            @RequestParam @JsonFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @JsonFormat(pattern = "yyyy-MM-dd") Date endDate) {
        scheduleService.deleteByDateRange(startDate, endDate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("检查排班冲突")
    @PostMapping("/check")
    @PreAuthorize("@el.check('schedule:list')")
    public ResponseEntity<Map<String, Object>> checkConflict(@RequestBody Schedule schedule) {
        return new ResponseEntity<>(scheduleService.checkConflict(schedule), HttpStatus.OK);
    }

    @Log("自动生成排班")
    @ApiOperation("自动生成排班")
    @PostMapping("/generate")
    @PreAuthorize("@el.check('schedule:add')")
    public ResponseEntity<Object> generateSchedule(
            @RequestParam Long ruleId,
            @RequestBody Set<Long> userIds,
            @RequestParam @JsonFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @JsonFormat(pattern = "yyyy-MM-dd") Date endDate) {
        scheduleService.generateSchedule(ruleId, userIds, startDate, endDate);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("按部门自动生成排班")
    @ApiOperation("按部门自动生成排班")
    @PostMapping("/generate/dept")
    @PreAuthorize("@el.check('schedule:add')")
    public ResponseEntity<Object> generateScheduleByDept(
            @RequestParam Long ruleId,
            @RequestParam Long deptId,
            @RequestParam @JsonFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @JsonFormat(pattern = "yyyy-MM-dd") Date endDate) {
        scheduleService.generateScheduleByDept(ruleId, deptId, startDate, endDate);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("导出排班数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('schedule:list')")
    public void exportSchedule(HttpServletResponse response, ScheduleQueryCriteria criteria) throws IOException {
        scheduleService.download(scheduleService.queryAll(criteria), response);
    }
}
