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
package me.zhengjie.modules.attendance.rest;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.attendance.domain.AttendanceRecord;
import me.zhengjie.modules.attendance.domain.AttendanceStatistics;
import me.zhengjie.modules.attendance.domain.dto.AttendanceCheckInDto;
import me.zhengjie.modules.attendance.domain.dto.AttendanceRecordQueryCriteria;
import me.zhengjie.modules.attendance.service.AttendanceRecordService;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 考勤记录Controller
 * @author Zheng Jie
 * @date 2025-03-27
 */
@Api(tags = "考勤：考勤记录管理")
@RestController
@RequestMapping("/api/attendance/records")
@RequiredArgsConstructor
public class AttendanceRecordController {

    private final AttendanceRecordService attendanceRecordService;

    @ApiOperation("导出考勤记录数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('attendance:record:list')")
    public void exportAttendanceRecord(HttpServletResponse response, AttendanceRecordQueryCriteria criteria) throws IOException {
        attendanceRecordService.download(attendanceRecordService.queryAll(criteria), response);
    }

    @ApiOperation("查询考勤记录")
    @GetMapping
    @PreAuthorize("@el.check('attendance:record:list')")
    public ResponseEntity<PageResult<AttendanceRecord>> queryAttendanceRecord(AttendanceRecordQueryCriteria criteria){
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(attendanceRecordService.queryAll(criteria, page), HttpStatus.OK);
    }

    @Log("打卡")
    @ApiOperation("打卡")
    @PostMapping(value = "/checkin")
    public ResponseEntity<AttendanceRecord> checkIn(@Validated @RequestBody AttendanceCheckInDto checkInDto){
        checkInDto.setUserId(SecurityUtils.getCurrentUserId());
        checkInDto.setIpAddress(SecurityUtils.getIp());
        return new ResponseEntity<>(attendanceRecordService.checkIn(checkInDto), HttpStatus.CREATED);
    }

    @ApiOperation("查询用户当天考勤记录")
    @GetMapping(value = "/today")
    public ResponseEntity<AttendanceRecord> getTodayRecord(){
        Long userId = SecurityUtils.getCurrentUserId();
        Date today = DateUtil.date();
        return new ResponseEntity<>(attendanceRecordService.findByUserAndDate(userId, today), HttpStatus.OK);
    }

    @ApiOperation("查询用户某月考勤记录")
    @GetMapping(value = "/month/{year}/{month}")
    public ResponseEntity<List<AttendanceRecord>> getMonthRecord(@PathVariable Integer year, @PathVariable Integer month){
        Long userId = SecurityUtils.getCurrentUserId();
        Date startDate = DateUtil.beginOfMonth(DateUtil.parse(year + "-" + month, "yyyy-MM"));
        Date endDate = DateUtil.endOfMonth(startDate);
        return new ResponseEntity<>(attendanceRecordService.findByUserAndDateRange(userId, startDate, endDate), HttpStatus.OK);
    }

    @ApiOperation("按日统计考勤")
    @GetMapping(value = "/statistics/day/{date}")
    public ResponseEntity<AttendanceStatistics> statisticsByDay(@PathVariable String date){
        Long userId = SecurityUtils.getCurrentUserId();
        Date statDate = DateUtil.parse(date, "yyyy-MM-dd");
        return new ResponseEntity<>(attendanceRecordService.statisticsByDay(userId, statDate), HttpStatus.OK);
    }

    @ApiOperation("按周统计考勤")
    @GetMapping(value = "/statistics/week")
    public ResponseEntity<AttendanceStatistics> statisticsByWeek(@RequestParam String startDate, @RequestParam String endDate){
        Long userId = SecurityUtils.getCurrentUserId();
        Date start = DateUtil.parse(startDate, "yyyy-MM-dd");
        Date end = DateUtil.parse(endDate, "yyyy-MM-dd");
        return new ResponseEntity<>(attendanceRecordService.statisticsByWeek(userId, start, end), HttpStatus.OK);
    }

    @ApiOperation("按月统计考勤")
    @GetMapping(value = "/statistics/month/{year}/{month}")
    public ResponseEntity<AttendanceStatistics> statisticsByMonth(@PathVariable Integer year, @PathVariable Integer month){
        Long userId = SecurityUtils.getCurrentUserId();
        return new ResponseEntity<>(attendanceRecordService.statisticsByMonth(userId, year, month), HttpStatus.OK);
    }

    @ApiOperation("导出考勤统计数据")
    @GetMapping(value = "/statistics/download")
    @PreAuthorize("@el.check('attendance:record:list')")
    public void exportStatistics(HttpServletResponse response, @RequestParam String startDate, @RequestParam String endDate) throws IOException {
        Date start = DateUtil.parse(startDate, "yyyy-MM-dd");
        Date end = DateUtil.parse(endDate, "yyyy-MM-dd");
        List<AttendanceRecord> records = attendanceRecordService.findByDeptAndDateRange(null, start, end);
        List<AttendanceStatistics> statisticsList = new java.util.ArrayList<>();
        for (AttendanceRecord record : records) {
            statisticsList.add(attendanceRecordService.statisticsByDay(record.getUserId(), record.getAttendanceDate()));
        }
        attendanceRecordService.downloadStatistics(statisticsList, response);
    }
}
