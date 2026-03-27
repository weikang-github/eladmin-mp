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
import java.time.LocalTime;
import java.util.List;

/**
* 考勤组实体类
* @author Your Name
* @date 2024-03-27
*/
@Getter
@Setter
@NoArgsConstructor
@TableName("tool_attendance_group")
public class AttendanceGroup extends BaseEntity implements Serializable {

    @TableId(value = "group_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @ApiModelProperty(value = "考勤组名称")
    private String groupName;

    @ApiModelProperty(value = "部门ID列表（JSON格式）")
    private String deptIds;

    @ApiModelProperty(value = "工作日设置（JSON格式，如：[1,2,3,4,5]表示周一到周五）")
    private String workDays;

    @ApiModelProperty(value = "上班时间")
    private LocalTime workStartTime;

    @ApiModelProperty(value = "下班时间")
    private LocalTime workEndTime;

    @ApiModelProperty(value = "迟到分钟数")
    private Integer lateMinutes;

    @ApiModelProperty(value = "早退分钟数")
    private Integer earlyMinutes;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "备注")
    private String remark;

    public void copy(AttendanceGroup source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
