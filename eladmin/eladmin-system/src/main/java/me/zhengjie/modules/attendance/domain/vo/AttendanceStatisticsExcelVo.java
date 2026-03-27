/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
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
package me.zhengjie.modules.attendance.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 考勤统计Excel导出VO
 * @author Zheng Jie
 * @date 2025-03-27
 */
@Getter
@Setter
public class AttendanceStatisticsExcelVo implements Serializable {

    @ExcelProperty("用户名")
    @ApiModelProperty(value = "用户名")
    private String username;

    @ExcelProperty("部门")
    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ExcelProperty("应出勤天数")
    @ApiModelProperty(value = "应出勤天数")
    private Integer shouldAttendanceDays;

    @ExcelProperty("实际出勤天数")
    @ApiModelProperty(value = "实际出勤天数")
    private Integer actualAttendanceDays;

    @ExcelProperty("迟到次数")
    @ApiModelProperty(value = "迟到次数")
    private Integer lateCount;

    @ExcelProperty("早退次数")
    @ApiModelProperty(value = "早退次数")
    private Integer earlyLeaveCount;

    @ExcelProperty("旷工天数")
    @ApiModelProperty(value = "旷工天数")
    private Integer absentDays;

    @ExcelProperty("缺卡次数")
    @ApiModelProperty(value = "缺卡次数")
    private Integer missCount;

    @ExcelProperty("请假天数")
    @ApiModelProperty(value = "请假天数")
    private BigDecimal leaveDays;

    @ExcelProperty("出勤率")
    @ApiModelProperty(value = "出勤率")
    private BigDecimal attendanceRate;

    @ExcelProperty("统计类型")
    @ApiModelProperty(value = "统计类型")
    private String statisticsType;

    @ExcelProperty("统计日期")
    @ApiModelProperty(value = "统计日期")
    private String statisticsDate;
}
