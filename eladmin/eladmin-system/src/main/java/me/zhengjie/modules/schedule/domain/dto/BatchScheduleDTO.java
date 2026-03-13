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
import java.sql.Date;
import java.util.List;
import java.util.Set;

/**
 * 批量排班DTO
 * @author Zheng Jie
 * @date 2025-04-10
 */
@Data
public class BatchScheduleDTO {

    @NotNull
    @ApiModelProperty(value = "员工ID列表")
    private Set<Long> userIds;

    @NotNull
    @ApiModelProperty(value = "班次ID")
    private Long shiftId;

    @NotNull
    @ApiModelProperty(value = "排班日期列表")
    private List<Date> scheduleDates;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否自动检测冲突")
    private Boolean checkConflict = true;
}
