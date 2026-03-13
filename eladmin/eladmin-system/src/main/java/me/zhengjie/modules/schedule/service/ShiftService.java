package me.zhengjie.modules.schedule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.schedule.domain.Shift;
import me.zhengjie.modules.schedule.domain.dto.ShiftQueryCriteria;
import me.zhengjie.utils.PageResult;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ShiftService extends IService<Shift> {

    Shift findById(Long id);

    void create(Shift resources);

    void update(Shift resources);

    void delete(Set<Long> ids);

    PageResult<Shift> queryAll(ShiftQueryCriteria criteria, Page<Object> page);

    List<Shift> queryAll(ShiftQueryCriteria criteria);

    void download(List<Shift> queryAll, HttpServletResponse response) throws IOException;

    List<Shift> findEnabledShifts();
}
