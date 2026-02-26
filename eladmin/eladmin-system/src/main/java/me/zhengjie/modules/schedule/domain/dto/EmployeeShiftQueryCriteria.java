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
import java.util.List;

/**
 * 员工排班查询条件
 * @author Zheng Jie
 * @date 2026-02-26
 */
@Data
public class EmployeeShiftQueryCriteria implements Serializable {

    @ApiModelProperty(value = "员工ID")
    private Long userId;

    @ApiModelProperty(value = "员工ID列表")
    private List<Long> userIds;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "排班日期开始")
    private LocalDate startDate;

    @ApiModelProperty(value = "排班日期结束")
    private LocalDate endDate;

    @ApiModelProperty(value = "班次类型ID")
    private Long shiftTypeId;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "来源")
    private String source;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;

    @ApiModelProperty(value = "每页数据量", example = "10")
    private Integer size = 10;
}
