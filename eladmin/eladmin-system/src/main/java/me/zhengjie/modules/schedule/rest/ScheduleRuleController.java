package me.zhengjie.modules.schedule.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.schedule.domain.ScheduleRule;
import me.zhengjie.modules.schedule.domain.dto.ScheduleRuleQueryCriteria;
import me.zhengjie.modules.schedule.service.ScheduleRuleService;
import me.zhengjie.utils.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Api(tags = "排班管理：排班规则管理")
@RestController
@RequestMapping("/api/schedule/rules")
@RequiredArgsConstructor
public class ScheduleRuleController {

    private final ScheduleRuleService scheduleRuleService;

    @ApiOperation("查询排班规则")
    @GetMapping
    @PreAuthorize("@el.check('rule:list')")
    public ResponseEntity<PageResult<ScheduleRule>> queryRule(ScheduleRuleQueryCriteria criteria) {
        return new ResponseEntity<>(scheduleRuleService.queryAll(criteria, null), HttpStatus.OK);
    }

    @Log("新增排班规则")
    @ApiOperation("新增排班规则")
    @PostMapping
    @PreAuthorize("@el.check('rule:add')")
    public ResponseEntity<Object> createRule(@Validated @RequestBody ScheduleRule resources) {
        scheduleRuleService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改排班规则")
    @ApiOperation("修改排班规则")
    @PutMapping
    @PreAuthorize("@el.check('rule:edit')")
    public ResponseEntity<Object> updateRule(@Validated(ScheduleRule.Update.class) @RequestBody ScheduleRule resources) {
        scheduleRuleService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除排班规则")
    @ApiOperation("删除排班规则")
    @DeleteMapping
    @PreAuthorize("@el.check('rule:del')")
    public ResponseEntity<Object> deleteRule(@RequestBody Set<Long> ids) {
        scheduleRuleService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("导出排班规则数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rule:list')")
    public void exportRule(HttpServletResponse response, ScheduleRuleQueryCriteria criteria) throws IOException {
        scheduleRuleService.download(scheduleRuleService.queryAll(criteria), response);
    }
}
