package me.zhengjie.modules.schedule.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import me.zhengjie.base.BaseEntity;

import java.util.Date;
import java.util.List;

@Data
public class ScheduleQueryCriteria extends BaseEntity {

    private Integer page = 0;

    private Integer size = 10;

    private Long userId;

    private Long deptId;

    private Long shiftId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private String scheduleType;

    private String status;

    private Long ruleId;

    private List<Long> userIds;

    private List<Long> deptIds;
}
