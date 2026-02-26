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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.modules.schedule.domain.ShiftType;
import me.zhengjie.modules.schedule.domain.dto.ShiftTypeQueryCriteria;
import me.zhengjie.modules.schedule.mapper.ShiftTypeMapper;
import me.zhengjie.modules.schedule.service.ShiftTypeService;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 班次类型Service实现
 * @author Zheng Jie
 * @date 2026-02-26
 */
@Service
@RequiredArgsConstructor
public class ShiftTypeServiceImpl extends ServiceImpl<ShiftTypeMapper, ShiftType> implements ShiftTypeService {

    private final ShiftTypeMapper shiftTypeMapper;

    @Override
    public PageResult<ShiftType> queryAll(ShiftTypeQueryCriteria criteria, Page<Object> page) {
        LambdaQueryWrapper<ShiftType> wrapper = buildQueryWrapper(criteria);
        Page<ShiftType> resultPage = shiftTypeMapper.selectPage(new Page<>(page.getCurrent(), page.getSize()), wrapper);
        return PageUtil.toPage(resultPage.getRecords(), resultPage.getTotal());
    }

    @Override
    public List<ShiftType> queryAll(ShiftTypeQueryCriteria criteria) {
        LambdaQueryWrapper<ShiftType> wrapper = buildQueryWrapper(criteria);
        return shiftTypeMapper.selectList(wrapper);
    }

    private LambdaQueryWrapper<ShiftType> buildQueryWrapper(ShiftTypeQueryCriteria criteria) {
        LambdaQueryWrapper<ShiftType> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(criteria.getName() != null, ShiftType::getName, criteria.getName())
               .eq(criteria.getCode() != null, ShiftType::getCode, criteria.getCode())
               .eq(criteria.getIsEnabled() != null, ShiftType::getIsEnabled, criteria.getIsEnabled())
               .orderByAsc(ShiftType::getSortOrder);
        return wrapper;
    }

    @Override
    public ShiftType findById(Long id) {
        return getById(id);
    }

    @Override
    public ShiftType findByCode(String code) {
        return shiftTypeMapper.findByCode(code);
    }

    @Override
    public List<ShiftType> findAllEnabled() {
        return shiftTypeMapper.findAllEnabled();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ShiftType resources) {
        // 检查编码是否已存在
        ShiftType existing = shiftTypeMapper.findByCode(resources.getCode());
        if (existing != null) {
            throw new EntityExistException(ShiftType.class, "code", resources.getCode());
        }
        save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ShiftType resources) {
        // 检查编码是否被其他记录使用
        ShiftType existing = shiftTypeMapper.findByCode(resources.getCode());
        if (existing != null && !existing.getId().equals(resources.getId())) {
            throw new EntityExistException(ShiftType.class, "code", resources.getCode());
        }
        updateById(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids) {
        removeByIds(ids);
    }

    @Override
    public void download(List<ShiftType> data, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ShiftType dto : data) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("班次名称", dto.getName());
            map.put("班次编码", dto.getCode());
            map.put("上班时间", dto.getStartTime());
            map.put("下班时间", dto.getEndTime());
            map.put("工作时长", dto.getWorkHours());
            map.put("是否休息", dto.getIsRest() ? "是" : "否");
            map.put("是否启用", dto.getIsEnabled() ? "是" : "否");
            map.put("排序", dto.getSortOrder());
            map.put("备注", dto.getRemark());
            map.put("创建时间", dto.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
