package me.zhengjie.modules.schedule.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.sql.Date;
import java.util.List;

@Data
public class ScheduleQueryCriteria {

    @ApiModelProperty(value = "员工ID")
    private Long userId;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "班次ID")
    private Long shiftId;

    @ApiModelProperty(value = "排班日期范围")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private List<Date> scheduleDate;

    @ApiModelProperty(value = "是否锁定")
    private Boolean isLocked;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;

    @ApiModelProperty(value = "每页数据量", example = "10")
    private Integer size = 10;
}
