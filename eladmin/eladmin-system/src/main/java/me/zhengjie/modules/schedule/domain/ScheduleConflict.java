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
import me.zhengjie.modules.system.domain.User;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 排班冲突记录
 * @author Zheng Jie
 * @date 2026-02-26
 */
@Getter
@Setter
@TableName("schedule_conflict")
public class ScheduleConflict implements Serializable {

    @TableId(value = "conflict_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @ApiModelProperty(value = "员工ID")
    private Long userId;

    @TableField(exist = false)
    @ApiModelProperty(value = "员工")
    private User user;

    @ApiModelProperty(value = "冲突日期")
    private LocalDate shiftDate;

    @ApiModelProperty(value = "冲突类型：TIME_OVERLAP-时间重叠，CONTINUOUS_WORK-连续工作超时，REST_INSUFFICIENT-休息不足")
    private String conflictType;

    @ApiModelProperty(value = "冲突描述")
    private String conflictDesc;

    @ApiModelProperty(value = "关联排班ID")
    private Long relatedShiftId;

    @ApiModelProperty(value = "状态：UNRESOLVED-未解决，RESOLVED-已解决，IGNORED-已忽略")
    private String status;

    @ApiModelProperty(value = "解决人")
    private String resolvedBy;

    @ApiModelProperty(value = "解决时间")
    private LocalDateTime resolvedTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
