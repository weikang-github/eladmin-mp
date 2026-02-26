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

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 排班冲突查询条件
 * @author Zheng Jie
 * @date 2026-02-26
 */
@Data
public class ScheduleConflictQueryCriteria implements Serializable {

    @ApiModelProperty(value = "员工ID")
    private Long userId;

    @ApiModelProperty(value = "冲突日期开始")
    private LocalDate startDate;

    @ApiModelProperty(value = "冲突日期结束")
    private LocalDate endDate;

    @ApiModelProperty(value = "冲突类型")
    private String conflictType;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;

    @ApiModelProperty(value = "每页数据量", example = "10")
    private Integer size = 10;
}
