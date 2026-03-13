package me.zhengjie.modules.schedule.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.schedule.domain.RotationRule;
import me.zhengjie.modules.schedule.domain.Schedule;
import me.zhengjie.modules.schedule.domain.ScheduleGroup;
import me.zhengjie.modules.schedule.domain.ScheduleGroupMember;
import me.zhengjie.modules.schedule.domain.Shift;
import me.zhengjie.modules.schedule.domain.dto.AutoScheduleRequest;
import me.zhengjie.modules.schedule.domain.dto.BatchScheduleRequest;
import me.zhengjie.modules.schedule.domain.dto.ScheduleConflictResult;
import me.zhengjie.modules.schedule.domain.dto.ScheduleQueryCriteria;
import me.zhengjie.modules.schedule.mapper.RotationRuleMapper;
import me.zhengjie.modules.schedule.mapper.ScheduleGroupMemberMapper;
import me.zhengjie.modules.schedule.mapper.ScheduleMapper;
import me.zhengjie.modules.schedule.service.ScheduleService;
import me.zhengjie.modules.schedule.service.ShiftService;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.mapper.UserMapper;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements ScheduleService {

    private final ScheduleMapper scheduleMapper;
    private final ShiftService shiftService;
    private final UserMapper userMapper;
    private final RotationRuleMapper rotationRuleMapper;
    private final ScheduleGroupMemberMapper scheduleGroupMemberMapper;

    @Override
    public Schedule findById(Long id) {
        return getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Schedule resources) {
        ScheduleConflictResult conflictResult = detectConflicts(resources);
        if (conflictResult.getHasConflict()) {
            throw new BadRequestException("排班存在冲突，请检查冲突信息");
        }
        save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Schedule resources) {
        ScheduleConflictResult conflictResult = detectConflicts(resources);
        if (conflictResult.getHasConflict()) {
            throw new BadRequestException("排班存在冲突，请检查冲突信息");
        }
        updateById(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        removeByIds(ids);
    }

    @Override
    public PageResult<Schedule> queryAll(ScheduleQueryCriteria criteria, Page<Object> page) {
        criteria.setOffset(page.offset());
        List<Schedule> schedules = scheduleMapper.findAll(criteria);
        long total = count();
        return PageUtil.toPage(schedules, total);
    }

    @Override
    public List<Schedule> queryAll(ScheduleQueryCriteria criteria) {
        return scheduleMapper.findAll(criteria);
    }

    @Override
    public void download(List<Schedule> queryAll, HttpServletResponse response) throws IOException {
        // Implementation for Excel download
    }

    @Override
    public List<Schedule> findByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return scheduleMapper.findByUserIdAndDateRange(userId, startDate, endDate);
    }

    @Override
    public List<Schedule> findByDeptIdAndDateRange(Long deptId, LocalDate startDate, LocalDate endDate) {
        return scheduleMapper.findByDeptIdAndDateRange(deptId, startDate, endDate);
    }

    @Override
    public ScheduleConflictResult detectConflicts(Schedule schedule) {
        ScheduleConflictResult result = new ScheduleConflictResult();
        List<ScheduleConflictResult.ConflictInfo> conflicts = new ArrayList<>();

        List<Schedule> existingSchedules = scheduleMapper.findConflicts(
            schedule.getUserId(), 
            schedule.getScheduleDate(), 
            schedule.getScheduleId()
        );

        if (!existingSchedules.isEmpty()) {
            ScheduleConflictResult.ConflictInfo conflictInfo = new ScheduleConflictResult.ConflictInfo();
            conflictInfo.setUserId(schedule.getUserId());
            conflictInfo.setConflictDate(schedule.getScheduleDate());
            conflictInfo.setConflictType("OVERLAP");
            conflictInfo.setDescription("该日期已存在排班");
            
            List<ScheduleConflictResult.ScheduleInfo> scheduleInfos = existingSchedules.stream()
                .map(s -> {
                    ScheduleConflictResult.ScheduleInfo info = new ScheduleConflictResult.ScheduleInfo();
                    info.setScheduleId(s.getScheduleId());
                    Shift shift = shiftService.getById(s.getShiftId());
                    if (shift != null) {
                        info.setShiftName(shift.getName());
                        info.setStartTime(shift.getStartTime().toString());
                        info.setEndTime(shift.getEndTime().toString());
                    }
                    return info;
                }).collect(Collectors.toList());
            conflictInfo.setSchedules(scheduleInfos);
            conflicts.add(conflictInfo);
        }

        result.setHasConflict(!conflicts.isEmpty());
        result.setConflicts(conflicts);
        return result;
    }

    @Override
    public ScheduleConflictResult detectConflicts(BatchScheduleRequest request) {
        ScheduleConflictResult result = new ScheduleConflictResult();
        List<ScheduleConflictResult.ConflictInfo> conflicts = new ArrayList<>();

        for (Long userId : request.getUserIds()) {
            LocalDate currentDate = request.getStartDate();
            while (!currentDate.isAfter(request.getEndDate()) || currentDate.isEqual(request.getEndDate())) {
                if (request.getExcludeWeekdays() != null && 
                    request.getExcludeWeekdays().contains(currentDate.getDayOfWeek().getValue())) {
                    currentDate = currentDate.plusDays(1);
                    continue;
                }

                List<Schedule> existingSchedules = scheduleMapper.findConflicts(userId, currentDate, null);
                if (!existingSchedules.isEmpty()) {
                    ScheduleConflictResult.ConflictInfo conflictInfo = new ScheduleConflictResult.ConflictInfo();
                    conflictInfo.setUserId(userId);
                    User user = userMapper.selectById(userId);
                    if (user != null) {
                        conflictInfo.setUserName(user.getUsername());
                    }
                    conflictInfo.setConflictDate(currentDate);
                    conflictInfo.setConflictType("OVERLAP");
                    conflictInfo.setDescription("该日期已存在排班");
                    conflicts.add(conflictInfo);
                }
                currentDate = currentDate.plusDays(1);
            }
        }

        result.setHasConflict(!conflicts.isEmpty());
        result.setConflicts(conflicts);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSchedule(BatchScheduleRequest request) {
        if (!request.getOverwrite()) {
            ScheduleConflictResult conflictResult = detectConflicts(request);
            if (conflictResult.getHasConflict()) {
                throw new BadRequestException("排班存在冲突，请检查冲突信息或选择覆盖已有排班");
            }
        }

        List<Schedule> schedules = new ArrayList<>();
        for (Long userId : request.getUserIds()) {
            LocalDate currentDate = request.getStartDate();
            while (!currentDate.isAfter(request.getEndDate()) || currentDate.isEqual(request.getEndDate())) {
                if (request.getExcludeWeekdays() != null && 
                    request.getExcludeWeekdays().contains(currentDate.getDayOfWeek().getValue())) {
                    currentDate = currentDate.plusDays(1);
                    continue;
                }

                if (request.getOverwrite()) {
                    scheduleMapper.deleteByDateRange(currentDate, currentDate, userId);
                }

                Schedule schedule = new Schedule();
                schedule.setUserId(userId);
                schedule.setShiftId(request.getShiftId());
                schedule.setScheduleDate(currentDate);
                schedule.setType("manual");
                schedule.setStatus("0");
                schedules.add(schedule);
                currentDate = currentDate.plusDays(1);
            }
        }

        if (!schedules.isEmpty()) {
            scheduleMapper.batchInsert(schedules);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoSchedule(AutoScheduleRequest request) {
        RotationRule rule = rotationRuleMapper.selectById(request.getRotationRuleId());
        if (rule == null) {
            throw new BadRequestException("轮班规则不存在");
        }

        String pattern = rule.getPattern();
        List<Long> shiftIds = parsePattern(pattern);

        List<Schedule> schedules = new ArrayList<>();
        int cycleDays = rule.getCycleDays();

        for (Long userId : request.getUserIds()) {
            LocalDate currentDate = request.getStartDate();
            int dayIndex = 0;

            while (!currentDate.isAfter(request.getEndDate()) || currentDate.isEqual(request.getEndDate())) {
                Long shiftId = shiftIds.get(dayIndex % cycleDays);
                
                if (request.getOverwrite()) {
                    scheduleMapper.deleteByDateRange(currentDate, currentDate, userId);
                }

                Schedule schedule = new Schedule();
                schedule.setUserId(userId);
                schedule.setShiftId(shiftId);
                schedule.setScheduleDate(currentDate);
                schedule.setType("auto");
                schedule.setStatus("0");
                schedules.add(schedule);

                currentDate = currentDate.plusDays(1);
                dayIndex++;
            }
        }

        if (!schedules.isEmpty()) {
            scheduleMapper.batchInsert(schedules);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateMonthlySchedule(Integer year, Integer month, Long groupId) {
        ScheduleGroup group = null;
        if (groupId != null) {
            // 从分组信息获取
        }

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<ScheduleGroupMember> members = scheduleGroupMemberMapper.findByGroupId(groupId);
        if (members.isEmpty()) {
            throw new BadRequestException("该分组没有成员");
        }

        RotationRule rule = rotationRuleMapper.selectById(group.getRotationRuleId());
        if (rule == null) {
            throw new BadRequestException("轮班规则不存在");
        }

        AutoScheduleRequest request = new AutoScheduleRequest();
        request.setGroupId(groupId);
        request.setRotationRuleId(rule.getRuleId());
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setOverwrite(true);
        request.setUserIds(members.stream().map(ScheduleGroupMember::getUserId).collect(Collectors.toList()));

        autoSchedule(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByDateRange(LocalDate startDate, LocalDate endDate, Long userId) {
        scheduleMapper.deleteByDateRange(startDate, endDate, userId);
    }

    private List<Long> parsePattern(String pattern) {
        List<Long> shiftIds = new ArrayList<>();
        if (pattern == null || pattern.trim().isEmpty()) {
            return shiftIds;
        }
        String[] parts = pattern.split(",");
        for (String part : parts) {
            try {
                shiftIds.add(Long.parseLong(part.trim()));
            } catch (NumberFormatException e) {
                // 解析失败
            }
        }
        return shiftIds;
    }
}
