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
import java.util.Map;

/**
 * 排班日历VO
 * @author Zheng Jie
 * @date 2026-02-26
 */
@Data
public class ScheduleCalendarVO implements Serializable {

    @ApiModelProperty(value = "员工ID")
    private Long userId;

    @ApiModelProperty(value = "员工名称")
    private String username;

    @ApiModelProperty(value = "员工昵称")
    private String nickName;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "日期排班映射 key:日期(yyyy-MM-dd), value:班次信息")
    private Map<String, ShiftInfoVO> scheduleMap;

    /**
     * 班次信息
     */
    @Data
    public static class ShiftInfoVO implements Serializable {

        @ApiModelProperty(value = "排班ID")
        private Long shiftId;

        @ApiModelProperty(value = "班次类型ID")
        private Long shiftTypeId;

        @ApiModelProperty(value = "班次名称")
        private String shiftName;

        @ApiModelProperty(value = "班次编码")
        private String shiftCode;

        @ApiModelProperty(value = "显示颜色")
        private String color;

        @ApiModelProperty(value = "上班时间")
        private String startTime;

        @ApiModelProperty(value = "下班时间")
        private String endTime;

        @ApiModelProperty(value = "状态")
        private String status;

        @ApiModelProperty(value = "是否休息")
        private Boolean isRest;
    }
}
