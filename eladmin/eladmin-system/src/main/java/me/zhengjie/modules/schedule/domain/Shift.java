package me.zhengjie.modules.schedule.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental;
import me.zhengjie.base.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalTime;

@Getter
@Setter
@TableName("schedule_shift")
public class Shift extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "shift_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long shiftId;

    @NotBlank
    @ApiModelProperty(value = "班次名称")
    private String name;

    @NotBlank
    @ApiModelProperty(value = "班次编码")
    private String code;

    @ApiModelProperty(value = "班次类型(day:按天, week:按周, month:按月")
    private String type;

    @NotNull
    @ApiModelProperty(value = "开始时间")
    private LocalTime startTime;

    @NotNull
    @ApiModelProperty(value = "结束时间")
    private LocalTime endTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "颜色标识")
    private String color;
}
