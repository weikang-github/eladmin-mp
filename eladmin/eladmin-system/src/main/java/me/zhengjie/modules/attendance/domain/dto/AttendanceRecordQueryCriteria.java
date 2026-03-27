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
package me.zhengjie.modules.attendance.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDate;

/**
* 考勤记录查询条件类
* @author Your Name
* @date 2024-03-27
*/
@Data
public class AttendanceRecordQueryCriteria {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "考勤组ID")
    private Long groupId;

    @ApiModelProperty(value = "开始日期")
    private LocalDate startDate;

    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "出勤状态")
    private String status;
}
