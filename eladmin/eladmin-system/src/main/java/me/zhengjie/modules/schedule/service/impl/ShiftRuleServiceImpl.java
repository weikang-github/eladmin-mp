package me.zhengjie.modules.schedule.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.schedule.domain.ShiftRule;
import me.zhengjie.modules.schedule.domain.ShiftRuleItem;
import me.zhengjie.modules.schedule.domain.dto.ShiftRuleQueryCriteria;
import me.zhengjie.modules.schedule.mapper.ShiftRuleItemMapper;
import me.zhengjie.modules.schedule.mapper.ShiftRuleMapper;
import me.zhengjie.modules.schedule.service.ShiftRuleService;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ShiftRuleServiceImpl extends ServiceImpl<ShiftRuleMapper, ShiftRule> implements ShiftRuleService {

    private final ShiftRuleItemMapper shiftRuleItemMapper;
    private final ShiftRuleMapper shiftRuleMapper;

    @Override
    public List<ShiftRule> queryAll(ShiftRuleQueryCriteria criteria) {
        return baseMapper.findAll(criteria);
    }

    @Override
    public PageResult<ShiftRule> queryAll(ShiftRuleQueryCriteria criteria, Page<Object> page) {
        return PageUtil.toPage(shiftRuleMapper.findAll(criteria, page));
    }

    @Override
    public ShiftRule findById(Long id) {
        return getById(id);
    }

    @Override
    public ShiftRule findWithItems(Long id) {
        return shiftRuleMapper.findWithItems(id);
    }

    @Override
    public void create(ShiftRule shiftRule) {
        save(shiftRule);
    }

    @Override
    public void update(ShiftRule shiftRule) {
        updateById(shiftRule);
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
    public List<ShiftRule> findByUserId(Long userId) {
        return shiftRuleMapper.findByUserId(userId);
    }

    @Override
    public List<ShiftRule> findByDeptId(Long deptId) {
        return shiftRuleMapper.findByDeptId(deptId);
    }

    @Override
    public List<ShiftRule> findEnabled() {
        ShiftRuleQueryCriteria criteria = new ShiftRuleQueryCriteria();
        criteria.setEnabled(true);
        return shiftRuleMapper.findAll(criteria);
    }

    @Override
    public void saveRuleWithItems(ShiftRule shiftRule, List<ShiftRuleItem> items) {
        // 保存轮班规则
        if (shiftRule.getId() == null) {
            save(shiftRule);
        } else {
            updateById(shiftRule);
            // 删除旧的规则明细
            shiftRuleItemMapper.deleteByRuleId(shiftRule.getId());
        }

        // 保存规则明细
        if (items != null && !items.isEmpty()) {
            for (ShiftRuleItem item : items) {
                item.setRuleId(shiftRule.getId());
            }
            shiftRuleItemMapper.batchInsert(items);
        }
    }
}
