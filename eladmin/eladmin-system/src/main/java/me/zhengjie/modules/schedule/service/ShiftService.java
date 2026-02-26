package me.zhengjie.modules.schedule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.schedule.domain.Shift;
import me.zhengjie.modules.schedule.domain.dto.ShiftQueryCriteria;
import me.zhengjie.utils.PageResult;

import java.util.List;

public interface ShiftService extends IService<Shift> {

    List<Shift> queryAll(ShiftQueryCriteria criteria);

    PageResult<Shift> queryAll(ShiftQueryCriteria criteria, Page<Object> page);

    Shift findById(Long id);

    void create(Shift shift);

    void update(Shift shift);

    void delete(Long id);

    void deleteAll(Long[] ids);

    List<Shift> findEnabled();
}
