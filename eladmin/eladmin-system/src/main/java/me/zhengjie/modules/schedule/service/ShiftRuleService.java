package me.zhengjie.modules.schedule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.schedule.domain.ShiftRule;
import me.zhengjie.modules.schedule.domain.ShiftRuleItem;
import me.zhengjie.modules.schedule.domain.dto.ShiftRuleQueryCriteria;
import me.zhengjie.utils.PageResult;

import java.util.List;

public interface ShiftRuleService extends IService<ShiftRule> {

    List<ShiftRule> queryAll(ShiftRuleQueryCriteria criteria);

    PageResult<ShiftRule> queryAll(ShiftRuleQueryCriteria criteria, Page<Object> page);

    ShiftRule findById(Long id);

    ShiftRule findWithItems(Long id);

    void create(ShiftRule shiftRule);

    void update(ShiftRule shiftRule);

    void delete(Long id);

    void deleteAll(Long[] ids);

    List<ShiftRule> findByUserId(Long userId);

    List<ShiftRule> findByDeptId(Long deptId);

    List<ShiftRule> findEnabled();

    void saveRuleWithItems(ShiftRule shiftRule, List<ShiftRuleItem> items);
}
