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

@Getter
@Setter
@TableName("schedule_rotation_rule")
public class RotationRule extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "rule_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long ruleId;

    @NotBlank
    @ApiModelProperty(value = "规则名称")
    private String name;

    @NotBlank
    @ApiModelProperty(value = "规则编码")
    private String code;

    @ApiModelProperty(value = "规则类型(AB:A/B轮班, THREE_SHIFT:三班倒, CUSTOM:自定义)")
    private String type;

    @ApiModelProperty(value = "周期天数")
    private Integer cycleDays;

    @ApiModelProperty(value = "轮班模式(JSON格式存储班次序列)")
    private String pattern;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "备注")
    private String remark;
}
