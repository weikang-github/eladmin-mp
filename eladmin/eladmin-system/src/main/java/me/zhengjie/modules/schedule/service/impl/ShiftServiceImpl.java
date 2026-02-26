package me.zhengjie.modules.schedule.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.schedule.domain.Shift;
import me.zhengjie.modules.schedule.domain.dto.ShiftQueryCriteria;
import me.zhengjie.modules.schedule.mapper.ShiftMapper;
import me.zhengjie.modules.schedule.service.ShiftService;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ShiftServiceImpl extends ServiceImpl<ShiftMapper, Shift> implements ShiftService {

    private final ShiftMapper shiftMapper;

    @Override
    public List<Shift> queryAll(ShiftQueryCriteria criteria) {
        return shiftMapper.findAll(criteria);
    }

    @Override
    public PageResult<Shift> queryAll(ShiftQueryCriteria criteria, Page<Object> page) {
        return PageUtil.toPage(shiftMapper.findAll(criteria, page));
    }

    @Override
    public Shift findById(Long id) {
        return getById(id);
    }

    @Override
    public void create(Shift shift) {
        save(shift);
    }

    @Override
    public void update(Shift shift) {
        updateById(shift);
    }

    @Override
    public void delete(Long id) {
        removeById(id);
    }

    @Override
    public void deleteAll(Long[] ids) {
        removeBatchByIds(List.of(ids));
    }

    @Override
    public List<Shift> findEnabled() {
        return shiftMapper.findEnabled();
    }
}
