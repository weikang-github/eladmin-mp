package me.zhengjie.modules.schedule.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.schedule.domain.UserRule;
import me.zhengjie.modules.schedule.domain.dto.UserRuleQueryCriteria;
import me.zhengjie.modules.schedule.mapper.UserRuleMapper;
import me.zhengjie.modules.schedule.service.UserRuleService;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserRuleServiceImpl extends ServiceImpl<UserRuleMapper, UserRule> implements UserRuleService {

    private final UserRuleMapper userRuleMapper;

    @Override
    public List<UserRule> queryAll(UserRuleQueryCriteria criteria) {
        return userRuleMapper.findAll(criteria);
    }

    @Override
    public PageResult<UserRule> queryAll(UserRuleQueryCriteria criteria, Page<Object> page) {
        return PageUtil.toPage(userRuleMapper.findAll(criteria, page));
    }

    @Override
    public UserRule findById(Long id) {
        return getById(id);
    }

    @Override
    public void create(UserRule userRule) {
        save(userRule);
    }

    @Override
    public void update(UserRule userRule) {
        updateById(userRule);
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
    public List<UserRule> findEnabledByUserId(Long userId) {
        return userRuleMapper.findEnabledByUserId(userId);
    }

    @Override
    public void batchBindRules(List<UserRule> userRules) {
        if (!userRules.isEmpty()) {
            userRuleMapper.batchInsert(userRules);
        }
    }

    @Override
    public void unbindRule(Long userId, Long ruleId) {
        // 逻辑删除或更新状态为禁用
        UserRuleQueryCriteria criteria = new UserRuleQueryCriteria();
        criteria.setUserId(userId);
        criteria.setRuleId(ruleId);
        List<UserRule> userRules = userRuleMapper.findAll(criteria);
        for (UserRule userRule : userRules) {
            userRule.setEnabled(false);
            updateById(userRule);
        }
    }
}
