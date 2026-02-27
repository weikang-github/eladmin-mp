package me.zhengjie.modules.schedule.service.impl;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.schedule.domain.Schedule;
import me.zhengjie.modules.schedule.domain.ScheduleRule;
import me.zhengjie.modules.schedule.domain.dto.ScheduleQueryCriteria;
import me.zhengjie.modules.schedule.mapper.ScheduleMapper;
import me.zhengjie.modules.schedule.service.ScheduleRuleService;
import me.zhengjie.modules.schedule.service.ScheduleService;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleMapper scheduleMapper;
    private final ScheduleRuleService scheduleRuleService;
    private final UserService userService;

    @Override
    public PageResult<Schedule> queryAll(ScheduleQueryCriteria criteria, Page<Object> page) {
        return PageUtil.toPage(scheduleMapper.findAll(criteria, page));
    }

    @Override
    public List<Schedule> queryAll(ScheduleQueryCriteria criteria) {
        return scheduleMapper.findAll(criteria, new Page<>(0, Integer.MAX_VALUE)).getRecords();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Schedule resources) {
        Map<String, Object> conflict = checkConflict(resources);
        if (conflict.get("hasConflict").equals(true)) {
            throw new BadRequestException("排班冲突：" + conflict.get("message"));
        }
        
        User user = userService.findById(resources.getUserId());
        if (user != null) {
            resources.setDeptId(user.getDeptId());
        }
        
        scheduleMapper.insert(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Schedule resources) {
        Schedule existing = scheduleMapper.selectById(resources.getId());
        if (existing == null) {
            throw new BadRequestException("排班记录不存在");
        }
        
        if (!existing.getUserId().equals(resources.getUserId()) || 
            !existing.getScheduleDate().equals(resources.getScheduleDate())) {
            Map<String, Object> conflict = checkConflict(resources);
            if (conflict.get("hasConflict").equals(true)) {
                throw new BadRequestException("排班冲突：" + conflict.get("message"));
            }
        }
        
        User user = userService.findById(resources.getUserId());
        if (user != null) {
            resources.setDeptId(user.getDeptId());
        }
        
        scheduleMapper.updateById(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        scheduleMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByDateRange(Date startDate, Date endDate) {
        scheduleMapper.deleteByDateRange(startDate, endDate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByUserIdsAndDateRange(Set<Long> userIds, Date startDate, Date endDate) {
        scheduleMapper.deleteByUserIdsAndDateRange(userIds, startDate, endDate);
    }

    @Override
    public void download(List<Schedule> queryAll, HttpServletResponse response) throws IOException {
        
    }

    @Override
    public List<Schedule> findByUserAndDateRange(Long userId, Date startDate, Date endDate) {
        return scheduleMapper.findByUserAndDateRange(userId, startDate, endDate);
    }

    @Override
    public List<Schedule> findByDeptAndDateRange(Long deptId, Date startDate, Date endDate) {
        return scheduleMapper.findByDeptAndDateRange(deptId, startDate, endDate);
    }

    @Override
    public Map<String, Object> checkConflict(Schedule schedule) {
        Map<String, Object> result = new HashMap<>();
        result.put("hasConflict", false);
        result.put("message", "");
        
        int count = scheduleMapper.countByUserAndDate(schedule.getUserId(), schedule.getScheduleDate());
        if (count > 0) {
            result.put("hasConflict", true);
            result.put("message", "该员工在指定日期已有排班");
            return result;
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateSchedule(Long ruleId, Set<Long> userIds, Date startDate, Date endDate) {
        ScheduleRule rule = scheduleRuleService.getById(ruleId);
        if (rule == null) {
            throw new BadRequestException("排班规则不存在");
        }
        
        List<Long> shiftIds = scheduleRuleService.getShiftIds(ruleId);
        if (CollUtil.isEmpty(shiftIds)) {
            throw new BadRequestException("排班规则未配置班次");
        }
        
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        
        int cycleDays = rule.getCycleDays();
        int totalDays = (int) ((endCal.getTimeInMillis() - startCal.getTimeInMillis()) / (1000 * 60 * 60 * 24)) + 1;
        
        for (Long userId : userIds) {
            User user = userService.findById(userId);
            if (user == null) {
                continue;
            }
            
            for (int day = 0; day < totalDays; day++) {
                Calendar currentCal = (Calendar) startCal.clone();
                currentCal.add(Calendar.DAY_OF_MONTH, day);
                Date currentDate = currentCal.getTime();
                
                int shiftIndex = day % cycleDays;
                if (shiftIndex >= shiftIds.size()) {
                    shiftIndex = shiftIndex % shiftIds.size();
                }
                
                Long shiftId = shiftIds.get(shiftIndex);
                
                Schedule schedule = new Schedule();
                schedule.setUserId(userId);
                schedule.setDeptId(user.getDeptId());
                schedule.setShiftId(shiftId);
                schedule.setScheduleDate(currentDate);
                schedule.setScheduleType("auto");
                schedule.setRuleId(ruleId);
                schedule.setStatus("normal");
                
                int count = scheduleMapper.countByUserAndDate(userId, currentDate);
                if (count == 0) {
                    scheduleMapper.insert(schedule);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateScheduleByDept(Long ruleId, Long deptId, Date startDate, Date endDate) {
        List<User> users = userService.queryAll(new me.zhengjie.modules.system.domain.dto.UserQueryCriteria());
        Set<Long> userIds = users.stream()
                .filter(u -> deptId.equals(u.getDeptId()))
                .map(User::getId)
                .collect(Collectors.toSet());
        
        if (CollUtil.isEmpty(userIds)) {
            throw new BadRequestException("该部门下没有员工");
        }
        
        generateSchedule(ruleId, userIds, startDate, endDate);
    }

    @Override
    public List<Schedule> getCalendarData(Date startDate, Date endDate) {
        return scheduleMapper.findByDateRange(startDate, endDate);
    }
}
