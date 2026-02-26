/*
 *  Copyright 2019-2025 Zheng Jie
 */
package me.zhengjie.modules.schedule.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.schedule.domain.EmployeeShift;
import me.zhengjie.modules.schedule.domain.dto.BatchScheduleDTO;
import me.zhengjie.modules.schedule.domain.dto.EmployeeShiftQueryCriteria;
import me.zhengjie.modules.schedule.domain.dto.GenerateScheduleDTO;
import me.zhengjie.modules.schedule.domain.vo.ScheduleCalendarVO;
import me.zhengjie.modules.schedule.service.EmployeeShiftService;
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
@Api(tags = "排班管理：员工排班管理")
@RequestMapping("/api/schedule/employeeShift")
public class EmployeeShiftController {

    private final EmployeeShiftService employeeShiftService;

    @ApiOperation("查询员工排班")
    @GetMapping
    public ResponseEntity<PageResult<EmployeeShift>> query(EmployeeShiftQueryCriteria criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(employeeShiftService.queryAll(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("获取排班日历")
    @GetMapping("/calendar")
    public ResponseEntity<List<ScheduleCalendarVO>> getCalendar(@RequestParam Long userId, @RequestParam String yearMonth) {
        return new ResponseEntity<>(employeeShiftService.getCalendar(userId, yearMonth), HttpStatus.OK);
    }

    @ApiOperation("根据ID查询员工排班")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeShift> queryById(@PathVariable Long id) {
        return new ResponseEntity<>(employeeShiftService.findById(id), HttpStatus.OK);
    }

    @Log("新增员工排班")
    @ApiOperation("新增员工排班")
    @PostMapping
    public ResponseEntity<Object> create(@Validated @RequestBody EmployeeShift resources) {
        employeeShiftService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("批量排班")
    @ApiOperation("批量排班")
    @PostMapping("/batch")
    public ResponseEntity<Object> batchSchedule(@Validated @RequestBody BatchScheduleDTO dto) {
        employeeShiftService.batchSchedule(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("自动生成排班")
    @ApiOperation("自动生成排班")
    @PostMapping("/generate")
    public ResponseEntity<Object> generateSchedule(@Validated @RequestBody GenerateScheduleDTO dto) {
        employeeShiftService.generateSchedule(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("修改员工排班")
    @ApiOperation("修改员工排班")
    @PutMapping
    public ResponseEntity<Object> update(@Validated(EmployeeShift.Update.class) @RequestBody EmployeeShift resources) {
        employeeShiftService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除员工排班")
    @ApiOperation("删除员工排班")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody List<Long> ids) {
        employeeShiftService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("导出员工排班数据")
    @ApiOperation("导出员工排班数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, EmployeeShiftQueryCriteria criteria) throws IOException {
        employeeShiftService.download(employeeShiftService.queryAll(criteria), response);
    }
}