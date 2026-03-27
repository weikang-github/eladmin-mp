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
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.attendance.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 打卡请求DTO
 * @author Zheng Jie
 * @date 2025-03-27
 */
@Data
public class AttendanceCheckInDto implements Serializable {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @NotNull
    @ApiModelProperty(value = "考勤组ID")
    private Long groupId;

    @ApiModelProperty(value = "打卡类型(IN-上班打卡, OUT-下班打卡)")
    private String checkType;

    @ApiModelProperty(value = "纬度")
    private BigDecimal latitude;

    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;

    @ApiModelProperty(value = "打卡方式(GPS/WIFI)")
    private String checkInMethod;

    @ApiModelProperty(value = "WiFi名称")
    private String wifiName;

    @ApiModelProperty(value = "WiFi MAC地址")
    private String wifiMac;

    @ApiModelProperty(value = "设备信息")
    private String device;

    @ApiModelProperty(value = "IP地址")
    private String ipAddress;
}
