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

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 自动生成排班DTO
 * @author Zheng Jie
 * @date 2026-02-26
 */
@Data
public class GenerateScheduleDTO implements Serializable {

    @ApiModelProperty(value = "员工ID列表（为空则生成全部有规则的员工的排班）")
    private List<Long> userIds;

    @NotNull
    @ApiModelProperty(value = "排班年份", required = true)
    private Integer year;

    @NotNull
    @ApiModelProperty(value = "排班月份", required = true)
    private Integer month;

    @ApiModelProperty(value = "是否覆盖已有排班")
    private Boolean overwrite = false;
}
