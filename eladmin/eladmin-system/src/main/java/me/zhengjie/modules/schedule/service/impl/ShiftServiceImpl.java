package me.zhengjie.modules.schedule.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.exception.EntityNotFoundException;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Time;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {

    private final ShiftMapper shiftMapper;

    @Override
    public PageResult<Shift> queryAll(ShiftQueryCriteria criteria, Page<Object> page) {
        return PageUtil.toPage(shiftMapper.findAll(criteria, page));
    }

    @Override
    public List<Shift> queryAll(ShiftQueryCriteria criteria) {
        return shiftMapper.findAll(criteria, new Page<>(0, Integer.MAX_VALUE)).getRecords();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Shift resources) {
        if (shiftMapper.findByCode(resources.getShiftCode()) != null) {
            throw new EntityExistException(Shift.class, "shiftCode", resources.getShiftCode());
        }
        if (resources.getWorkHours() == null) {
            resources.setWorkHours(calculateWorkHours(resources.getStartTime(), resources.getEndTime(), 
                resources.getBreakStartTime(), resources.getBreakEndTime()));
        }
        shiftMapper.insert(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Shift resources) {
        Shift shift = shiftMapper.selectById(resources.getId());
        if (shift == null) {
            throw new EntityNotFoundException(Shift.class, "id", resources.getId());
        }
        Shift shiftByCode = shiftMapper.findByCode(resources.getShiftCode());
        if (shiftByCode != null && !shiftByCode.getId().equals(resources.getId())) {
            throw new EntityExistException(Shift.class, "shiftCode", resources.getShiftCode());
        }
        if (resources.getWorkHours() == null) {
            resources.setWorkHours(calculateWorkHours(resources.getStartTime(), resources.getEndTime(), 
                resources.getBreakStartTime(), resources.getBreakEndTime()));
        }
        shiftMapper.updateById(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        shiftMapper.deleteBatchIds(ids);
    }

    @Override
    public void download(List<Shift> queryAll, HttpServletResponse response) throws IOException {
        
    }

    @Override
    public Shift findByCode(String shiftCode) {
        return shiftMapper.findByCode(shiftCode);
    }

    private BigDecimal calculateWorkHours(Time startTime, Time endTime, Time breakStartTime, Time breakEndTime) {
        long startMillis = startTime.getTime();
        long endMillis = endTime.getTime();
        
        long workMillis = endMillis - startMillis;
        if (workMillis < 0) {
            workMillis += 24 * 60 * 60 * 1000;
        }
        
        if (breakStartTime != null && breakEndTime != null) {
            long breakStartMillis = breakStartTime.getTime();
            long breakEndMillis = breakEndTime.getTime();
            long breakMillis = breakEndMillis - breakStartMillis;
            if (breakMillis < 0) {
                breakMillis += 24 * 60 * 60 * 1000;
            }
            workMillis -= breakMillis;
        }
        
        return BigDecimal.valueOf(workMillis).divide(BigDecimal.valueOf(3600000), 2, RoundingMode.HALF_UP);
    }
}
