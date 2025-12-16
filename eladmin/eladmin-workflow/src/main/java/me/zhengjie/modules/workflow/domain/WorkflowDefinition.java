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
package me.zhengjie.modules.workflow.domain;

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

/**
 * 工作流定义
 * @author workflow-engine
 */
@Getter
@Setter
@TableName("workflow_definition")
public class WorkflowDefinition extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "workflow_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "流程名称")
    private String name;

    @ApiModelProperty(value = "流程描述")
    private String description;

    @NotBlank
    @ApiModelProperty(value = "流程定义JSON")
    private String definitionJson;

    @ApiModelProperty(value = "流程版本")
    private Integer version = 1;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled = true;

    @ApiModelProperty(value = "是否为最新版本")
    private Boolean isLatest = true;
}
