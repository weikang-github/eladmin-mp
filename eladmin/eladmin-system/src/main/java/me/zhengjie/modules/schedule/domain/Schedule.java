package me.zhengjie.modules.schedule.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@TableName("schedule")
public class Schedule extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "schedule_id", type = IdType.AUTO)
    @ApiModelProperty(value = "排班ID", hidden = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @NotNull
    @ApiModelProperty(value = "班次ID")
    private Long shiftId;

    @NotNull
    @ApiModelProperty(value = "排班日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date scheduleDate;

    @NotNull
    @ApiModelProperty(value = "排班类型")
    private String scheduleType;

    @ApiModelProperty(value = "规则ID")
    private Long ruleId;

    @NotNull
    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户名称")
    private String userName;

    @TableField(exist = false)
    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @TableField(exist = false)
    @ApiModelProperty(value = "班次名称")
    private String shiftName;

    @TableField(exist = false)
    @ApiModelProperty(value = "班次类型")
    private String shiftType;

    @TableField(exist = false)
    @ApiModelProperty(value = "班次颜色")
    private String shiftColor;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Schedule schedule = (Schedule) o;
        return Objects.equals(id, schedule.id) &&
                Objects.equals(userId, schedule.userId) &&
                Objects.equals(scheduleDate, schedule.scheduleDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, scheduleDate);
    }
}
