/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.schedule.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.schedule.domain.Shift;
import me.zhengjie.modules.schedule.domain.dto.ShiftQueryCriteria;
import me.zhengjie.modules.schedule.mapper.ShiftMapper;
import me.zhengjie.modules.schedule.service.ShiftService;
import me.zhengjie.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Zheng Jie
 * @date 2025-04-10
 */
@Service
@RequiredArgsConstructor
public class ShiftServiceImpl extends ServiceImpl<ShiftMapper, Shift> implements ShiftService {

    private final ShiftMapper shiftMapper;
    private final RedisUtils redisUtils;

    @Override
    public PageResult<Shift> queryAll(ShiftQueryCriteria criteria, Page<Object> page) {
        return PageUtil.toPage(shiftMapper.findAll(criteria, page));
    }

    @Override
    public List<Shift> queryAll(ShiftQueryCriteria criteria) {
        return shiftMapper.findAll(criteria);
    }

    @Override
    public Shift findById(Long id) {
        String key = CacheKey.SHIFT_ID + id;
        Shift shift = redisUtils.get(key, Shift.class);
        if(shift == null){
            shift = shiftMapper.selectById(id);
            redisUtils.set(key, shift, 1, TimeUnit.DAYS);
        }
        return shift;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Shift resources) {
        Shift exist = shiftMapper.findByCode(resources.getCode());
        if(exist != null){
            throw new BadRequestException("班次编码已存在");
        }
        validateTime(resources);
        save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Shift resources) {
        Shift shift = getById(resources.getId());
        Shift exist = shiftMapper.findByCode(resources.getCode());
        if(exist != null && !exist.getId().equals(resources.getId())){
            throw new BadRequestException("班次编码已存在");
        }
        validateTime(resources);
        resources.setId(shift.getId());
        saveOrUpdate(resources);
        // 清理缓存
        redisUtils.del(CacheKey.SHIFT_ID + resources.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            redisUtils.del(CacheKey.SHIFT_ID + id);
        }
        removeBatchByIds(ids);
    }

    @Override
    public void download(List<Shift> shifts, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Shift shift : shifts) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("班次名称", shift.getName());
            map.put("班次编码", shift.getCode());
            map.put("班次类型", getTypeText(shift.getType()));
            map.put("开始时间", shift.getStartTime());
            map.put("结束时间", shift.getEndTime());
            map.put("休息开始", shift.getBreakStartTime());
            map.put("休息结束", shift.getBreakEndTime());
            map.put("状态", shift.getEnabled() ? "启用" : "停用");
            map.put("创建日期", shift.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Shift findByCode(String code) {
        return shiftMapper.findByCode(code);
    }

    @Override
    public List<Shift> findByType(String type) {
        return shiftMapper.findByType(type);
    }

    @Override
    public List<Shift> findByEnabled(Boolean enabled) {
        return shiftMapper.findByEnabled(enabled);
    }

    private void validateTime(Shift shift) {
        if(shift.getStartTime().compareTo(shift.getEndTime()) >= 0) {
            throw new BadRequestException("开始时间必须早于结束时间");
        }
        if(shift.getBreakStartTime() != null && shift.getBreakEndTime() != null) {
            if(shift.getBreakStartTime().compareTo(shift.getBreakEndTime()) >= 0) {
                throw new BadRequestException("休息开始时间必须早于休息结束时间");
            }
            if(shift.getBreakStartTime().compareTo(shift.getStartTime()) < 0 ||
               shift.getBreakEndTime().compareTo(shift.getEndTime()) > 0) {
                throw new BadRequestException("休息时间必须在班次时间范围内");
            }
        }
    }

    private String getTypeText(String type) {
        switch (type) {
            case "MORNING": return "早班";
            case "AFTERNOON": return "中班";
            case "NIGHT": return "晚班";
            case "GRAVEYARD": return "夜班";
            case "FLEXIBLE": return "弹性班";
            default: return type;
        }
    }
}
