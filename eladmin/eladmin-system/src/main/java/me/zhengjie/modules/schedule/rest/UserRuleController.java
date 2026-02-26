package me.zhengjie.modules.schedule.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.schedule.domain.UserRule;
import me.zhengjie.modules.schedule.domain.dto.UserRuleQueryCriteria;
import me.zhengjie.modules.schedule.service.UserRuleService;
import me.zhengjie.utils.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "排班管理：员工轮班规则")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userRule")
public class UserRuleController {

    private final UserRuleService userRuleService;

    @ApiOperation("查询员工轮班规则列表")
    @GetMapping
    @PreAuthorize("@el.check('userRule:list')")
    public ResponseEntity<PageResult<UserRule>> query(UserRuleQueryCriteria criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(userRuleService.queryAll(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("查询员工轮班规则详情")
    @GetMapping("/{id}")
    @PreAuthorize("@el.check('userRule:query')")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        return new ResponseEntity<>(userRuleService.findById(id), HttpStatus.OK);
    }

    @ApiOperation("查询员工启用的轮班规则")
    @GetMapping("/enabled/{userId}")
    public ResponseEntity<Object> findEnabledByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(userRuleService.findEnabledByUserId(userId), HttpStatus.OK);
    }

    @ApiOperation("创建员工轮班规则")
    @PostMapping
    @PreAuthorize("@el.check('userRule:add')")
    public ResponseEntity<Object> create(@Valid @RequestBody UserRule userRule) {
        userRuleService.create(userRule);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("批量绑定轮班规则")
    @PostMapping("/batch")
    @PreAuthorize("@el.check('userRule:batch')")
    public ResponseEntity<Object> batchBind(@RequestBody List<UserRule> userRules) {
        userRuleService.batchBindRules(userRules);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改员工轮班规则")
    @PutMapping
    @PreAuthorize("@el.check('userRule:edit')")
    public ResponseEntity<Object> update(@Valid @RequestBody UserRule userRule) {
        userRuleService.update(userRule);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除员工轮班规则")
    @DeleteMapping
    @PreAuthorize("@el.check('userRule:del')")
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        userRuleService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除单个员工轮班规则")
    @DeleteMapping("/{id}")
    @PreAuthorize("@el.check('userRule:del')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        userRuleService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("解除员工轮班规则绑定")
    @DeleteMapping("/unbind/{userId}/{ruleId}")
    @PreAuthorize("@el.check('userRule:del')")
    public ResponseEntity<Object> unbind(@PathVariable Long userId, @PathVariable Long ruleId) {
        userRuleService.unbindRule(userId, ruleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
