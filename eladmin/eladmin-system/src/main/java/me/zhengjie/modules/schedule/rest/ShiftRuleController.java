package me.zhengjie.modules.schedule.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.schedule.domain.ShiftRule;
import me.zhengjie.modules.schedule.domain.ShiftRuleItem;
import me.zhengjie.modules.schedule.domain.dto.ShiftRuleQueryCriteria;
import me.zhengjie.modules.schedule.service.ShiftRuleService;
import me.zhengjie.utils.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "排班管理：轮班规则")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shiftRule")
public class ShiftRuleController {

    private final ShiftRuleService shiftRuleService;

    @ApiOperation("查询轮班规则列表")
    @GetMapping
    @PreAuthorize("@el.check('shiftRule:list')")
    public ResponseEntity<PageResult<ShiftRule>> query(ShiftRuleQueryCriteria criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(shiftRuleService.queryAll(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("查询轮班规则详情")
    @GetMapping("/{id}")
    @PreAuthorize("@el.check('shiftRule:query')")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        return new ResponseEntity<>(shiftRuleService.findWithItems(id), HttpStatus.OK);
    }

    @ApiOperation("查询启用的轮班规则")
    @GetMapping("/enabled")
    public ResponseEntity<Object> findEnabled() {
        return new ResponseEntity<>(shiftRuleService.findEnabled(), HttpStatus.OK);
    }

    @ApiOperation("创建轮班规则")
    @PostMapping
    @PreAuthorize("@el.check('shiftRule:add')")
    public ResponseEntity<Object> create(@Valid @RequestBody ShiftRule shiftRule) {
        shiftRuleService.create(shiftRule);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("保存轮班规则及明细")
    @PostMapping("/saveWithItems")
    @PreAuthorize("@el.check('shiftRule:add')")
    public ResponseEntity<Object> saveWithItems(@RequestParam Long ruleId, @RequestBody List<ShiftRuleItem> items) {
        ShiftRule shiftRule = shiftRuleService.findById(ruleId);
        if (shiftRule == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        shiftRuleService.saveRuleWithItems(shiftRule, items);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改轮班规则")
    @PutMapping
    @PreAuthorize("@el.check('shiftRule:edit')")
    public ResponseEntity<Object> update(@Valid @RequestBody ShiftRule shiftRule) {
        shiftRuleService.update(shiftRule);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除轮班规则")
    @DeleteMapping
    @PreAuthorize("@el.check('shiftRule:del')")
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        shiftRuleService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除单个轮班规则")
    @DeleteMapping("/{id}")
    @PreAuthorize("@el.check('shiftRule:del')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        shiftRuleService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
