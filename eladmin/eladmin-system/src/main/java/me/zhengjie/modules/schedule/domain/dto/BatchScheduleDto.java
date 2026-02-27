package me.zhengjie.modules.schedule.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
public class BatchScheduleDTO implements Serializable {

    @NotNull(message = "班次ID不能为空")
    @ApiModelProperty(value = "班次ID")
    private Long shiftId;

    @NotNull(message = "用户ID列表不能为空")
    @ApiModelProperty(value = "用户ID列表")
    private Set<Long> userIds;

    @NotNull(message = "开始日期不能为空")
    @ApiModelProperty(value = "开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @NotNull(message = "结束日期不能为空")
    @ApiModelProperty(value = "结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @ApiModelProperty(value = "备注")
    private String remark;
}
