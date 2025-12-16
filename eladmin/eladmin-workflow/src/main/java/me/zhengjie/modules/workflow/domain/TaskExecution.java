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
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 任务执行记录
 * @author workflow-engine
 */
@Getter
@Setter
@TableName("workflow_task_execution")
public class TaskExecution extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "task_execution_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "执行实例ID")
    private Long executionId;

    @NotNull
    @ApiModelProperty(value = "任务节点ID")
    private String taskId;

    @ApiModelProperty(value = "任务类型")
    private String taskType;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "任务配置")
    private String taskConfig;

    @ApiModelProperty(value = "任务状态")
    private String status;

    @ApiModelProperty(value = "任务输入参数")
    private String inputParams;

    @ApiModelProperty(value = "任务输出结果")
    private String outputResult;

    @ApiModelProperty(value = "执行开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp startTime;

    @ApiModelProperty(value = "执行结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp endTime;

    @ApiModelProperty(value = "执行耗时(毫秒)")
    private Long duration;

    @ApiModelProperty(value = "重试次数")
    private Integer retryCount = 0;

    @ApiModelProperty(value = "错误信息")
    private String errorMessage;

    @ApiModelProperty(value = "是否已完成")
    private Boolean isCompleted = false;

    @ApiModelProperty(value = "是否成功")
    private Boolean isSuccess = false;
}
