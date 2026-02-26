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
import java.sql.Time;
import java.util.Objects;

@Getter
@Setter
@TableName("sch_shift")
public class Shift extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "shift_id", type = IdType.AUTO)
    @ApiModelProperty(value = "班次ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "班次名称")
    private String shiftName;

    @ApiModelProperty(value = "班次编码")
    private String shiftCode;

    @NotBlank
    @ApiModelProperty(value = "班次类型: MORNING-早班, AFTERNOON-中班, NIGHT-夜班, FLEXIBLE-弹性班, REST-休息")
    private String shiftType;

    @ApiModelProperty(value = "开始时间")
    private Time startTime;

    @ApiModelProperty(value = "结束时间")
    private Time endTime;

    @ApiModelProperty(value = "休息时长(分钟)")
    private Integer breakDuration;

    @ApiModelProperty(value = "显示颜色")
    private String color;

    @NotNull
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "描述")
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shift shift = (Shift) o;
        return Objects.equals(id, shift.id) &&
                Objects.equals(shiftName, shift.shiftName) &&
                Objects.equals(shiftCode, shift.shiftCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shiftName, shiftCode);
    }
}
