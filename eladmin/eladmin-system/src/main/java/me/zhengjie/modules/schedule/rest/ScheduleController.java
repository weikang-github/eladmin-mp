package me.zhengjie.modules.schedule.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.schedule.domain.Schedule;
import me.zhengjie.modules.schedule.domain.dto.BatchScheduleRequest;
import me.zhengjie.modules.schedule.domain.dto.ScheduleQueryCriteria;
import me.zhengjie.modules.schedule.service.ScheduleService;
import me.zhengjie.utils.PageResult;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Api(tags = "排班管理：排班管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @ApiOperation("查询排班列表")
    @GetMapping
    @PreAuthorize("@el.check('schedule:list')")
    public ResponseEntity<PageResult<Schedule>> query(ScheduleQueryCriteria criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(scheduleService.queryAll(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("查询排班详情")
    @GetMapping("/{id}")
    @PreAuthorize("@el.check('schedule:query')")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        return new ResponseEntity<>(scheduleService.findById(id), HttpStatus.OK);
    }

    @ApiOperation("查询员工排班")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Object> findByUserId(@PathVariable Long userId, 
                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<Schedule> schedules = scheduleService.findByUserIdAndDateRange(userId, 
                Date.valueOf(startDate), Date.valueOf(endDate));
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @ApiOperation("查询部门排班")
    @GetMapping("/dept/{deptId}")
    public ResponseEntity<Object> findByDeptId(@PathVariable Long deptId, 
                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<Schedule> schedules = scheduleService.findByDeptIdAndDateRange(deptId, 
                Date.valueOf(startDate), Date.valueOf(endDate));
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @ApiOperation("创建排班")
    @PostMapping
    @PreAuthorize("@el.check('schedule:add')")
    public ResponseEntity<Object> create(@Valid @RequestBody Schedule schedule) {
        scheduleService.create(schedule);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改排班")
    @PutMapping
    @PreAuthorize("@el.check('schedule:edit')")
    public ResponseEntity<Object> update(@Valid @RequestBody Schedule schedule) {
        scheduleService.update(schedule);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除排班")
    @DeleteMapping
    @PreAuthorize("@el.check('schedule:del')")
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        scheduleService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除单个排班")
    @DeleteMapping("/{id}")
    @PreAuthorize("@el.check('schedule:del')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        scheduleService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("批量排班")
    @PostMapping("/batch")
    @PreAuthorize("@el.check('schedule:batch')")
    public ResponseEntity<Object> batchSchedule(@Valid @RequestBody BatchScheduleRequest request) {
        scheduleService.batchSchedule(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("检查排班冲突")
    @PostMapping("/check-conflicts")
    public ResponseEntity<Object> checkConflicts(@Valid @RequestBody BatchScheduleRequest request) {
        return new ResponseEntity<>(scheduleService.checkConflicts(request), HttpStatus.OK);
    }

    @ApiOperation("自动生成排班")
    @PostMapping("/auto-generate")
    @PreAuthorize("@el.check('schedule:autoGenerate')")
    public ResponseEntity<Object> autoGenerate(@RequestParam Long deptId,
                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        scheduleService.autoGenerateSchedule(deptId, Date.valueOf(startDate), Date.valueOf(endDate));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("生成月度排班")
    @PostMapping("/monthly")
    @PreAuthorize("@el.check('schedule:monthly')")
    public ResponseEntity<Object> generateMonthly(@RequestParam Long deptId,
                                                 @RequestParam int year,
                                                 @RequestParam int month) {
        scheduleService.generateMonthlySchedule(deptId, year, month);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
