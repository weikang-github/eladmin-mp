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
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * 考勤记录实体类
 * @author Zheng Jie
 * @date 2025-03-27
 */
@Getter
@Setter
@TableName("attendance_record")
public class AttendanceRecord extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "record_id", type = IdType.AUTO)
    @ApiModelProperty(value = "记录ID", hidden = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @NotNull
    @ApiModelProperty(value = "考勤组ID")
    private Long groupId;

    @ApiModelProperty(value = "打卡日期")
    private Date attendanceDate;

    @ApiModelProperty(value = "上班打卡时间")
    private Date checkInTime;

    @ApiModelProperty(value = "下班打卡时间")
    private Date checkOutTime;

    @ApiModelProperty(value = "上班打卡纬度")
    private BigDecimal checkInLatitude;

    @ApiModelProperty(value = "上班打卡经度")
    private BigDecimal checkInLongitude;

    @ApiModelProperty(value = "下班打卡纬度")
    private BigDecimal checkOutLatitude;

    @ApiModelProperty(value = "下班打卡经度")
    private BigDecimal checkOutLongitude;

    @ApiModelProperty(value = "上班打卡方式(GPS/WIFI)")
    private String checkInType;

    @ApiModelProperty(value = "下班打卡方式(GPS/WIFI)")
    private String checkOutType;

    @ApiModelProperty(value = "上班打卡状态(NORMAL/LATE/MISS)")
    private String checkInStatus;

    @ApiModelProperty(value = "下班打卡状态(NORMAL/EARLY/MISS)")
    private String checkOutStatus;

    @ApiModelProperty(value = "当日考勤状态(NORMAL/LATE/EARLY/ABSENT/MISS)")
    private String attendanceStatus;

    @ApiModelProperty(value = "上班WiFi名称")
    private String checkInWifiName;

    @ApiModelProperty(value = "下班WiFi名称")
    private String checkOutWifiName;

    @ApiModelProperty(value = "上班WiFi MAC")
    private String checkInWifiMac;

    @ApiModelProperty(value = "下班WiFi MAC")
    private String checkOutWifiMac;

    @ApiModelProperty(value = "打卡设备")
    private String device;

    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户名称")
    private String username;

    @TableField(exist = false)
    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttendanceRecord that = (AttendanceRecord) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
