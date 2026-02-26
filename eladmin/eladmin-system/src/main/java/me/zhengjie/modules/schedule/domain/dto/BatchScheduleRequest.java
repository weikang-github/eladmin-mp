package me.zhengjie.modules.schedule.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;

@Data
public class BatchScheduleRequest {

    @NotNull
    @ApiModelProperty(value = "员工ID列表")
    private List<Long> userIds;

    @NotNull
    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @NotNull
    @ApiModelProperty(value = "班次ID")
    private Long shiftId;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始日期")
    private Date startDate;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束日期")
    private Date endDate;

    @ApiModelProperty(value = "是否锁定")
    private Boolean isLocked = false;

    @ApiModelProperty(value = "是否跳过已有排班")
    private Boolean skipExisting = true;

    @ApiModelProperty(value = "备注")
    private String remark;
}
