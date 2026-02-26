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
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import me.zhengjie.modules.system.domain.User;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 员工轮班规则关联
 * @author Zheng Jie
 * @date 2026-02-26
 */
@Getter
@Setter
@TableName("schedule_employee_rule")
public class EmployeeRule extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "员工ID")
    private Long userId;

    @TableField(exist = false)
    @ApiModelProperty(value = "员工")
    private User user;

    @NotNull
    @ApiModelProperty(value = "轮班规则ID")
    private Long ruleId;

    @TableField(exist = false)
    @ApiModelProperty(value = "轮班规则")
    private RotationRule rule;

    @NotNull
    @ApiModelProperty(value = "规则生效开始日期")
    private LocalDate startDate;

    @ApiModelProperty(value = "规则生效结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "周期开始索引（用于轮班规则）")
    private Integer cycleStartIndex;

    @NotNull
    @ApiModelProperty(value = "是否启用")
    private Boolean isEnabled;

    @ApiModelProperty(value = "备注")
    private String remark;
}
