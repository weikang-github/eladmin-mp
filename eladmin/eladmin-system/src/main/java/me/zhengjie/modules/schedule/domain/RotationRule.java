/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.schedule.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 轮班规则
 * @author Zheng Jie
 * @date 2026-02-26
 */
@Getter
@Setter
@TableName("schedule_rotation_rule")
public class RotationRule extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "rule_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "规则名称")
    private String name;

    @NotBlank
    @ApiModelProperty(value = "规则编码")
    private String code;

    @NotBlank
    @ApiModelProperty(value = "轮班类型：TWO_SHIFT-两班倒，THREE_SHIFT-三班倒，FOUR_SHIFT-四班三运转，CUSTOM-自定义")
    private String rotationType;

    @NotNull
    @ApiModelProperty(value = "轮班周期（天）")
    private Integer cycleDays;

    @NotBlank
    @ApiModelProperty(value = "班次序列，JSON数组格式")
    private String shiftSequence;

    @NotNull
    @ApiModelProperty(value = "是否启用")
    private Boolean isEnabled;

    @ApiModelProperty(value = "备注")
    private String remark;
}
