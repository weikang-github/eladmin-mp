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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 排班模板
 * @author Zheng Jie
 * @date 2026-02-26
 */
@Getter
@Setter
@TableName("schedule_template")
public class ScheduleTemplate extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "template_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "模板名称")
    private String name;

    @ApiModelProperty(value = "适用部门ID")
    private Long deptId;

    @TableField(exist = false)
    @ApiModelProperty(value = "部门")
    private Dept dept;

    @NotBlank
    @ApiModelProperty(value = "周排班模板，JSON格式")
    private String weekSchedule;

    @NotNull
    @ApiModelProperty(value = "是否启用")
    private Boolean isEnabled;

    @ApiModelProperty(value = "备注")
    private String remark;
}
