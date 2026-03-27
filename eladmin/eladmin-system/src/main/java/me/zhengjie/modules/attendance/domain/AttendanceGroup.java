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
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

/**
 * 考勤组实体类
 * @author Zheng Jie
 * @date 2025-03-27
 */
@Getter
@Setter
@TableName("attendance_group")
public class AttendanceGroup extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "group_id", type = IdType.AUTO)
    @ApiModelProperty(value = "考勤组ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "考勤组名称")
    private String groupName;

    @ApiModelProperty(value = "公司纬度")
    private BigDecimal companyLatitude;

    @ApiModelProperty(value = "公司经度")
    private BigDecimal companyLongitude;

    @ApiModelProperty(value = "允许打卡距离(米)")
    private Integer allowDistance = 100;

    @ApiModelProperty(value = "WiFi名称")
    private String wifiName;

    @ApiModelProperty(value = "WiFi MAC地址")
    private String wifiMac;

    @ApiModelProperty(value = "上班时间")
    private String workStartTime;

    @ApiModelProperty(value = "下班时间")
    private String workEndTime;

    @ApiModelProperty(value = "迟到分钟数")
    private Integer lateMinutes;

    @ApiModelProperty(value = "早退分钟数")
    private Integer earlyLeaveMinutes;

    @ApiModelProperty(value = "工作日(1-7表示周一到周日,逗号分隔)")
    private String workDays;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled = true;

    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField(exist = false)
    @ApiModelProperty(value = "部门ID集合")
    private Set<Long> deptIds;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttendanceGroup that = (AttendanceGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
