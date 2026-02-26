package me.zhengjie.modules.schedule.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.schedule.domain.Shift;
import me.zhengjie.modules.schedule.domain.dto.ShiftQueryCriteria;
import me.zhengjie.modules.schedule.service.ShiftService;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "排班管理：班次管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shift")
public class ShiftController {

    private final ShiftService shiftService;

    @ApiOperation("查询班次列表")
    @GetMapping
    @PreAuthorize("@el.check('shift:list')")
    public ResponseEntity<PageResult<Shift>> query(ShiftQueryCriteria criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(shiftService.queryAll(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("查询所有启用的班次")
    @GetMapping("/enabled")
    public ResponseEntity<Object> findEnabled() {
        return new ResponseEntity<>(shiftService.findEnabled(), HttpStatus.OK);
    }

    @ApiOperation("查询班次详情")
    @GetMapping("/{id}")
    @PreAuthorize("@el.check('shift:query')")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        return new ResponseEntity<>(shiftService.findById(id), HttpStatus.OK);
    }

    @ApiOperation("创建班次")
    @PostMapping
    @PreAuthorize("@el.check('shift:add')")
    public ResponseEntity<Object> create(@Valid @RequestBody Shift shift) {
        shiftService.create(shift);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改班次")
    @PutMapping
    @PreAuthorize("@el.check('shift:edit')")
    public ResponseEntity<Object> update(@Valid @RequestBody Shift shift) {
        shiftService.update(shift);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除班次")
    @DeleteMapping
    @PreAuthorize("@el.check('shift:del')")
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        shiftService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除单个班次")
    @DeleteMapping("/{id}")
    @PreAuthorize("@el.check('shift:del')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        shiftService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
