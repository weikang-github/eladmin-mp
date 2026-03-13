package me.zhengjie.modules.schedule.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.schedule.domain.ScheduleGroup;
import me.zhengjie.modules.schedule.domain.dto.ScheduleGroupQueryCriteria;
import me.zhengjie.modules.schedule.service.ScheduleGroupService;
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

@RestController
@RequiredArgsConstructor
@Api(tags = "排班管理：排班分组管理")
@RequestMapping("/api/schedule-group")
public class ScheduleGroupController {

    private final ScheduleGroupService scheduleGroupService;

    @GetMapping
    @ApiOperation("查询排班分组")
    @PreAuthorize("@el.check('group:list')")
    public ResponseEntity<PageResult<ScheduleGroup>> queryScheduleGroup(ScheduleGroupQueryCriteria criteria, Page<Object> page) {
        return new ResponseEntity<>(scheduleGroupService.queryAll(criteria, page), HttpStatus.OK);
    }

    @GetMapping("/dept/{deptId}")
    @ApiOperation("查询部门排班分组")
    public ResponseEntity<List<ScheduleGroup>> queryByDeptId(@PathVariable Long deptId) {
        return new ResponseEntity<>(scheduleGroupService.findByDeptId(deptId), HttpStatus.OK);
    }

    @Log("导出排班分组数据")
    @ApiOperation("导出排班分组数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('group:list')")
    public void exportScheduleGroup(HttpServletResponse response, ScheduleGroupQueryCriteria criteria) throws IOException {
        scheduleGroupService.download(scheduleGroupService.queryAll(criteria), response);
    }

    @PostMapping
    @Log("新增排班分组")
    @ApiOperation("新增排班分组")
    @PreAuthorize("@el.check('group:add')")
    public ResponseEntity<Object> createScheduleGroup(@Validated @RequestBody ScheduleGroup resources) {
        scheduleGroupService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改排班分组")
    @ApiOperation("修改排班分组")
    @PreAuthorize("@el.check('group:edit')")
    public ResponseEntity<Object> updateScheduleGroup(@Validated @RequestBody ScheduleGroup resources) {
        scheduleGroupService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除排班分组")
    @ApiOperation("删除排班分组")
    @PreAuthorize("@el.check('group:del')")
    public ResponseEntity<Object> deleteScheduleGroup(@RequestBody Set<Long> ids) {
        scheduleGroupService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{groupId}/members")
    @Log("添加分组成员")
    @ApiOperation("添加分组成员")
    @PreAuthorize("@el.check('group:edit')")
    public ResponseEntity<Object> addMembers(@PathVariable Long groupId, @RequestBody List<Long> userIds) {
        scheduleGroupService.addMembers(groupId, userIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}/members")
    @Log("移除分组成员")
    @ApiOperation("移除分组成员")
    @PreAuthorize("@el.check('group:edit')")
    public ResponseEntity<Object> removeMembers(@PathVariable Long groupId, @RequestBody List<Long> userIds) {
        scheduleGroupService.removeMembers(groupId, userIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
