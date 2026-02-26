package me.zhengjie.modules.schedule.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.schedule.domain.Schedule;
import me.zhengjie.modules.schedule.domain.ShiftRule;
import me.zhengjie.modules.schedule.domain.UserRule;
import me.zhengjie.modules.schedule.domain.dto.BatchScheduleRequest;
import me.zhengjie.modules.schedule.domain.dto.ScheduleQueryCriteria;
import me.zhengjie.modules.schedule.mapper.ScheduleMapper;
import me.zhengjie.modules.schedule.mapper.UserRuleMapper;
import me.zhengjie.modules.schedule.service.ScheduleService;
import me.zhengjie.modules.schedule.service.ShiftRuleService;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements ScheduleService {

    private final ShiftRuleService shiftRuleService;
    private final UserRuleMapper userRuleMapper;
    private final ScheduleMapper scheduleMapper;

    @Override
    public List<Schedule> queryAll(ScheduleQueryCriteria criteria) {
        return scheduleMapper.findAll(criteria);
    }

    @Override
    public PageResult<Schedule> queryAll(ScheduleQueryCriteria criteria, Page<Object> page) {
        return PageUtil.toPage(scheduleMapper.findAll(criteria, page));
    }

    @Override
    public Schedule findById(Long id) {
        return getById(id);
    }

    @Override
    public void create(Schedule schedule) {
        schedule.setCreateBy(SecurityUtils.getCurrentUsername());
        schedule.setCreateTime(new Timestamp(System.currentTimeMillis()));
        schedule.setUpdateBy(SecurityUtils.getCurrentUsername());
        schedule.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        save(schedule);
    }

    @Override
    public void update(Schedule schedule) {
        schedule.setUpdateBy(SecurityUtils.getCurrentUsername());
        schedule.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        updateById(schedule);
    }

    @Override
    public void delete(Long id) {
        removeById(id);
    }

    @Override
    public void deleteAll(Long[] ids) {
        removeBatchByIds(List.of(ids));
    }

    @Override
    public List<Schedule> findByUserIdAndDateRange(Long userId, Date startDate, Date endDate) {
        return scheduleMapper.findByUserIdAndDateRange(userId, startDate, endDate);
    }

    @Override
    public List<Schedule> findByDeptIdAndDateRange(Long deptId, Date startDate, Date endDate) {
        return scheduleMapper.findByDeptIdAndDateRange(deptId, startDate, endDate);
    }

    @Override
    public void batchSchedule(BatchScheduleRequest request) {
        List<Schedule> schedules = new ArrayList<>();
        String username = SecurityUtils.getCurrentUsername();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        LocalDate start = request.getStartDate().toLocalDate();
        LocalDate end = request.getEndDate().toLocalDate();

        for (Long userId : request.getUserIds()) {
            LocalDate current = start;
            while (!current.isAfter(end)) {
                Date scheduleDate = Date.valueOf(current);
                
                if (request.getSkipExisting()) {
                    Schedule existing = scheduleMapper.findByUserIdAndDate(userId, scheduleDate);
                    if (existing != null) {
                        current = current.plusDays(1);
                        continue;
                    }
                }

                Schedule schedule = new Schedule();
                schedule.setUserId(userId);
                schedule.setDeptId(request.getDeptId());
                schedule.setShiftId(request.getShiftId());
                schedule.setScheduleDate(scheduleDate);
                schedule.setScheduleType("MANUAL");
                schedule.setIsLocked(request.getIsLocked());
                schedule.setRemark(request.getRemark());
                schedule.setCreateBy(username);
                schedule.setCreateTime(now);
                schedule.setUpdateBy(username);
                schedule.setUpdateTime(now);
                schedules.add(schedule);

                current = current.plusDays(1);
            }
        }

        if (!schedules.isEmpty()) {
            scheduleMapper.batchInsert(schedules);
        }
    }

    @Override
    public void autoGenerateSchedule(Long deptId, Date startDate, Date endDate) {
        // 获取部门所有员工的轮班规则
        List<UserRule> userRules = userRuleMapper.findAll(null);
        Map<Long, UserRule> userRuleMap = userRules.stream()
                .filter(ur -> ur.getEnabled())
                .collect(Collectors.toMap(UserRule::getUserId, ur -> ur));

        List<Schedule> schedules = new ArrayList<>();
        String username = SecurityUtils.getCurrentUsername();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        LocalDate start = startDate.toLocalDate();
        LocalDate end = endDate.toLocalDate();

        for (Map.Entry<Long, UserRule> entry : userRuleMap.entrySet()) {
            Long userId = entry.getKey();
            UserRule userRule = entry.getValue();

            // 获取轮班规则详情
            ShiftRule shiftRule = shiftRuleService.findById(userRule.getRuleId());
            if (shiftRule == null || !shiftRule.getEnabled()) {
                continue;
            }

            LocalDate current = start;
            while (!current.isAfter(end)) {
                Date scheduleDate = Date.valueOf(current);
                
                // 计算轮班周期中的位置
                long daysSinceStart = start.toEpochDay() - start.toEpochDay();
                int dayIndex = (int) (daysSinceStart % shiftRule.getCycleDays()) + 1;

                // 根据轮班规则获取对应的班次
                Long shiftId = shiftRule.getItems().stream()
                        .filter(item -> item.getDayIndex() == dayIndex)
                        .findFirst()
                        .map(item -> item.getShiftId())
                        .orElse(null);

                if (shiftId != null) {
                    Schedule existing = scheduleMapper.findByUserIdAndDate(userId, scheduleDate);
                    if (existing == null || !existing.getIsLocked()) {
                        Schedule schedule = existing != null ? existing : new Schedule();
                        schedule.setUserId(userId);
                        schedule.setDeptId(deptId);
                        schedule.setShiftId(shiftId);
                        schedule.setScheduleDate(scheduleDate);
                        schedule.setScheduleType("AUTO");
                        schedule.setIsLocked(false);
                        schedule.setRemark("自动生成");
                        
                        if (existing == null) {
                            schedule.setCreateBy(username);
                            schedule.setCreateTime(now);
                        }
                        schedule.setUpdateBy(username);
                        schedule.setUpdateTime(now);
                        schedules.add(schedule);
                    }
                }

                current = current.plusDays(1);
            }
        }

        if (!schedules.isEmpty()) {
            List<Schedule> toInsert = new ArrayList<>();
            List<Schedule> toUpdate = new ArrayList<>();
            
            for (Schedule schedule : schedules) {
                if (schedule.getId() == null) {
                    toInsert.add(schedule);
                } else {
                    toUpdate.add(schedule);
                }
            }
            
            if (!toInsert.isEmpty()) {
                scheduleMapper.batchInsert(toInsert);
            }
            if (!toUpdate.isEmpty()) {
                scheduleMapper.batchUpdate(toUpdate);
            }
        }
    }

    @Override
    public Map<String, Object> checkConflicts(BatchScheduleRequest request) {
        Map<String, Object> result = new HashMap<>();
        List<String> conflicts = new ArrayList<>();
        boolean hasConflict = false;

        LocalDate start = request.getStartDate().toLocalDate();
        LocalDate end = request.getEndDate().toLocalDate();

        for (Long userId : request.getUserIds()) {
            LocalDate current = start;
            while (!current.isAfter(end)) {
                Date scheduleDate = Date.valueOf(current);
                Schedule existing = scheduleMapper.findByUserIdAndDate(userId, scheduleDate);
                if (existing != null && existing.getIsLocked()) {
                    conflicts.add("员工 " + userId + " 在 " + current + " 已有锁定的排班");
                    hasConflict = true;
                }
                current = current.plusDays(1);
            }
        }

        result.put("hasConflict", hasConflict);
        result.put("conflicts", conflicts);
        return result;
    }

    @Override
    public void generateMonthlySchedule(Long deptId, int year, int month) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        Date startDate = Date.valueOf(startOfMonth);
        Date endDate = Date.valueOf(endOfMonth);
        autoGenerateSchedule(deptId, startDate, endDate);
    }

    @Override
    public List<Schedule> findByDateRange(Date startDate, Date endDate) {
        ScheduleQueryCriteria criteria = new ScheduleQueryCriteria();
        List<Date> dateRange = Arrays.asList(startDate, endDate);
        return scheduleMapper.findAll(criteria);
    }
}
