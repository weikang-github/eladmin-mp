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
import java.time.LocalDate;

@Getter
@Setter
@TableName("schedule_group")
public class ScheduleGroup extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "group_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long groupId;

    @NotBlank
    @ApiModelProperty(value = "分组名称")
    private String name;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "轮班规则ID")
    private Long rotationRuleId;

    @ApiModelProperty(value = "开始日期")
    private LocalDate startDate;

    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "备注")
    private String remark;
}
