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

@Getter
@Setter
@TableName("schedule_group_member")
public class ScheduleGroupMember extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "分组ID")
    private Long groupId;

    @NotNull
    @ApiModelProperty(value = "员工ID")
    private Long userId;

    @ApiModelProperty(value = "轮班起始偏移天数")
    private Integer offsetDays;
}
