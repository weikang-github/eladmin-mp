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
import lombok.NoArgsConstructor;
import lombok.Setter;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import me.zhengjie.base.BaseEntity;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
* 考勤记录实体类
* @author Your Name
* @date 2024-03-27
*/
@Getter
@Setter
@NoArgsConstructor
@TableName("tool_attendance_record")
public class AttendanceRecord extends BaseEntity implements Serializable {

    @TableId(value = "record_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "考勤组ID")
    private Long groupId;

    @ApiModelProperty(value = "考勤日期")
    private LocalDate attendanceDate;

    @ApiModelProperty(value = "上班打卡时间")
    private LocalTime checkInTime;

    @ApiModelProperty(value = "下班打卡时间")
    private LocalTime checkOutTime;

    @ApiModelProperty(value = "打卡方式：GPS/WIFI")
    private String checkMethod;

    @ApiModelProperty(value = "打卡纬度")
    private Double latitude;

    @ApiModelProperty(value = "打卡经度")
    private Double longitude;

    @ApiModelProperty(value = "WiFi名称")
    private String wifiName;

    @ApiModelProperty(value = "WiFi MAC地址")
    private String wifiMac;

    @ApiModelProperty(value = "出勤状态：正常/迟到/早退/缺卡/旷工")
    private String status;

    @ApiModelProperty(value = "备注")
    private String remark;

    public void copy(AttendanceRecord source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
