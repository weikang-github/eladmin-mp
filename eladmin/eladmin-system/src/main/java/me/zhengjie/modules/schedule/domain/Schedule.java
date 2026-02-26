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
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.User;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
@TableName("sch_schedule")
public class Schedule extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "schedule_id", type = IdType.AUTO)
    @ApiModelProperty(value = "排班ID", hidden = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "员工ID")
    private Long userId;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "班次ID")
    private Long shiftId;

    @NotNull
    @ApiModelProperty(value = "排班日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date scheduleDate;

    @ApiModelProperty(value = "排班类型: AUTO-自动生成, MANUAL-手动调整")
    private String scheduleType;

    @ApiModelProperty(value = "是否锁定(锁定后不可自动覆盖)")
    private Boolean isLocked;

    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField(exist = false)
    @ApiModelProperty(value = "员工信息")
    private User user;

    @TableField(exist = false)
    @ApiModelProperty(value = "部门信息")
    private Dept dept;

    @TableField(exist = false)
    @ApiModelProperty(value = "班次信息")
    private Shift shift;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
