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
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.attendance.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * 考勤节假日查询条件
 * @author Zheng Jie
 * @date 2025-03-27
 */
@Data
public class AttendanceHolidayQueryCriteria implements Serializable {

    @ApiModelProperty(value = "节假日名称")
    private String holidayName;

    @ApiModelProperty(value = "年份")
    private Integer year;

    @ApiModelProperty(value = "类型")
    private String holidayType;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;

    @ApiModelProperty(value = "每页数据量", example = "10")
    private Integer size = 10;

    @ApiModelProperty(value = "偏移量", hidden = true)
    private long offset;
}
