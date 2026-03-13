package me.zhengjie.modules.schedule.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@TableName("schedule")
public class Schedule extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "schedule_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long scheduleId;

    @NotNull
    @ApiModelProperty(value = "员工ID")
    private Long userId;

    @NotNull
    @ApiModelProperty(value = "班次ID")
    private Long shiftId;

    @NotNull
    @ApiModelProperty(value = "排班日期")
    private LocalDate scheduleDate;

    @ApiModelProperty(value = "排班类型(manual:手动, auto:自动)")
    private String type;

    @ApiModelProperty(value = "状态(0:正常, 1:调休, 2:加班)")
    private String status;

    @ApiModelProperty(value = "备注")
    private String remark;
}
