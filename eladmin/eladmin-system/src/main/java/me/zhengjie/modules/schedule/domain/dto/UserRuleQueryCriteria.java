package me.zhengjie.modules.schedule.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserRuleQueryCriteria {

    @ApiModelProperty(value = "员工ID")
    private Long userId;

    @ApiModelProperty(value = "规则ID")
    private Long ruleId;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;

    @ApiModelProperty(value = "每页数据量", example = "10")
    private Integer size = 10;
}
