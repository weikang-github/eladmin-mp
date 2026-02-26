package me.zhengjie.modules.schedule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.schedule.domain.UserRule;
import me.zhengjie.modules.schedule.domain.dto.UserRuleQueryCriteria;
import me.zhengjie.utils.PageResult;

import java.util.List;

public interface UserRuleService extends IService<UserRule> {

    List<UserRule> queryAll(UserRuleQueryCriteria criteria);

    PageResult<UserRule> queryAll(UserRuleQueryCriteria criteria, Page<Object> page);

    UserRule findById(Long id);

    void create(UserRule userRule);

    void update(UserRule userRule);

    void delete(Long id);

    void deleteAll(Long[] ids);

    List<UserRule> findEnabledByUserId(Long userId);

    void batchBindRules(List<UserRule> userRules);

    void unbindRule(Long userId, Long ruleId);
}
