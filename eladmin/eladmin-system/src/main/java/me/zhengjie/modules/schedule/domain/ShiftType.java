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
import java.math.BigDecimal;
import java.sql.Time;

/**
 * 班次类型
 * @author Zheng Jie
 * @date 2026-02-26
 */
@Getter
@Setter
@TableName("schedule_shift_type")
public class ShiftType extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "shift_type_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "班次名称")
    private String name;

    @NotBlank
    @ApiModelProperty(value = "班次编码")
    private String code;

    @ApiModelProperty(value = "显示颜色")
    private String color;

    @ApiModelProperty(value = "上班时间")
    private Time startTime;

    @ApiModelProperty(value = "下班时间")
    private Time endTime;

    @ApiModelProperty(value = "工作时长（小时）")
    private BigDecimal workHours;

    @NotNull
    @ApiModelProperty(value = "是否休息")
    private Boolean isRest;

    @NotNull
    @ApiModelProperty(value = "是否启用")
    private Boolean isEnabled;

    @ApiModelProperty(value = "排序")
    private Integer sortOrder;

    @ApiModelProperty(value = "备注")
    private String remark;
}
