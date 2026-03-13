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
import java.util.Objects;

/**
 * 排班规则实体
 * @author Zheng Jie
 * @date 2025-04-10
 */
@Getter
@Setter
@TableName("sch_schedule_rule")
public class ScheduleRule extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "rule_id", type = IdType.AUTO)
    @ApiModelProperty(value = "规则ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "规则名称")
    private String name;

    @NotBlank
    @ApiModelProperty(value = "规则类型")
    private String type;

    @NotBlank
    @ApiModelProperty(value = "规则表达式")
    private String expression;

    @ApiModelProperty(value = "优先级")
    private Integer priority;

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
        ScheduleRule that = (ScheduleRule) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
