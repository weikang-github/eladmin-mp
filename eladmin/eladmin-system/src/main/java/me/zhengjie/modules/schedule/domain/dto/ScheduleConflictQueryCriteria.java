package me.zhengjie.modules.schedule.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Data
public class ScheduleConflictQueryCriteria implements Serializable {

    @ApiModelProperty(value = "ID")
    private Long conflictId;

    @ApiModelProperty(value = "员工ID")
    private Long userId;

    @ApiModelProperty(value = "冲突类型")
    private String conflictType;

    @ApiModelProperty(value = "冲突状态")
    private String status;

    @ApiModelProperty(value = "冲突日期开始")
    private LocalDate startDate;

    @ApiModelProperty(value = "冲突日期结束")
    private LocalDate endDate;

    @ApiModelProperty(value = "创建时间")
    private List<Timestamp> createTime;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;

    @ApiModelProperty(value = "每页数据量", example = "10")
    private Integer size = 10;

    @ApiModelProperty(value = "偏移量", hidden = true)
    private long offset;
}
