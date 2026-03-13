package me.zhengjie.modules.schedule.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class AutoScheduleRequest {

    @ApiModelProperty(value = "排班分组ID")
    private Long groupId;

    @ApiModelProperty(value = "员工ID列表")
    private List<Long> userIds;

    @ApiModelProperty(value = "轮班规则ID")
    private Long rotationRuleId;

    @ApiModelProperty(value = "开始日期")
    private LocalDate startDate;

    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "是否覆盖已有排班")
    private Boolean overwrite = false;
}
