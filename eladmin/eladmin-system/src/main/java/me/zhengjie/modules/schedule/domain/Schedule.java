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
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 排班实体
 * @author Zheng Jie
 * @date 2025-04-10
 */
@Getter
@Setter
@TableName("sch_schedule")
public class Schedule extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "schedule_id", type = IdType.AUTO)
    @ApiModelProperty(value = "排班ID", hidden = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "员工ID")
    private Long userId;

    @NotNull
    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @NotNull
    @ApiModelProperty(value = "班次ID")
    private Long shiftId;

    @NotNull
    @ApiModelProperty(value = "排班日期")
    private Date scheduleDate;

    @ApiModelProperty(value = "实际开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "实际结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "来源类型")
    private String sourceType;

    @ApiModelProperty(value = "是否有冲突")
    private Boolean conflictFlag;

    @ApiModelProperty(value = "冲突信息")
    private String conflictMsg;

    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField(exist = false)
    @ApiModelProperty(value = "员工信息")
    private User user;

    @TableField(exist = false)
    @ApiModelProperty(value = "部门信息")
    private Dept dept;

    @TableField(exist = false)
    @ApiModelProperty(value = "班次信息")
    private Shift shift;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否检测冲突")
    private Boolean checkConflict;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Schedule schedule = (Schedule) o;
        return Objects.equals(id, schedule.id) &&
                Objects.equals(userId, schedule.userId) &&
                Objects.equals(scheduleDate, schedule.scheduleDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, scheduleDate);
    }
}
