package me.zhengjie.modules.schedule.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@TableName("sch_shift_rule_item")
public class ShiftRuleItem extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "item_id", type = IdType.AUTO)
    @ApiModelProperty(value = "明细ID", hidden = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "规则ID")
    private Long ruleId;

    @NotNull
    @ApiModelProperty(value = "周期内第几天(1开始)")
    private Integer dayIndex;

    @NotNull
    @ApiModelProperty(value = "班次ID")
    private Long shiftId;

    @TableField(exist = false)
    @ApiModelProperty(value = "班次信息")
    private Shift shift;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShiftRuleItem that = (ShiftRuleItem) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(ruleId, that.ruleId) &&
                Objects.equals(dayIndex, that.dayIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ruleId, dayIndex);
    }
}
