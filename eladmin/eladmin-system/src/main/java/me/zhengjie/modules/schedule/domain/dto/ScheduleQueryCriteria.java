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
package me.zhengjie.modules.schedule.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 * 排班查询条件
 * @author Zheng Jie
 * @date 2025-04-10
 */
@Data
public class ScheduleQueryCriteria implements Serializable {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "员工ID")
    private Long userId;

    @ApiModelProperty(value = "多个员工ID")
    private Set<Long> userIds;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "多个部门ID")
    private Set<Long> deptIds;

    @ApiModelProperty(value = "班次ID")
    private Long shiftId;

    @ApiModelProperty(value = "排班日期")
    private Date scheduleDate;

    @ApiModelProperty(value = "排班日期范围-开始")
    private Date startDate;

    @ApiModelProperty(value = "排班日期范围-结束")
    private Date endDate;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "来源类型")
    private String sourceType;

    @ApiModelProperty(value = "是否有冲突")
    private Boolean conflictFlag;

    @ApiModelProperty(value = "创建时间")
    private List<Timestamp> createTime;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;

    @ApiModelProperty(value = "每页数据量", example = "10")
    private Integer size = 10;

    @ApiModelProperty(value = "偏移量", hidden = true)
    private long offset;
}
