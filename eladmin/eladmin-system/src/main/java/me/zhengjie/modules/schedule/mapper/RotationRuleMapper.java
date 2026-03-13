package me.zhengjie.modules.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.schedule.domain.RotationRule;
import me.zhengjie.modules.schedule.domain.dto.RotationRuleQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RotationRuleMapper extends BaseMapper<RotationRule> {

    List<RotationRule> findAll(@Param("criteria") RotationRuleQueryCriteria criteria);

    IPage<RotationRule> findAll(@Param("criteria") RotationRuleQueryCriteria criteria, Page<Object> page);
}
