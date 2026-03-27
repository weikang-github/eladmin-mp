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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 考勤节假日实体类
 * @author Zheng Jie
 * @date 2025-03-27
 */
@Getter
@Setter
@TableName("attendance_holiday")
public class AttendanceHoliday extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "holiday_id", type = IdType.AUTO)
    @ApiModelProperty(value = "节假日ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "节假日名称")
    private String holidayName;

    @NotNull
    @ApiModelProperty(value = "日期")
    private Date holidayDate;

    @ApiModelProperty(value = "类型(HOLIDAY-节假日, WORKDAY-调休工作日)")
    private String holidayType;

    @ApiModelProperty(value = "年份")
    private Integer year;

    @ApiModelProperty(value = "备注")
    private String remark;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttendanceHoliday that = (AttendanceHoliday) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
