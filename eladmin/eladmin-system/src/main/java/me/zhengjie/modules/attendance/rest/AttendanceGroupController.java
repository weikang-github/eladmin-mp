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
import me.zhengjie.modules.attendance.domain.AttendanceGroup;
import me.zhengjie.modules.attendance.domain.dto.AttendanceGroupQueryCriteria;
import me.zhengjie.modules.attendance.service.AttendanceGroupService;
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
 * 考勤组Controller
 * @author Zheng Jie
 * @date 2025-03-27
 */
@Api(tags = "考勤：考勤组管理")
@RestController
@RequestMapping("/api/attendance/groups")
@RequiredArgsConstructor
public class AttendanceGroupController {

    private final AttendanceGroupService attendanceGroupService;

    @ApiOperation("导出考勤组数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('attendance:group:list')")
    public void exportAttendanceGroup(HttpServletResponse response, AttendanceGroupQueryCriteria criteria) throws IOException {
        attendanceGroupService.download(attendanceGroupService.queryAll(criteria), response);
    }

    @ApiOperation("查询考勤组")
    @GetMapping
    @PreAuthorize("@el.check('attendance:group:list')")
    public ResponseEntity<PageResult<AttendanceGroup>> queryAttendanceGroup(AttendanceGroupQueryCriteria criteria){
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(attendanceGroupService.queryAll(criteria, page), HttpStatus.OK);
    }

    @Log("新增考勤组")
    @ApiOperation("新增考勤组")
    @PostMapping
    @PreAuthorize("@el.check('attendance:group:add')")
    public ResponseEntity<Object> createAttendanceGroup(@Validated @RequestBody AttendanceGroup resources){
        attendanceGroupService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改考勤组")
    @ApiOperation("修改考勤组")
    @PutMapping
    @PreAuthorize("@el.check('attendance:group:edit')")
    public ResponseEntity<Object> updateAttendanceGroup(@Validated(AttendanceGroup.Update.class) @RequestBody AttendanceGroup resources){
        attendanceGroupService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除考勤组")
    @ApiOperation("删除考勤组")
    @DeleteMapping
    @PreAuthorize("@el.check('attendance:group:del')")
    public ResponseEntity<Object> deleteAttendanceGroup(@RequestBody Set<Long> ids){
        attendanceGroupService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("查询用户所属考勤组")
    @GetMapping(value = "/user/{userId}")
    @PreAuthorize("@el.check('attendance:group:list')")
    public ResponseEntity<AttendanceGroup> findByUserId(@PathVariable Long userId){
        return new ResponseEntity<>(attendanceGroupService.findByUserId(userId), HttpStatus.OK);
    }

    @ApiOperation("查询部门所属考勤组")
    @GetMapping(value = "/dept/{deptId}")
    @PreAuthorize("@el.check('attendance:group:list')")
    public ResponseEntity<List<AttendanceGroup>> findByDeptId(@PathVariable Long deptId){
        return new ResponseEntity<>(attendanceGroupService.findByDeptId(deptId), HttpStatus.OK);
    }
}
