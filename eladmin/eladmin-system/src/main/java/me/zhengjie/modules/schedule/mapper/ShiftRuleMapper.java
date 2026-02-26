package me.zhengjie.modules.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.schedule.domain.ShiftRule;
import me.zhengjie.modules.schedule.domain.dto.ShiftRuleQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ShiftRuleMapper extends BaseMapper<ShiftRule> {

    List<ShiftRule> findAll(@Param("criteria") ShiftRuleQueryCriteria criteria);

    IPage<ShiftRule> findAll(@Param("criteria") ShiftRuleQueryCriteria criteria, Page<Object> page);

    ShiftRule findWithItems(@Param("id") Long id);

    List<ShiftRule> findByUserId(@Param("userId") Long userId);

    List<ShiftRule> findByDeptId(@Param("deptId") Long deptId);
}
