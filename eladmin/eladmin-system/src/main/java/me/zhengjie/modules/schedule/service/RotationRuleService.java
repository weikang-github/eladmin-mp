package me.zhengjie.modules.schedule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.schedule.domain.RotationRule;
import me.zhengjie.modules.schedule.domain.dto.RotationRuleQueryCriteria;
import me.zhengjie.utils.PageResult;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface RotationRuleService extends IService<RotationRule> {

    RotationRule findById(Long id);

    void create(RotationRule resources);

    void update(RotationRule resources);

    void delete(Set<Long> ids);

    PageResult<RotationRule> queryAll(RotationRuleQueryCriteria criteria, Page<Object> page);

    List<RotationRule> queryAll(RotationRuleQueryCriteria criteria);

    void download(List<RotationRule> queryAll, HttpServletResponse response) throws IOException;

    List<RotationRule> findEnabledRules();
}
