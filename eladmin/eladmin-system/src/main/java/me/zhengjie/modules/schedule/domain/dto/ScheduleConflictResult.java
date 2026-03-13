package me.zhengjie.modules.schedule.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class ScheduleConflictResult {

    @ApiModelProperty(value = "是否有冲突")
    private Boolean hasConflict = false;

    @ApiModelProperty(value = "冲突列表")
    private List<ConflictInfo> conflicts;

    @Data
    public static class ConflictInfo {
        @ApiModelProperty(value = "员工ID")
        private Long userId;

        @ApiModelProperty(value = "员工姓名")
        private String userName;

        @ApiModelProperty(value = "冲突日期")
        private LocalDate conflictDate;

        @ApiModelProperty(value = "冲突类型")
        private String conflictType;

        @ApiModelProperty(value = "冲突描述")
        private String description;

        @ApiModelProperty(value = "涉及的排班信息")
        private List<ScheduleInfo> schedules;
    }

    @Data
    public static class ScheduleInfo {
        @ApiModelProperty(value = "排班ID")
        private Long scheduleId;

        @ApiModelProperty(value = "班次名称")
        private String shiftName;

        @ApiModelProperty(value = "开始时间")
        private String startTime;

        @ApiModelProperty(value = "结束时间")
        private String endTime;
    }
}
