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
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Objects;

@Getter
@Setter
@TableName("shift")
public class Shift extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "shift_id", type = IdType.AUTO)
    @ApiModelProperty(value = "班次ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "班次名称")
    private String shiftName;

    @NotBlank
    @ApiModelProperty(value = "班次编码")
    private String shiftCode;

    @NotBlank
    @ApiModelProperty(value = "班次类型")
    private String shiftType;

    @NotNull
    @ApiModelProperty(value = "开始时间")
    private Time startTime;

    @NotNull
    @ApiModelProperty(value = "结束时间")
    private Time endTime;

    @ApiModelProperty(value = "休息开始时间")
    private Time breakStartTime;

    @ApiModelProperty(value = "休息结束时间")
    private Time breakEndTime;

    @ApiModelProperty(value = "工作时长(小时)")
    private BigDecimal workHours;

    @ApiModelProperty(value = "显示颜色")
    private String color;

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
        Shift shift = (Shift) o;
        return Objects.equals(id, shift.id) &&
                Objects.equals(shiftCode, shift.shiftCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shiftCode);
    }
}
