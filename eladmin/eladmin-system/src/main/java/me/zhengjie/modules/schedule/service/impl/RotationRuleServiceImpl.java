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
import me.zhengjie.modules.schedule.domain.RotationRule;
import me.zhengjie.modules.schedule.domain.dto.RotationRuleQueryCriteria;
import me.zhengjie.modules.schedule.mapper.RotationRuleMapper;
import me.zhengjie.modules.schedule.service.RotationRuleService;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 轮班规则Service实现
 * @author Zheng Jie
 * @date 2026-02-26
 */
@Service
@RequiredArgsConstructor
public class RotationRuleServiceImpl extends ServiceImpl<RotationRuleMapper, RotationRule> implements RotationRuleService {

    private final RotationRuleMapper rotationRuleMapper;

    @Override
    public PageResult<RotationRule> queryAll(RotationRuleQueryCriteria criteria, Page<Object> page) {
        LambdaQueryWrapper<RotationRule> wrapper = buildQueryWrapper(criteria);
        Page<RotationRule> resultPage = rotationRuleMapper.selectPage(new Page<>(page.getCurrent(), page.getSize()), wrapper);
        return PageUtil.toPage(resultPage.getRecords(), resultPage.getTotal());
    }

    @Override
    public List<RotationRule> queryAll(RotationRuleQueryCriteria criteria) {
        LambdaQueryWrapper<RotationRule> wrapper = buildQueryWrapper(criteria);
        return rotationRuleMapper.selectList(wrapper);
    }

    private LambdaQueryWrapper<RotationRule> buildQueryWrapper(RotationRuleQueryCriteria criteria) {
        LambdaQueryWrapper<RotationRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(criteria.getName() != null, RotationRule::getName, criteria.getName())
               .eq(criteria.getRotationType() != null, RotationRule::getRotationType, criteria.getRotationType())
               .eq(criteria.getIsEnabled() != null, RotationRule::getIsEnabled, criteria.getIsEnabled())
               .orderByDesc(RotationRule::getCreateTime);
        return wrapper;
    }

    @Override
    public RotationRule findById(Long id) {
        return getById(id);
    }

    @Override
    public RotationRule findByCode(String code) {
        return rotationRuleMapper.findByCode(code);
    }

    @Override
    public List<RotationRule> findAllEnabled() {
        return rotationRuleMapper.findAllEnabled();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(RotationRule resources) {
        // 检查编码是否已存在
        RotationRule existing = rotationRuleMapper.findByCode(resources.getCode());
        if (existing != null) {
            throw new EntityExistException(RotationRule.class, "code", resources.getCode());
        }
        save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RotationRule resources) {
        // 检查编码是否被其他记录使用
        RotationRule existing = rotationRuleMapper.findByCode(resources.getCode());
        if (existing != null && !existing.getId().equals(resources.getId())) {
            throw new EntityExistException(RotationRule.class, "code", resources.getCode());
        }
        updateById(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids) {
        removeByIds(ids);
    }

    @Override
    public void download(List<RotationRule> data, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RotationRule dto : data) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("规则名称", dto.getName());
            map.put("规则编码", dto.getCode());
            map.put("轮班类型", dto.getRotationType());
            map.put("周期天数", dto.getCycleDays());
            map.put("班次序列", dto.getShiftSequence());
            map.put("是否启用", dto.getIsEnabled() ? "是" : "否");
            map.put("备注", dto.getRemark());
            map.put("创建时间", dto.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
