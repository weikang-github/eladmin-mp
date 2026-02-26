package me.zhengjie.modules.schedule.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShiftRuleQueryCriteria {

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "规则类型")
    private String ruleType;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "适用部门ID")
    private Long deptId;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;

    @ApiModelProperty(value = "每页数据量", example = "10")
    private Integer size = 10;
}
