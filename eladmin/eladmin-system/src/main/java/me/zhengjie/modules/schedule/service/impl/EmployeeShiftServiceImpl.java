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
package me.zhengjie.modules.schedule.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.schedule.domain.*;
import me.zhengjie.modules.schedule.domain.dto.*;
import me.zhengjie.modules.schedule.mapper.EmployeeShiftMapper;
import me.zhengjie.modules.schedule.service.*;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeShiftServiceImpl extends ServiceImpl<EmployeeShiftMapper, EmployeeShift> implements EmployeeShiftService {

    private final EmployeeShiftMapper employeeShiftMapper;
    private final ShiftTypeService shiftTypeService;
    private final RotationRuleService rotationRuleService;
    private final EmployeeRuleService employeeRuleService;
    private final ScheduleConflictService scheduleConflictService;
    private final UserService userService;

    @Override
    public PageResult<EmployeeShift> queryAll(EmployeeShiftQueryCriteria criteria, Page<Object> page) {
        List<EmployeeShift> list = employeeShiftMapper.findAll(criteria);
        return PageUtil.toPage(list, (long) list.size());
    }

    @Override
    public List<EmployeeShift> queryAll(EmployeeShiftQueryCriteria criteria) {
        return employeeShiftMapper.findAll(criteria);
    }

    @Override
    public EmployeeShift findById(Long id) {
        return getById(id);
    }

    @Override
    public List<EmployeeShift> findByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return employeeShiftMapper.findByUserAndDateRange(userId, startDate, endDate);
    }

    @Override
    public List<EmployeeShift> findByDeptAndDateRange(Long deptId, LocalDate startDate, LocalDate endDate) {
        return employeeShiftMapper.findByDeptAndDateRange(deptId, startDate, endDate);
    }

    @Override
    public List<ScheduleCalendarVO> getScheduleCalendar(EmployeeShiftQueryCriteria criteria) {
        List<EmployeeShift> shifts = employeeShiftMapper.findAll(criteria);
        Map<Long, List<EmployeeShift>> userShiftMap = shifts.stream()
                .collect(Collectors.groupingBy(EmployeeShift::getUserId));
        List<ScheduleCalendarVO> result = new ArrayList<>();
        for (Map.Entry<Long, List<EmployeeShift>> entry : userShiftMap.entrySet()) {
            Long userId = entry.getKey();
            List<EmployeeShift> userShifts = entry.getValue();
            if (userShifts.isEmpty()) continue;
            ScheduleCalendarVO vo = new ScheduleCalendarVO();
            EmployeeShift firstShift = userShifts.get(0);
            vo.setUserId(userId);
            if (firstShift.getUser() != null) {
                vo.setUsername(firstShift.getUser().getUsername());
                vo.setNickName(firstShift.getUser().getNickName());
            }
            if (firstShift.getDept() != null) {
                vo.setDeptName(firstShift.getDept().getName());
            }
            Map<String, ScheduleCalendarVO.ShiftInfoVO> scheduleMap = new HashMap<>();
            for (EmployeeShift shift : userShifts) {
                ScheduleCalendarVO.ShiftInfoVO info = new ScheduleCalendarVO.ShiftInfoVO();
                info.setShiftId(shift.getId());
                info.setShiftTypeId(shift.getShiftTypeId());
                if (shift.getShiftType() != null) {
                    info.setShiftName(shift.getShiftType().getName());
                    info.setShiftCode(shift.getShiftType().getCode());
                    info.setColor(shift.getShiftType().getColor());
                    info.setIsRest(shift.getShiftType().getIsRest());
                    if (shift.getShiftType().getStartTime() != null) {
                        info.setStartTime(shift.getShiftType().getStartTime().toString());
                    }
                    if (shift.getShiftType().getEndTime() != null) {
                        info.setEndTime(shift.getShiftType().getEndTime().toString());
                    }
                }
                info.setStatus(shift.getStatus());
                scheduleMap.put(shift.getShiftDate().toString(), info);
            }
            vo.setScheduleMap(scheduleMap);
            result.add(vo);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(EmployeeShift resources) {
        EmployeeShift existing = employeeShiftMapper.findByUserAndDate(resources.getUserId(), resources.getShiftDate());
        if (existing != null) {
            throw new BadRequestException("该员工在" + resources.getShiftDate() + "已有排班");
        }
        resources.setStatus("SCHEDULED");
        resources.setSource("MANUAL");
        save(resources);
        checkConflict(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EmployeeShift resources) {
        EmployeeShift old = getById(resources.getId());
        if (old == null) {
            throw new BadRequestException("排班记录不存在");
        }
        if (!old.getUserId().equals(resources.getUserId()) || !old.getShiftDate().equals(resources.getShiftDate())) {
            EmployeeShift existing = employeeShiftMapper.findByUserAndDate(resources.getUserId(), resources.getShiftDate());
            if (existing != null && !existing.getId().equals(resources.getId())) {
                throw new BadRequestException("该员工在" + resources.getShiftDate() + "已有排班");
            }
        }
        updateById(resources);
        checkConflict(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSchedule(BatchScheduleDTO dto) {
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new BadRequestException("开始日期不能大于结束日期");
        }
        List<EmployeeShift> shifts = new ArrayList<>();
        LocalDate currentDate = dto.getStartDate();
        ShiftType shiftType = null;
        RotationRule rule = null;
        if (dto.getShiftTypeId() != null) {
            shiftType = shiftTypeService.findById(dto.getShiftTypeId());
            if (shiftType == null) {
                throw new BadRequestException("班次类型不存在");
            }
        } else if (dto.getRuleId() != null) {
            rule = rotationRuleService.findById(dto.getRuleId());
            if (rule == null) {
                throw new BadRequestException("轮班规则不存在");
            }
        } else {
            throw new BadRequestException("请选择班次类型或轮班规则");
        }
        int dayIndex = 0;
        while (!currentDate.isAfter(dto.getEndDate())) {
            for (Long userId : dto.getUserIds()) {
                EmployeeShift shift = new EmployeeShift();
                shift.setUserId(userId);
                shift.setShiftDate(currentDate);
                shift.setStatus("SCHEDULED");
                shift.setSource("MANUAL");
                shift.setRemark(dto.getRemark());
                shift.setCreateBy(SecurityUtils.getCurrentUsername());
                shift.setCreateTime(LocalDateTime.now());
                if (rule != null) {
                    List<String> sequence = JSON.parseArray(rule.getShiftSequence(), String.class);
                    int index = dayIndex % sequence.size();
                    String shiftCode = sequence.get(index);
                    ShiftType type = shiftTypeService.findByCode(shiftCode);
                    if (type != null) {
                        shift.setShiftTypeId(type.getId());
                    }
                    shift.setRuleId(rule.getId());
                } else {
                    shift.setShiftTypeId(shiftType.getId());
                }
                shifts.add(shift);
            }
            currentDate = currentDate.plusDays(1);
            dayIndex++;
        }
        if (!shifts.isEmpty()) {
            for (Long userId : dto.getUserIds()) {
                employeeShiftMapper.deleteByDateRange(userId, dto.getStartDate(), dto.getEndDate());
            }
            for (EmployeeShift shift : shifts) {
                save(shift);
            }
            for (EmployeeShift shift : shifts) {
                checkConflict(shift);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateSchedule(GenerateScheduleDTO dto) {
        LocalDate startDate = LocalDate.of(dto.getYear(), dto.getMonth(), 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        List<EmployeeRule> rules;
        if (dto.getUserIds() != null && !dto.getUserIds().isEmpty()) {
            rules = new ArrayList<>();
            for (Long userId : dto.getUserIds()) {
                EmployeeRule rule = employeeRuleService.findActiveRuleByUser(userId, startDate);
                if (rule != null) {
                    rules.add(rule);
                }
            }
        } else {
            rules = employeeRuleService.findAllEnabled();
        }
        for (EmployeeRule employeeRule : rules) {
            generateScheduleForEmployee(employeeRule, startDate, endDate, dto.getOverwrite());
        }
    }

    private void generateScheduleForEmployee(EmployeeRule employeeRule, LocalDate startDate, LocalDate endDate, boolean overwrite) {
        RotationRule rule = rotationRuleService.findById(employeeRule.getRuleId());
        if (rule == null || !rule.getIsEnabled()) {
            return;
        }
        List<String> sequence = JSON.parseArray(rule.getShiftSequence(), String.class);
        if (sequence == null || sequence.isEmpty()) {
            return;
        }
        Long userId = employeeRule.getUserId();
        User user = userService.findById(userId);
        if (user == null) {
            return;
        }
        if (overwrite) {
            employeeShiftMapper.deleteByDateRange(userId, startDate, endDate);
        }
        List<EmployeeShift> shifts = new ArrayList<>();
        LocalDate currentDate = startDate;
        int cycleIndex = employeeRule.getCycleStartIndex() != null ? employeeRule.getCycleStartIndex() : 0;
        while (!currentDate.isAfter(endDate)) {
            if (!overwrite) {
                EmployeeShift existing = employeeShiftMapper.findByUserAndDate(userId, currentDate);
                if (existing != null) {
                    currentDate = currentDate.plusDays(1);
                    cycleIndex = (cycleIndex + 1) % sequence.size();
                    continue;
                }
            }
            String shiftCode = sequence.get(cycleIndex % sequence.size());
            ShiftType shiftType = shiftTypeService.findByCode(shiftCode);
            if (shiftType != null) {
                EmployeeShift shift = new EmployeeShift();
                shift.setUserId(userId);
                shift.setDeptId(user.getDeptId());
                shift.setShiftDate(currentDate);
                shift.setShiftTypeId(shiftType.getId());
                shift.setStatus("SCHEDULED");
                shift.setSource("AUTO");
                shift.setRuleId(rule.getId());
                shift.setCreateBy("system");
                shift.setCreateTime(LocalDateTime.now());
                shifts.add(shift);
            }
            currentDate = currentDate.plusDays(1);
            cycleIndex = (cycleIndex + 1) % sequence.size();
        }
        if (!shifts.isEmpty()) {
            for (EmployeeShift shift : shifts) {
                save(shift);
            }
            for (EmployeeShift shift : shifts) {
                checkConflict(shift);
            }
        }
        employeeRule.setCycleStartIndex(cycleIndex);
        employeeRuleService.update(employeeRule);
    }

    private void checkConflict(EmployeeShift shift) {
        checkContinuousWork(shift);
        checkRestInsufficient(shift);
    }

    private void checkContinuousWork(EmployeeShift shift) {
        LocalDate checkStart = shift.getShiftDate().minusDays(6);
        LocalDate checkEnd = shift.getShiftDate();
        List<EmployeeShift> recentShifts = employeeShiftMapper.findByUserAndDateRange(
                shift.getUserId(), checkStart, checkEnd);
        long workDays = recentShifts.stream()
                .filter(s -> s.getShiftType() != null && !s.getShiftType().getIsRest())
                .count();
        if (workDays >= 7) {
            ScheduleConflict conflict = new ScheduleConflict();
            conflict.setUserId(shift.getUserId());
            conflict.setShiftDate(shift.getShiftDate());
            conflict.setConflictType("CONTINUOUS_WORK");
            conflict.setConflictDesc("连续工作超过7天，请安排休息");
            conflict.setRelatedShiftId(shift.getId());
            conflict.setStatus("UNRESOLVED");
            conflict.setCreateTime(LocalDateTime.now());
            scheduleConflictService.save(conflict);
        }
    }

    private void checkRestInsufficient(EmployeeShift shift) {
        ShiftType shiftType = shiftTypeService.findById(shift.getShiftTypeId());
        if (shiftType == null) return;
        if ("NIGHT".equals(shiftType.getCode())) {
            LocalDate nextDay = shift.getShiftDate().plusDays(1);
            EmployeeShift nextDayShift = employeeShiftMapper.findByUserAndDate(shift.getUserId(), nextDay);
            if (nextDayShift != null) {
                ShiftType nextDayType = shiftTypeService.findById(nextDayShift.getShiftTypeId());
                if (nextDayType != null && "MORNING".equals(nextDayType.getCode())) {
                    ScheduleConflict conflict = new ScheduleConflict();
                    conflict.setUserId(shift.getUserId());
                    conflict.setShiftDate(nextDay);
                    conflict.setConflictType("REST_INSUFFICIENT");
                    conflict.setConflictDesc("夜班后第二天安排早班，休息时间不足");
                    conflict.setRelatedShiftId(nextDayShift.getId());
                    conflict.setStatus("UNRESOLVED");
                    conflict.setCreateTime(LocalDateTime.now());
                    scheduleConflictService.save(conflict);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids) {
        removeByIds(ids);
    }

    @Override
    public void download(List<EmployeeShift> data, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (EmployeeShift dto : data) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("员工姓名", dto.getUser() != null ? dto.getUser().getNickName() : "");
            map.put("部门", dto.getDept() != null ? dto.getDept().getName() : "");
            map.put("排班日期", dto.getShiftDate());
            map.put("班次", dto.getShiftType() != null ? dto.getShiftType().getName() : "");
            map.put("状态", getStatusText(dto.getStatus()));
            map.put("来源", getSourceText(dto.getSource()));
            map.put("备注", dto.getRemark());
            map.put("创建时间", dto.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    private String getStatusText(String status) {
        switch (status) {
            case "SCHEDULED": return "已排班";
            case "CONFIRMED": return "已确认";
            case "WORKING": return "工作中";
            case "COMPLETED": return "已完成";
            case "ABSENT": return "缺勤";
            case "LEAVE": return "请假";
            default: return status;
        }
    }

    private String getSourceText(String source) {
        switch (source) {
            case "MANUAL": return "手动";
            case "AUTO": return "自动生成";
            case "IMPORT": return "导入";
            default: return source;
        }
    }
}
