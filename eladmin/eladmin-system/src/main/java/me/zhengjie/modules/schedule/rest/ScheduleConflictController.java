/*
 *  Copyright 2019-2025 Zheng Jie
 */
package me.zhengjie.modules.schedule.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.schedule.domain.ScheduleConflict;
import me.zhengjie.modules.schedule.domain.dto.ScheduleConflictQueryCriteria;
import me.zhengjie.modules.schedule.service.ScheduleConflictService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "排班管理：排班冲突管理")
@RequestMapping("/api/schedule/conflict")
public class ScheduleConflictController {

    private final ScheduleConflictService scheduleConflictService;

    @ApiOperation("查询排班冲突")
    @GetMapping
    public ResponseEntity<PageResult<ScheduleConflict>> query(ScheduleConflictQueryCriteria criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(scheduleConflictService.queryAll(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("根据ID查询排班冲突")
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleConflict> queryById(@PathVariable Long id) {
        return new ResponseEntity<>(scheduleConflictService.findById(id), HttpStatus.OK);
    }

    @Log("解决排班冲突")
    @ApiOperation("解决排班冲突")
    @PutMapping("/{id}/resolve")
    public ResponseEntity<Object> resolveConflict(@PathVariable Long id) {
        scheduleConflictService.resolveConflict(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("忽略排班冲突")
    @ApiOperation("忽略排班冲突")
    @PutMapping("/{id}/ignore")
    public ResponseEntity<Object> ignoreConflict(@PathVariable Long id) {
        scheduleConflictService.ignoreConflict(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("重新检测冲突")
    @ApiOperation("重新检测冲突")
    @PostMapping("/detect")
    public ResponseEntity<Object> detectConflict() {
        scheduleConflictService.detectAllConflicts();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("删除排班冲突记录")
    @ApiOperation("删除排班冲突记录")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody List<Long> ids) {
        scheduleConflictService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("导出排班冲突数据")
    @ApiOperation("导出排班冲突数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, ScheduleConflictQueryCriteria criteria) throws IOException {
        scheduleConflictService.download(scheduleConflictService.queryAll(criteria), response);
    }
}