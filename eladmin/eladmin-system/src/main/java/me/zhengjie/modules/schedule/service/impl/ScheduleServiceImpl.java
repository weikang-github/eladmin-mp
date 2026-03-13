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

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.schedule.domain.Schedule;
import me.zhengjie.modules.schedule.domain.Shift;
import me.zhengjie.modules.schedule.domain.ShiftRotation;
import me.zhengjie.modules.schedule.domain.dto.AutoScheduleDTO;
import me.zhengjie.modules.schedule.domain.dto.BatchScheduleDTO;
import me.zhengjie.modules.schedule.domain.dto.ScheduleQueryCriteria;
import me.zhengjie.modules.schedule.mapper.ScheduleMapper;
import me.zhengjie.modules.schedule.mapper.ShiftMapper;
import me.zhengjie.modules.schedule.mapper.ShiftRotationMapper;
import me.zhengjie.modules.schedule.service.ScheduleService;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.mapper.UserMapper;
import me.zhengjie.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Zheng Jie
 * @date 2025-04-10
 */
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements ScheduleService {

    private final ScheduleMapper scheduleMapper;
    private final ShiftMapper shiftMapper;
    private final ShiftRotationMapper shiftRotationMapper;
    private final UserMapper userMapper;
    private final RedisUtils redisUtils;

    @Override
    public PageResult<Schedule> queryAll(ScheduleQueryCriteria criteria, Page<Object> page) {
        criteria.setOffset(page.offset());
        List<Schedule> schedules = scheduleMapper.findAll(criteria);
        Long total = scheduleMapper.countAll(criteria);
        return PageUtil.toPage(schedules, total);
    }

    @Override
    public List<Schedule> queryAll(ScheduleQueryCriteria criteria) {
        return scheduleMapper.findAll(criteria);
    }

    @Override
    public Schedule findById(Long id) {
        String key = CacheKey.SCHEDULE_ID + id;
        Schedule schedule = redisUtils.get(key, Schedule.class);
        if(schedule == null){
            schedule = scheduleMapper.selectById(id);
            if(schedule != null) {
                // 关联查询
                List<Schedule> list = scheduleMapper.findAll(new ScheduleQueryCriteria(){{
                    setId(id);
                }});
                if(!list.isEmpty()) {
                    schedule = list.get(0);
                }
                redisUtils.set(key, schedule, 1, TimeUnit.DAYS);
            }
        }
        return schedule;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Schedule resources) {
        // 检测冲突
        if(resources.getCheckConflict() != null && resources.getCheckConflict()) {
            List<Schedule> conflicts = checkConflict(resources.getUserId(), resources.getScheduleDate(), resources.getShiftId(), null);
            if(!conflicts.isEmpty()) {
                throw new BadRequestException("排班冲突：" + conflicts.get(0).getConflictMsg());
            }
        }
        
        Shift shift = shiftMapper.selectById(resources.getShiftId());
        if(shift == null) {
            throw new BadRequestException("班次不存在");
        }
        
        User user = userMapper.selectById(resources.getUserId());
        if(user == null) {
            throw new BadRequestException("用户不存在");
        }
        
        resources.setDeptId(user.getDeptId());
        resources.setStartTime(shift.getStartTime() != null ? 
            java.time.LocalDateTime.of(resources.getScheduleDate().toLocalDate(), shift.getStartTime().toLocalTime()) : null);
        resources.setEndTime(shift.getEndTime() != null ? 
            java.time.LocalDateTime.of(resources.getScheduleDate().toLocalDate(), shift.getEndTime().toLocalTime()) : null);
        resources.setStatus("ACTIVE");
        resources.setSourceType("MANUAL");
        resources.setConflictFlag(false);
        
        save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Schedule resources) {
        Schedule schedule = getById(resources.getId());
        if(schedule == null) {
            throw new BadRequestException("排班不存在");
        }
        
        // 检测冲突
        if(resources.getCheckConflict() != null && resources.getCheckConflict()) {
            List<Schedule> conflicts = checkConflict(resources.getUserId(), resources.getScheduleDate(), resources.getShiftId(), resources.getId());
            if(!conflicts.isEmpty()) {
                throw new BadRequestException("排班冲突：" + conflicts.get(0).getConflictMsg());
            }
        }
        
        if(resources.getShiftId() != null && !resources.getShiftId().equals(schedule.getShiftId())) {
            Shift shift = shiftMapper.selectById(resources.getShiftId());
            if(shift == null) {
                throw new BadRequestException("班次不存在");
            }
            resources.setStartTime(shift.getStartTime() != null ? 
                java.time.LocalDateTime.of(resources.getScheduleDate().toLocalDate(), shift.getStartTime().toLocalTime()) : null);
            resources.setEndTime(shift.getEndTime() != null ? 
                java.time.LocalDateTime.of(resources.getScheduleDate().toLocalDate(), shift.getEndTime().toLocalTime()) : null);
        }
        
        resources.setUpdateBy(SecurityUtils.getCurrentUsername());
        resources.setUpdateTime(DateTime.now().toTimestamp());
        saveOrUpdate(resources);
        
        // 清理缓存
        redisUtils.del(CacheKey.SCHEDULE_ID + resources.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            redisUtils.del(CacheKey.SCHEDULE_ID + id);
        }
        removeBatchByIds(ids);
    }

    @Override
    public void download(List<Schedule> schedules, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Schedule schedule : schedules) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("员工姓名", schedule.getUser() != null ? schedule.getUser().getNickName() : "-");
            map.put("部门名称", schedule.getDept() != null ? schedule.getDept().getName() : "-");
            map.put("班次名称", schedule.getShift() != null ? schedule.getShift().getName() : "-");
            map.put("排班日期", schedule.getScheduleDate());
            map.put("开始时间", schedule.getStartTime());
            map.put("结束时间", schedule.getEndTime());
            map.put("状态", getStatusText(schedule.getStatus()));
            map.put("来源", getSourceTypeText(schedule.getSourceType()));
            map.put("冲突标记", schedule.getConflictFlag() ? "是" : "否");
            map.put("备注", schedule.getRemark());
            map.put("创建日期", schedule.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchSchedule(BatchScheduleDTO batchScheduleDTO) {
        Map<String, Object> result = new HashMap<>();
        List<Schedule> createdSchedules = new ArrayList<>();
        List<Map<String, Object>> conflicts = new ArrayList<>();
        
        Set<Long> userIds = batchScheduleDTO.getUserIds();
        Long shiftId = batchScheduleDTO.getShiftId();
        List<Date> scheduleDates = batchScheduleDTO.getScheduleDates();
        
        if(CollectionUtil.isEmpty(userIds)) {
            throw new BadRequestException("请选择员工");
        }
        if(shiftId == null) {
            throw new BadRequestException("请选择班次");
        }
        if(CollectionUtil.isEmpty(scheduleDates)) {
            throw new BadRequestException("请选择排班日期");
        }
        
        Shift shift = shiftMapper.selectById(shiftId);
        if(shift == null) {
            throw new BadRequestException("班次不存在");
        }
        
        List<User> users = userMapper.selectBatchIds(userIds);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, u -> u));
        
        for (Long userId : userIds) {
            User user = userMap.get(userId);
            if(user == null) continue;
            
            for (Date scheduleDate : scheduleDates) {
                // 检测冲突
                if(batchScheduleDTO.getCheckConflict()) {
                    List<Schedule> conflictList = checkConflict(userId, scheduleDate, shiftId, null);
                    if(!conflictList.isEmpty()) {
                        Map<String, Object> conflict = new HashMap<>();
                        conflict.put("userId", userId);
                        conflict.put("userName", user.getNickName());
                        conflict.put("scheduleDate", scheduleDate);
                        conflict.put("conflictMsg", conflictList.get(0).getConflictMsg());
                        conflicts.add(conflict);
                        continue;
                    }
                }
                
                Schedule schedule = new Schedule();
                schedule.setUserId(userId);
                schedule.setDeptId(user.getDeptId());
                schedule.setShiftId(shiftId);
                schedule.setScheduleDate(scheduleDate);
                schedule.setStartTime(shift.getStartTime() != null ? 
                    java.time.LocalDateTime.of(scheduleDate.toLocalDate(), shift.getStartTime().toLocalTime()) : null);
                schedule.setEndTime(shift.getEndTime() != null ? 
                    java.time.LocalDateTime.of(scheduleDate.toLocalDate(), shift.getEndTime().toLocalTime()) : null);
                schedule.setStatus("ACTIVE");
                schedule.setSourceType("BATCH");
                schedule.setConflictFlag(false);
                schedule.setRemark(batchScheduleDTO.getRemark());
                schedule.setCreateBy(SecurityUtils.getCurrentUsername());
                schedule.setCreateTime(DateTime.now().toTimestamp());
                
                save(schedule);
                createdSchedules.add(schedule);
            }
        }
        
        result.put("createdCount", createdSchedules.size());
        result.put("conflictCount", conflicts.size());
        result.put("conflicts", conflicts);
        result.put("schedules", createdSchedules);
        
        return result;
    }

    @Override
    public List<Schedule> checkConflict(Long userId, Date scheduleDate, Long shiftId, Long excludeScheduleId) {
        List<Schedule> result = new ArrayList<>();
        
        // 1. 检查同一天是否有多个排班
        List<Schedule> existingSchedules = scheduleMapper.findConflictSchedules(userId, scheduleDate, excludeScheduleId);
        
        if(!existingSchedules.isEmpty()) {
            Shift newShift = shiftId != null ? shiftMapper.selectById(shiftId) : null;
            
            for (Schedule existing : existingSchedules) {
                Shift existingShift = existing.getShift();
                if(existingShift == null) {
                    existingShift = shiftMapper.selectById(existing.getShiftId());
                }
                
                if(newShift != null && existingShift != null) {
                    // 检查时间重叠
                    if(isTimeOverlap(newShift.getStartTime(), newShift.getEndTime(), 
                                    existingShift.getStartTime(), existingShift.getEndTime(),
                                    newShift.getIsCrossDay(), existingShift.getIsCrossDay())) {
                        existing.setConflictFlag(true);
                        existing.setConflictMsg(String.format("与班次[%s]时间重叠", existingShift.getName()));
                        result.add(existing);
                    }
                } else {
                    existing.setConflictFlag(true);
                    existing.setConflictMsg(String.format("日期[%s]已存在排班", scheduleDate));
                    result.add(existing);
                }
            }
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> autoGenerateMonthlySchedule(AutoScheduleDTO autoScheduleDTO) {
        Map<String, Object> result = new HashMap<>();
        
        ShiftRotation rotation = shiftRotationMapper.selectById(autoScheduleDTO.getRotationId());
        if(rotation == null || !rotation.getEnabled()) {
            throw new BadRequestException("轮班规则不存在或未启用");
        }
        
        // 计算生成日期范围
        LocalDate startLocal, endLocal;
        if(autoScheduleDTO.getStartDate() != null && autoScheduleDTO.getEndDate() != null) {
            startLocal = autoScheduleDTO.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            endLocal = autoScheduleDTO.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else {
            int year = autoScheduleDTO.getYear();
            int month = autoScheduleDTO.getMonth();
            startLocal = LocalDate.of(year, month, 1);
            endLocal = startLocal.withDayOfMonth(startLocal.lengthOfMonth());
        }
        
        // 获取需要排班的用户
        Set<Long> userIds = autoScheduleDTO.getUserIds();
        if(CollectionUtil.isEmpty(userIds)) {
            // 如果没有指定用户，根据部门查询
            if(CollectionUtil.isNotEmpty(autoScheduleDTO.getDeptIds())) {
                userIds = userMapper.findUserIdsByDeptIds(autoScheduleDTO.getDeptIds());
            }
        }
        
        if(CollectionUtil.isEmpty(userIds)) {
            throw new BadRequestException("没有找到需要排班的用户");
        }
        
        // 解析轮班模式
        cn.hutool.json.JSONArray shiftPatterns = cn.hutool.json.JSONUtil.parseArray(rotation.getRotationPattern());
        if(shiftPatterns.isEmpty()) {
            throw new BadRequestException("轮班规则未配置班次模式");
        }
        
        List<Schedule> schedules = new ArrayList<>();
        List<Map<String, Object>> conflicts = new ArrayList<>();
        List<User> users = userMapper.selectBatchIds(userIds);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, u -> u));
        
        LocalDate rotationStartLocal = rotation.getStartDate() != null ?
            rotation.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : startLocal;
        
        // 遍历每一天
        for (LocalDate currentLocal = startLocal; !currentLocal.isAfter(endLocal); currentLocal = currentLocal.plusDays(1)) {
            Date currentDate = Date.valueOf(currentLocal);
            
            // 计算当前日期在周期中的位置
            long daysFromRotationStart = ChronoUnit.DAYS.between(rotationStartLocal, currentLocal);
            int cyclePosition = (int) (daysFromRotationStart % rotation.getCycleDays());
            
            // 确保cyclePosition非负
            cyclePosition = (cyclePosition + rotation.getCycleDays()) % rotation.getCycleDays();
            
            for (Long userId : userIds) {
                User user = userMap.get(userId);
                if(user == null) continue;
                
                // 计算该用户的班次偏移
                int userOffset = Math.abs(userId.hashCode() % shiftPatterns.size());
                int patternIndex = (cyclePosition + userOffset) % shiftPatterns.size();
                
                cn.hutool.json.JSONObject pattern = shiftPatterns.getJSONObject(patternIndex);
                Long patternShiftId = pattern.getLong("shiftId");
                
                // 休息日跳过
                if(patternShiftId == null || patternShiftId == 0) {
                    continue;
                }
                
                Shift shift = shiftMapper.selectById(patternShiftId);
                if(shift == null) continue;
                
                // 检查冲突
                boolean hasConflict = false;
                if(autoScheduleDTO.getCheckConflict()) {
                    List<Schedule> conflictList = checkConflict(userId, currentDate, patternShiftId, null);
                    if(!conflictList.isEmpty()) {
                        if(!autoScheduleDTO.getOverwriteExisting()) {
                            Map<String, Object> conflict = new HashMap<>();
                            conflict.put("userId", userId);
                            conflict.put("userName", user.getNickName());
                            conflict.put("scheduleDate", currentDate);
                            conflict.put("conflictMsg", conflictList.get(0).getConflictMsg());
                            conflicts.add(conflict);
                            continue;
                        } else {
                            // 删除已存在的排班
                            scheduleMapper.deleteByUserIdAndDateRange(userId, currentDate, currentDate);
                        }
                    }
                }
                
                Schedule schedule = new Schedule();
                schedule.setUserId(userId);
                schedule.setDeptId(user.getDeptId());
                schedule.setShiftId(patternShiftId);
                schedule.setScheduleDate(currentDate);
                schedule.setStartTime(shift.getStartTime() != null ? 
                    java.time.LocalDateTime.of(currentLocal, shift.getStartTime().toLocalTime()) : null);
                schedule.setEndTime(shift.getEndTime() != null ? 
                    java.time.LocalDateTime.of(currentLocal, shift.getEndTime().toLocalTime()) : null);
                schedule.setStatus("ACTIVE");
                schedule.setSourceType("AUTO");
                schedule.setConflictFlag(false);
                schedule.setRemark("自动排班 - " + rotation.getName());
                schedule.setCreateBy(SecurityUtils.getCurrentUsername());
                schedule.setCreateTime(DateTime.now().toTimestamp());
                
                schedules.add(schedule);
            }
        }
        
        // 批量插入
        if(!schedules.isEmpty()) {
            scheduleMapper.batchInsert(schedules);
        }
        
        result.put("generatedCount", schedules.size());
        result.put("conflictCount", conflicts.size());
        result.put("conflicts", conflicts);
        result.put("schedules", schedules);
        
        return result;
    }

    @Override
    public List<Schedule> findByUserIdAndDateRange(Long userId, Date startDate, Date endDate) {
        return scheduleMapper.findByUserIdAndDateRange(userId, startDate, endDate);
    }

    @Override
    public Map<String, Object> getCalendarView(ScheduleQueryCriteria criteria) {
        Map<String, Object> result = new HashMap<>();
        List<Schedule> schedules = scheduleMapper.findAll(criteria);
        
        // 按日期分组
        Map<Date, List<Schedule>> dateGroup = schedules.stream()
            .collect(Collectors.groupingBy(Schedule::getScheduleDate));
        
        // 按用户分组
        Map<Long, List<Schedule>> userGroup = schedules.stream()
            .collect(Collectors.groupingBy(Schedule::getUserId));
        
        result.put("schedules", schedules);
        result.put("dateGroup", dateGroup);
        result.put("userGroup", userGroup);
        result.put("total", schedules.size());
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int detectAndMarkConflicts(Date startDate, Date endDate) {
        int conflictCount = 0;
        
        // 查询日期范围内的所有排班
        ScheduleQueryCriteria criteria = new ScheduleQueryCriteria();
        criteria.setStartDate(startDate);
        criteria.setEndDate(endDate);
        List<Schedule> schedules = scheduleMapper.findAll(criteria);
        
        // 按用户和日期分组
        Map<Long, Map<Date, List<Schedule>>> userDateGroup = schedules.stream()
            .collect(Collectors.groupingBy(Schedule::getUserId, 
                     Collectors.groupingBy(Schedule::getScheduleDate)));
        
        // 检测每个用户每天的排班冲突
        for (Map.Entry<Long, Map<Date, List<Schedule>>> userEntry : userDateGroup.entrySet()) {
            Long userId = userEntry.getKey();
            for (Map.Entry<Date, List<Schedule>> dateEntry : userEntry.getValue().entrySet()) {
                Date scheduleDate = dateEntry.getKey();
                List<Schedule> daySchedules = dateEntry.getValue();
                
                if(daySchedules.size() > 1) {
                    // 检查时间重叠
                    for (int i = 0; i < daySchedules.size(); i++) {
                        for (int j = i + 1; j < daySchedules.size(); j++) {
                            Schedule s1 = daySchedules.get(i);
                            Schedule s2 = daySchedules.get(j);
                            
                            Shift shift1 = s1.getShift();
                            Shift shift2 = s2.getShift();
                            
                            if(shift1 == null) shift1 = shiftMapper.selectById(s1.getShiftId());
                            if(shift2 == null) shift2 = shiftMapper.selectById(s2.getShiftId());
                            
                            if(shift1 != null && shift2 != null) {
                                if(isTimeOverlap(shift1.getStartTime(), shift1.getEndTime(),
                                                shift2.getStartTime(), shift2.getEndTime(),
                                                shift1.getIsCrossDay(), shift2.getIsCrossDay())) {
                                    // 标记冲突
                                    markConflict(s1, String.format("与班次[%s]时间重叠", shift2.getName()));
                                    markConflict(s2, String.format("与班次[%s]时间重叠", shift1.getName()));
                                    conflictCount += 2;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return conflictCount;
    }

    @Override
    public Map<String, Object> getStatistics(ScheduleQueryCriteria criteria) {
        Map<String, Object> result = new HashMap<>();
        List<Schedule> schedules = scheduleMapper.findAll(criteria);
        
        // 总排数
        result.put("totalCount", schedules.size());
        
        // 按班次统计
        Map<String, Long> shiftStats = schedules.stream()
            .filter(s -> s.getShift() != null)
            .collect(Collectors.groupingBy(s -> s.getShift().getName(), Collectors.counting()));
        result.put("shiftStats", shiftStats);
        
        // 按部门统计
        Map<String, Long> deptStats = schedules.stream()
            .filter(s -> s.getDept() != null)
            .collect(Collectors.groupingBy(s -> s.getDept().getName(), Collectors.counting()));
        result.put("deptStats", deptStats);
        
        // 按状态统计
        Map<String, Long> statusStats = schedules.stream()
            .collect(Collectors.groupingBy(Schedule::getStatus, Collectors.counting()));
        result.put("statusStats", statusStats);
        
        // 冲突统计
        long conflictCount = schedules.stream()
            .filter(s -> s.getConflictFlag() != null && s.getConflictFlag())
            .count();
        result.put("conflictCount", conflictCount);
        
        // 来源统计
        Map<String, Long> sourceStats = schedules.stream()
            .collect(Collectors.groupingBy(Schedule::getSourceType, Collectors.counting()));
        result.put("sourceStats", sourceStats);
        
        return result;
    }

    private boolean isTimeOverlap(java.sql.Time start1, java.sql.Time end1, 
                                  java.sql.Time start2, java.sql.Time end2,
                                  Boolean crossDay1, Boolean crossDay2) {
        if(start1 == null || end1 == null || start2 == null || end2 == null) {
            return true;
        }
        
        boolean cd1 = crossDay1 != null && crossDay1;
        boolean cd2 = crossDay2 != null && crossDay2;
        
        // 转换为分钟数便于计算
        int s1 = start1.toLocalTime().toSecondOfDay() / 60;
        int e1 = end1.toLocalTime().toSecondOfDay() / 60;
        int s2 = start2.toLocalTime().toSecondOfDay() / 60;
        int e2 = end2.toLocalTime().toSecondOfDay() / 60;
        
        if(cd1) e1 += 24 * 60;
        if(cd2) e2 += 24 * 60;
        
        // 检查时间区间是否重叠
        return (s1 < e2 && s2 < e1) || 
               (s1 + 24 * 60 < e2 && s2 < e1) ||
               (s1 < e2 + 24 * 60 && s2 + 24 * 60 < e1);
    }

    @Transactional(rollbackFor = Exception.class)
    protected void markConflict(Schedule schedule, String conflictMsg) {
        schedule.setConflictFlag(true);
        schedule.setConflictMsg(conflictMsg);
        schedule.setUpdateTime(DateTime.now().toTimestamp());
        schedule.setUpdateBy(SecurityUtils.getCurrentUsername());
        scheduleMapper.updateById(schedule);
        redisUtils.del(CacheKey.SCHEDULE_ID + schedule.getId());
    }

    private String getStatusText(String status) {
        if(status == null) return "-";
        switch (status) {
            case "ACTIVE": return "有效";
            case "CANCELLED": return "已取消";
            case "COMPLETED": return "已完成";
            default: return status;
        }
    }

    private String getSourceTypeText(String sourceType) {
        if(sourceType == null) return "-";
        switch (sourceType) {
            case "MANUAL": return "手动";
            case "BATCH": return "批量";
            case "AUTO": return "自动";
            default: return sourceType;
        }
    }
}
