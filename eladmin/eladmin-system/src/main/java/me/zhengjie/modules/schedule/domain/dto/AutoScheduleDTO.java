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

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

/**
 * @author Zheng Jie
 * @date 2025-04-10
 */
@Data
public class AutoScheduleDTO {

    @NotNull
    private Integer year;

    @NotNull
    private Integer month;

    private Set<Long> deptIds;

    private Set<Long> userIds;

    @NotNull
    private Long rotationId;

    private Boolean checkConflict = true;

    private Boolean overwriteExisting = false;

    private Date startDate;

    private Date endDate;
}
