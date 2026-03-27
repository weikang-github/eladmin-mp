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
package me.zhengjie.modules.attendance.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Date;

/**
 * 考勤记录Excel导出VO
 * @author Zheng Jie
 * @date 2025-03-27
 */
@Getter
@Setter
public class AttendanceRecordExcelVo implements Serializable {

    @ExcelProperty("用户名")
    @ApiModelProperty(value = "用户名")
    private String username;

    @ExcelProperty("部门")
    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ExcelProperty("考勤日期")
    @DateTimeFormat("yyyy-MM-dd")
    @ApiModelProperty(value = "考勤日期")
    private Date attendanceDate;

    @ExcelProperty("上班打卡时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "上班打卡时间")
    private Date checkInTime;

    @ExcelProperty("下班打卡时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "下班打卡时间")
    private Date checkOutTime;

    @ExcelProperty("上班打卡状态")
    @ApiModelProperty(value = "上班打卡状态")
    private String checkInStatus;

    @ExcelProperty("下班打卡状态")
    @ApiModelProperty(value = "下班打卡状态")
    private String checkOutStatus;

    @ExcelProperty("当日考勤状态")
    @ApiModelProperty(value = "当日考勤状态")
    private String attendanceStatus;

    @ExcelProperty("上班打卡方式")
    @ApiModelProperty(value = "上班打卡方式")
    private String checkInType;

    @ExcelProperty("下班打卡方式")
    @ApiModelProperty(value = "下班打卡方式")
    private String checkOutType;

    @ExcelProperty("备注")
    @ApiModelProperty(value = "备注")
    private String remark;
}
