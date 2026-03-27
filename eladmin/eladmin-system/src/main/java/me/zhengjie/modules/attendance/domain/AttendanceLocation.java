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

/**
* 考勤地点实体类
* @author Your Name
* @date 2024-03-27
*/
@Getter
@Setter
@NoArgsConstructor
@TableName("tool_attendance_location")
public class AttendanceLocation extends BaseEntity implements Serializable {

    @TableId(value = "location_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @ApiModelProperty(value = "地点名称")
    private String locationName;

    @ApiModelProperty(value = "纬度")
    private Double latitude;

    @ApiModelProperty(value = "经度")
    private Double longitude;

    @ApiModelProperty(value = "允许偏差距离（米）")
    private Integer radius;

    @ApiModelProperty(value = "WiFi名称")
    private String wifiName;

    @ApiModelProperty(value = "WiFi MAC地址")
    private String wifiMac;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "备注")
    private String remark;

    public void copy(AttendanceLocation source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
