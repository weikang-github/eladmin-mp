package me.zhengjie.modules.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.schedule.domain.UserRule;
import me.zhengjie.modules.schedule.domain.dto.UserRuleQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserRuleMapper extends BaseMapper<UserRule> {

    List<UserRule> findAll(@Param("criteria") UserRuleQueryCriteria criteria);

    IPage<UserRule> findAll(@Param("criteria") UserRuleQueryCriteria criteria, Page<Object> page);

    List<UserRule> findEnabledByUserId(@Param("userId") Long userId);

    void batchInsert(@Param("list") List<UserRule> userRules);
}
