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
@TableName("schedule_conflict")
public class ScheduleConflict extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "conflict_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long conflictId;

    @NotNull
    @ApiModelProperty(value = "员工ID")
    private Long userId;

    @NotNull
    @ApiModelProperty(value = "冲突日期")
    private LocalDate conflictDate;

    @ApiModelProperty(value = "冲突类型(OVERLAP:班次重叠, REST_CONFLICT:休息冲突, OVERTIME:超时)")
    private String conflictType;

    @ApiModelProperty(value = "冲突描述")
    private String description;

    @ApiModelProperty(value = "冲突状态(PENDING:待处理, RESOLVED:已解决, IGNORED:已忽略)")
    private String status;

    @ApiModelProperty(value = "涉及的排班ID1")
    private Long scheduleId1;

    @ApiModelProperty(value = "涉及的排班ID2")
    private Long scheduleId2;
}
