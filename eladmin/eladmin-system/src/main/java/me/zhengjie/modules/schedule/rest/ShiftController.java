package me.zhengjie.modules.schedule.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.schedule.domain.Shift;
import me.zhengjie.modules.schedule.domain.dto.ShiftQueryCriteria;
import me.zhengjie.modules.schedule.service.ShiftService;
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
@Api(tags = "排班管理：班次管理")
@RequestMapping("/api/shift")
public class ShiftController {

    private final ShiftService shiftService;

    @GetMapping
    @ApiOperation("查询班次")
    @PreAuthorize("@el.check('shift:list')")
    public ResponseEntity<PageResult<Shift>> queryShift(ShiftQueryCriteria criteria, Page<Object> page) {
        return new ResponseEntity<>(shiftService.queryAll(criteria, page), HttpStatus.OK);
    }

    @GetMapping("/all")
    @ApiOperation("查询所有启用的班次")
    public ResponseEntity<List<Shift>> queryAllEnabledShift() {
        return new ResponseEntity<>(shiftService.findEnabledShifts(), HttpStatus.OK);
    }

    @Log("导出班次数据")
    @ApiOperation("导出班次数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('shift:list')")
    public void exportShift(HttpServletResponse response, ShiftQueryCriteria criteria) throws IOException {
        shiftService.download(shiftService.queryAll(criteria), response);
    }

    @PostMapping
    @Log("新增班次")
    @ApiOperation("新增班次")
    @PreAuthorize("@el.check('shift:add')")
    public ResponseEntity<Object> createShift(@Validated @RequestBody Shift resources) {
        shiftService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改班次")
    @ApiOperation("修改班次")
    @PreAuthorize("@el.check('shift:edit')")
    public ResponseEntity<Object> updateShift(@Validated @RequestBody Shift resources) {
        shiftService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除班次")
    @ApiOperation("删除班次")
    @PreAuthorize("@el.check('shift:del')")
    public ResponseEntity<Object> deleteShift(@RequestBody Set<Long> ids) {
        shiftService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
