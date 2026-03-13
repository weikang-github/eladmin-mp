package me.zhengjie.modules.schedule.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
public class RotationRuleQueryCriteria implements Serializable {

    @ApiModelProperty(value = "ID")
    private Long ruleId;

    @ApiModelProperty(value = "模糊查询")
    private String blurry;

    @ApiModelProperty(value = "规则类型")
    private String type;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "创建时间")
    private List<Timestamp> createTime;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;

    @ApiModelProperty(value = "每页数据量", example = "10")
    private Integer size = 10;

    @ApiModelProperty(value = "偏移量", hidden = true)
    private long offset;
}
