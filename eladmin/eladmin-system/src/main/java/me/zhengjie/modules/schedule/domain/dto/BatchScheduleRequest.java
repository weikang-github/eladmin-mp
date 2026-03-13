package me.zhengjie.modules.schedule.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class BatchScheduleRequest {

    @ApiModelProperty(value = "员工ID列表")
    private List<Long> userIds;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "班次ID")
    private Long shiftId;

    @ApiModelProperty(value = "开始日期")
    private LocalDate startDate;

    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "是否覆盖已有排班")
    private Boolean overwrite = false;

    @ApiModelProperty(value = "排除的星期(1-7, 1=周一)")
    private List<Integer> excludeWeekdays;
}
