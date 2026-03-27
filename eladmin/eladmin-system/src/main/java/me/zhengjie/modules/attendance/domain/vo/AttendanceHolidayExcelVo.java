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
 * 节假日Excel导出VO
 * @author Zheng Jie
 * @date 2025-03-27
 */
@Getter
@Setter
public class AttendanceHolidayExcelVo implements Serializable {

    @ExcelProperty("节假日名称")
    @ApiModelProperty(value = "节假日名称")
    private String holidayName;

    @ExcelProperty("节假日日期")
    @DateTimeFormat("yyyy-MM-dd")
    @ApiModelProperty(value = "节假日日期")
    private Date holidayDate;

    @ExcelProperty("节假日类型")
    @ApiModelProperty(value = "节假日类型")
    private String holidayType;

    @ExcelProperty("年份")
    @ApiModelProperty(value = "年份")
    private Integer year;

    @ExcelProperty("月份")
    @ApiModelProperty(value = "月份")
    private Integer month;

    @ExcelProperty("备注")
    @ApiModelProperty(value = "备注")
    private String remark;
}
