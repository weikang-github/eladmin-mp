package me.zhengjie.modules.schedule.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.schedule.domain.Shift;
import me.zhengjie.modules.schedule.domain.dto.ShiftQueryCriteria;
import me.zhengjie.modules.schedule.mapper.ShiftMapper;
import me.zhengjie.modules.schedule.service.ShiftService;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl extends ServiceImpl<ShiftMapper, Shift> implements ShiftService {

    private final ShiftMapper shiftMapper;

    @Override
    public Shift findById(Long id) {
        return getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Shift resources) {
        save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Shift resources) {
        updateById(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        removeByIds(ids);
    }

    @Override
    public PageResult<Shift> queryAll(ShiftQueryCriteria criteria, Page<Object> page) {
        criteria.setOffset(page.offset());
        List<Shift> shifts = shiftMapper.findAll(criteria);
        long total = count();
        return PageUtil.toPage(shifts, total);
    }

    @Override
    public List<Shift> queryAll(ShiftQueryCriteria criteria) {
        return shiftMapper.findAll(criteria);
    }

    @Override
    public void download(List<Shift> queryAll, HttpServletResponse response) throws IOException {
        // Implementation for Excel download
    }

    @Override
    public List<Shift> findEnabledShifts() {
        return lambdaQuery().eq(Shift::getEnabled, true).list();
    }
}
