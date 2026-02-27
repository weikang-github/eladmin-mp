package me.zhengjie.modules.schedule.domain.dto;

import lombok.Data;
import me.zhengjie.base.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;

@Data
public class ShiftQueryCriteria extends BaseEntity {

    private Integer page = 0;

    private Integer size = 10;

    private String shiftName;

    private String shiftCode;

    private String shiftType;

    private Boolean enabled;

    @DateTimeFormat(pattern = "HH:mm:ss")
    private Time startTime;

    @DateTimeFormat(pattern = "HH:mm:ss")
    private Time endTime;
}
