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
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Time;
import java.util.Objects;

/**
 * 班次实体
 * @author Zheng Jie
 * @date 2025-04-10
 */
@Getter
@Setter
@TableName("sch_shift")
public class Shift extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "shift_id", type = IdType.AUTO)
    @ApiModelProperty(value = "班次ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "班次名称")
    private String name;

    @NotBlank
    @ApiModelProperty(value = "班次编码")
    private String code;

    @NotNull
    @ApiModelProperty(value = "开始时间")
    private Time startTime;

    @NotNull
    @ApiModelProperty(value = "结束时间")
    private Time endTime;

    @NotBlank
    @ApiModelProperty(value = "班次类型")
    private String type;

    @ApiModelProperty(value = "是否跨天")
    private Boolean isCrossDay;

    @ApiModelProperty(value = "休息开始时间")
    private Time breakStartTime;

    @ApiModelProperty(value = "休息结束时间")
    private Time breakEndTime;

    @ApiModelProperty(value = "显示颜色")
    private String color;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

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
        Shift shift = (Shift) o;
        return Objects.equals(id, shift.id) &&
                Objects.equals(code, shift.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code);
    }
}
