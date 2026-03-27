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
package me.zhengjie.modules.attendance.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 考勤统计实体类
 * @author Zheng Jie
 * @date 2025-03-27
 */
@Getter
@Setter
public class AttendanceStatistics implements Serializable {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "应出勤天数")
    private Integer shouldAttendanceDays;

    @ApiModelProperty(value = "实际出勤天数")
    private Integer actualAttendanceDays;

    @ApiModelProperty(value = "迟到次数")
    private Integer lateCount;

    @ApiModelProperty(value = "早退次数")
    private Integer earlyLeaveCount;

    @ApiModelProperty(value = "旷工天数")
    private Integer absentDays;

    @ApiModelProperty(value = "缺卡次数")
    private Integer missCount;

    @ApiModelProperty(value = "请假天数")
    private BigDecimal leaveDays;

    @ApiModelProperty(value = "出勤率")
    private BigDecimal attendanceRate;

    @ApiModelProperty(value = "统计类型(DAY/WEEK/MONTH)")
    private String statisticsType;

    @ApiModelProperty(value = "统计日期")
    private String statisticsDate;
}
