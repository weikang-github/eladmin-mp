package me.zhengjie.modules.schedule.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@TableName("sch_shift_rule")
public class ShiftRule extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "rule_id", type = IdType.AUTO)
    @ApiModelProperty(value = "规则ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "规则编码")
    private String ruleCode;

    @NotBlank
    @ApiModelProperty(value = "规则类型: TWO_SHIFT-两班倒, THREE_SHIFT-三班倒, CUSTOM-自定义")
    private String ruleType;

    @NotNull
    @ApiModelProperty(value = "周期天数")
    private Integer cycleDays;

    @ApiModelProperty(value = "适用部门ID")
    private Long deptId;

    @NotNull
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "描述")
    private String description;

    @TableField(exist = false)
    @ApiModelProperty(value = "规则明细")
    private List<ShiftRuleItem> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShiftRule shiftRule = (ShiftRule) o;
        return Objects.equals(id, shiftRule.id) &&
                Objects.equals(ruleName, shiftRule.ruleName) &&
                Objects.equals(ruleCode, shiftRule.ruleCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ruleName, ruleCode);
    }
}
