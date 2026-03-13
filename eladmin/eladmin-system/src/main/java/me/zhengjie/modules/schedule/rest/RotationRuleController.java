package me.zhengjie.modules.schedule.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.schedule.domain.RotationRule;
import me.zhengjie.modules.schedule.domain.dto.RotationRuleQueryCriteria;
import me.zhengjie.modules.schedule.service.RotationRuleService;
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
@Api(tags = "排班管理：轮班规则管理")
@RequestMapping("/api/rotation-rule")
public class RotationRuleController {

    private final RotationRuleService rotationRuleService;

    @GetMapping
    @ApiOperation("查询轮班规则")
    @PreAuthorize("@el.check('rotation:list')")
    public ResponseEntity<PageResult<RotationRule>> queryRotationRule(RotationRuleQueryCriteria criteria, Page<Object> page) {
        return new ResponseEntity<>(rotationRuleService.queryAll(criteria, page), HttpStatus.OK);
    }

    @GetMapping("/all")
    @ApiOperation("查询所有启用的轮班规则")
    public ResponseEntity<List<RotationRule>> queryAllEnabledRule() {
        return new ResponseEntity<>(rotationRuleService.findEnabledRules(), HttpStatus.OK);
    }

    @Log("导出轮班规则数据")
    @ApiOperation("导出轮班规则数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rotation:list')")
    public void exportRotationRule(HttpServletResponse response, RotationRuleQueryCriteria criteria) throws IOException {
        rotationRuleService.download(rotationRuleService.queryAll(criteria), response);
    }

    @PostMapping
    @Log("新增轮班规则")
    @ApiOperation("新增轮班规则")
    @PreAuthorize("@el.check('rotation:add')")
    public ResponseEntity<Object> createRotationRule(@Validated @RequestBody RotationRule resources) {
        rotationRuleService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改轮班规则")
    @ApiOperation("修改轮班规则")
    @PreAuthorize("@el.check('rotation:edit')")
    public ResponseEntity<Object> updateRotationRule(@Validated @RequestBody RotationRule resources) {
        rotationRuleService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除轮班规则")
    @ApiOperation("删除轮班规则")
    @PreAuthorize("@el.check('rotation:del')")
    public ResponseEntity<Object> deleteRotationRule(@RequestBody Set<Long> ids) {
        rotationRuleService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
