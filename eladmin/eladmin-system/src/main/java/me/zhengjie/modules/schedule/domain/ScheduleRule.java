package me.zhengjie.modules.schedule.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@TableName("schedule_rule")
public class ScheduleRule extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "rule_id", type = IdType.AUTO)
    @ApiModelProperty(value = "规则ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @NotBlank
    @ApiModelProperty(value = "规则编码")
    private String ruleCode;

    @NotBlank
    @ApiModelProperty(value = "规则类型")
    private String ruleType;

    @NotNull
    @ApiModelProperty(value = "轮班周期(天)")
    private Integer cycleDays;

    @NotBlank
    @ApiModelProperty(value = "班次ID列表(逗号分隔)")
    private String shiftIds;

    @ApiModelProperty(value = "描述")
    private String description;

    @NotNull
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScheduleRule rule = (ScheduleRule) o;
        return Objects.equals(id, rule.id) &&
                Objects.equals(ruleCode, rule.ruleCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ruleCode);
    }
}
