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
package me.zhengjie.modules.schedule.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.User;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工排班
 * @author Zheng Jie
 * @date 2026-02-26
 */
@Getter
@Setter
@TableName("schedule_employee_shift")
public class EmployeeShift extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "shift_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "员工ID")
    private Long userId;

    @TableField(exist = false)
    @ApiModelProperty(value = "员工")
    private User user;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @TableField(exist = false)
    @ApiModelProperty(value = "部门")
    private Dept dept;

    @NotNull
    @ApiModelProperty(value = "排班日期")
    private LocalDate shiftDate;

    @NotNull
    @ApiModelProperty(value = "班次类型ID")
    private Long shiftTypeId;

    @TableField(exist = false)
    @ApiModelProperty(value = "班次类型")
    private ShiftType shiftType;

    @ApiModelProperty(value = "实际上班时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "实际下班时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "状态：SCHEDULED-已排班，CONFIRMED-已确认，WORKING-工作中，COMPLETED-已完成，ABSENT-缺勤，LEAVE-请假")
    private String status;

    @ApiModelProperty(value = "来源：MANUAL-手动，AUTO-自动生成，IMPORT-导入")
    private String source;

    @ApiModelProperty(value = "应用的轮班规则ID")
    private Long ruleId;

    @ApiModelProperty(value = "备注")
    private String remark;
}
