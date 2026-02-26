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
package me.zhengjie.modules.schedule.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 批量排班DTO
 * @author Zheng Jie
 * @date 2026-02-26
 */
@Data
public class BatchScheduleDTO implements Serializable {

    @NotEmpty
    @ApiModelProperty(value = "员工ID列表", required = true)
    private List<Long> userIds;

    @NotNull
    @ApiModelProperty(value = "排班开始日期", required = true)
    private LocalDate startDate;

    @NotNull
    @ApiModelProperty(value = "排班结束日期", required = true)
    private LocalDate endDate;

    @ApiModelProperty(value = "班次类型ID（固定班次时使用）")
    private Long shiftTypeId;

    @ApiModelProperty(value = "轮班规则ID（轮班时使用）")
    private Long ruleId;

    @ApiModelProperty(value = "排班模板ID（使用模板时）")
    private Long templateId;

    @ApiModelProperty(value = "备注")
    private String remark;
}
