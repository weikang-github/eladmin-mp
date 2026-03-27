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
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

/**
 * 考勤组Excel导出VO
 * @author Zheng Jie
 * @date 2025-03-27
 */
@Getter
@Setter
public class AttendanceGroupExcelVo implements Serializable {

    @ExcelProperty("考勤组名称")
    @ApiModelProperty(value = "考勤组名称")
    private String groupName;

    @ExcelProperty("公司纬度")
    @ApiModelProperty(value = "公司纬度")
    private String companyLatitude;

    @ExcelProperty("公司经度")
    @ApiModelProperty(value = "公司经度")
    private String companyLongitude;

    @ExcelProperty("允许打卡距离(米)")
    @ApiModelProperty(value = "允许打卡距离(米)")
    private Integer allowDistance;

    @ExcelProperty("WiFi名称")
    @ApiModelProperty(value = "WiFi名称")
    private String wifiName;

    @ExcelProperty("上班时间")
    @ApiModelProperty(value = "上班时间")
    private String workStartTime;

    @ExcelProperty("下班时间")
    @ApiModelProperty(value = "下班时间")
    private String workEndTime;

    @ExcelProperty("迟到分钟数")
    @ApiModelProperty(value = "迟到分钟数")
    private Integer lateMinutes;

    @ExcelProperty("早退分钟数")
    @ApiModelProperty(value = "早退分钟数")
    private Integer earlyLeaveMinutes;

    @ExcelProperty("工作日")
    @ApiModelProperty(value = "工作日")
    private String workDays;

    @ExcelProperty("是否启用")
    @ApiModelProperty(value = "是否启用")
    private String enabled;

    @ExcelProperty("备注")
    @ApiModelProperty(value = "备注")
    private String remark;
}
