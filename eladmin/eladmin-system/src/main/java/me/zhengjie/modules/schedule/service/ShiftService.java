package me.zhengjie.modules.schedule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.schedule.domain.Shift;
import me.zhengjie.modules.schedule.domain.dto.ShiftQueryCriteria;
import me.zhengjie.utils.PageResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ShiftService extends IService<Shift> {

    PageResult<Shift> queryAll(ShiftQueryCriteria criteria, Page<Object> page);

    List<Shift> queryAll(ShiftQueryCriteria criteria);

    void create(Shift resources);

    void update(Shift resources);

    void delete(java.util.Set<Long> ids);

    void download(List<Shift> queryAll, HttpServletResponse response) throws IOException;

    Shift findByCode(String shiftCode);
}
