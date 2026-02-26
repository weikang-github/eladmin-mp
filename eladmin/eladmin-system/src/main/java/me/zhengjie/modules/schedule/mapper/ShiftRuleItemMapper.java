package me.zhengjie.modules.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.modules.schedule.domain.ShiftRuleItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ShiftRuleItemMapper extends BaseMapper<ShiftRuleItem> {

    @Select("SELECT * FROM sch_shift_rule_item WHERE rule_id = #{ruleId} ORDER BY day_index")
    List<ShiftRuleItem> findByRuleId(@Param("ruleId") Long ruleId);

    void batchInsert(@Param("list") List<ShiftRuleItem> items);

    @Select("DELETE FROM sch_shift_rule_item WHERE rule_id = #{ruleId}")
    void deleteByRuleId(@Param("ruleId") Long ruleId);
}
