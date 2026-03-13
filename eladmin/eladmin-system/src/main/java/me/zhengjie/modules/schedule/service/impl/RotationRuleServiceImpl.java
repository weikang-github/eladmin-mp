package me.zhengjie.modules.schedule.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.schedule.domain.RotationRule;
import me.zhengjie.modules.schedule.domain.dto.RotationRuleQueryCriteria;
import me.zhengjie.modules.schedule.mapper.RotationRuleMapper;
import me.zhengjie.modules.schedule.service.RotationRuleService;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RotationRuleServiceImpl extends ServiceImpl<RotationRuleMapper, RotationRule> implements RotationRuleService {

    private final RotationRuleMapper rotationRuleMapper;

    @Override
    public RotationRule findById(Long id) {
        return getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(RotationRule resources) {
        save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RotationRule resources) {
        updateById(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        removeByIds(ids);
    }

    @Override
    public PageResult<RotationRule> queryAll(RotationRuleQueryCriteria criteria, Page<Object> page) {
        criteria.setOffset(page.offset());
        List<RotationRule> rules = rotationRuleMapper.findAll(criteria);
        long total = count();
        return PageUtil.toPage(rules, total);
    }

    @Override
    public List<RotationRule> queryAll(RotationRuleQueryCriteria criteria) {
        return rotationRuleMapper.findAll(criteria);
    }

    @Override
    public void download(List<RotationRule> queryAll, HttpServletResponse response) throws IOException {
        // Implementation for Excel download
    }

    @Override
    public List<RotationRule> findEnabledRules() {
        return lambdaQuery().eq(RotationRule::getEnabled, true).list();
    }
}
