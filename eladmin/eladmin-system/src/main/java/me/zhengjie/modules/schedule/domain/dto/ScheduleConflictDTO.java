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

import java.util.Date;
import java.util.List;

/**
 * @author Zheng Jie
 * @date 2025-04-10
 */
@Data
public class ScheduleConflictDTO {

    private Long userId;

    private String username;

    private String nickName;

    private Date scheduleDate;

    private List<ConflictInfo> conflicts;

    private Boolean hasConflict;

    @Data
    public static class ConflictInfo {
        private Long scheduleId;
        private Long shiftId;
        private String shiftName;
        private String shiftCode;
        private Date startTime;
        private Date endTime;
        private String conflictType;
        private String conflictMsg;
    }
}
