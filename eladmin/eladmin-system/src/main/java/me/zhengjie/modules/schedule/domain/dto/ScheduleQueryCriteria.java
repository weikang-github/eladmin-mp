package me.zhengjie.modules.schedule.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Data
public class ScheduleQueryCriteria implements Serializable {

    @ApiModelProperty(value = "ID")
    private Long scheduleId;

    @ApiModelProperty(value = "员工ID")
    private Long userId;

    @ApiModelProperty(value = "班次ID")
    private Long shiftId;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "排班日期开始")
    private LocalDate startDate;

    @ApiModelProperty(value = "排班日期结束")
    private LocalDate endDate;

    @ApiModelProperty(value = "排班类型")
    private String type;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private List<Timestamp> createTime;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;

    @ApiModelProperty(value = "每页数据量", example = "10")
    private Integer size = 10;

    @ApiModelProperty(value = "偏移量", hidden = true)
    private long offset;
}
