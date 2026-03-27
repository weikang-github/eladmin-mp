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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.attendance.domain.AttendanceHoliday;
import me.zhengjie.modules.attendance.domain.dto.AttendanceHolidayQueryCriteria;
 *  AttendanceHolidayService
import me.zhengjie.modules.attendance.service.AttendanceHolidayService;
import me.zhengjie.utils.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

/**
 * 节假日Controller
 * @author Zheng Jie
 * @date 2025-03-27
 */
@Api(tags = "考勤：节假日管理")
@RestController
@RequestMapping("/api/attendance/holidays")
@RequiredArgsConstructor
public class AttendanceHolidayController {

    private final AttendanceHolidayService attendanceHolidayService;

    @ApiOperation("导出节假日数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('attendance:holiday:list')")
    public void exportAttendanceHoliday(HttpServletResponse response, AttendanceHolidayQueryCriteria criteria) throws IOException {
        attendanceHolidayService.download(attendanceHolidayService.queryAll(criteria), response);
    }

    @ApiOperation("查询节假日")
    @GetMapping
    @PreAuthorize("@el.check('attendance:holiday:list')")
    public ResponseEntity<PageResult<AttendanceHoliday>> queryAttendanceHoliday(AttendanceHolidayQueryCriteria criteria){
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(attendanceHolidayService.queryAll(criteria, page), HttpStatus.OK);
    }

    @Log("新增节假日")
    @ApiOperation("新增节假日")
    @PostMapping
    @PreAuthorize("@el.check('attendance:holiday:add')")
    public ResponseEntity<Object> createAttendanceHoliday(@Validated @RequestBody AttendanceHoliday resources){
        attendanceHolidayService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改节假日")
    @ApiOperation("修改节假日")
    @PutMapping
    @PreAuthorize("@el.check('attendance:holiday:edit')")
    public ResponseEntity<Object> updateAttendanceHoliday(@Validated(AttendanceHoliday.Update.class) @RequestBody AttendanceHoliday resources){
        attendanceHolidayService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除节假日")
    @ApiOperation("删除节假日")
    @DeleteMapping
    @PreAuthorize("@el.check('attendance:holiday:del')")
    public ResponseEntity<Object> deleteAttendanceHoliday(@RequestBody Set<Long> ids){
        attendanceHolidayService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("判断日期是否为节假日")
    @GetMapping(value = "/check/{date}")
    public ResponseEntity<Boolean> isHoliday(@PathVariable String date){
        Date checkDate = new Date(Long.parseLong(date));
        return new ResponseEntity<>(attendanceHolidayService.isHoliday(checkDate), HttpStatus.OK);
    }

    @ApiOperation("获取某年节假日列表")
    @GetMapping(value = "/year/{year}")
    @PreAuthorize("@el.check('attendance:holiday:list')")
    public ResponseEntity<java.util.List<AttendanceHoliday>> getHolidaysByYear(@PathVariable Integer year){
        return new ResponseEntity<>(attendanceHolidayService.getHolidaysByYear(year), HttpStatus.OK);
    }

    @ApiOperation("获取某月节假日列表")
    @GetMapping(value = "/month/{year}/{month}")
    @PreAuthorize("@el.check('attendance:holiday:list')")
    public ResponseEntity<java.util.List<AttendanceHoliday>> getHolidaysByMonth(@PathVariable Integer year, @PathVariable Integer month){
        return new ResponseEntity<>(attendanceHolidayService.getHolidaysByMonth(year, month), HttpStatus.OK);
    }
}
