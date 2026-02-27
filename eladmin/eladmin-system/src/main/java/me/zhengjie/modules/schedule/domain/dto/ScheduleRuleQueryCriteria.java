package me.zhengjie.modules.schedule.domain.dto;

import lombok.Data;
import me.zhengjie.base.BaseEntity;

@Data
public class ScheduleRuleQueryCriteria extends BaseEntity {

    private Integer page = 0;

    private Integer size = 10;

    private String ruleName;

    private String ruleCode;

    private String ruleType;

    private Boolean enabled;
}
