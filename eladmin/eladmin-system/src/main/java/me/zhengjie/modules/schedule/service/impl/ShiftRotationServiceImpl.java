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

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.schedule.domain.Schedule;
import me.zhengjie.modules.schedule.domain.Shift;
import me.zhengjie.modules.schedule.domain.ShiftRotation;
import me.zhengjie.modules.schedule.domain.dto.RotationGenerateDTO;
import me.zhengjie.modules.schedule.domain.dto.ShiftRotationQueryCriteria;
import me.zhengjie.modules.schedule.mapper.ScheduleMapper;
import me.zhengjie.modules.schedule.mapper.ShiftMapper;
import me.zhengjie.modules.schedule.mapper.ShiftRotationMapper;
import me.zhengjie.modules.schedule.service.ShiftRotationService;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.mapper.UserMapper;
import me.zhengjie.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Zheng Jie
 * @date 2025-04-10
 */
@Service
@RequiredArgsConstructor
public class ShiftRotationServiceImpl extends ServiceImpl<ShiftRotationMapper, ShiftRotation> implements ShiftRotationService {

    private final ShiftRotationMapper shiftRotationMapper;
    private final ShiftMapper shiftMapper;
    private final ScheduleMapper scheduleMapper;
    private final UserMapper userMapper;
    private final RedisUtils redisUtils;

    @Override
    public PageResult<ShiftRotation> queryAll(ShiftRotationQueryCriteria criteria, Page<Object> page) {
        return PageUtil.toPage(shiftRotationMapper.findAll(criteria, page));
    }

    @Override
    public List<ShiftRotation> queryAll(ShiftRotationQueryCriteria criteria) {
        return shiftRotationMapper.findAll(criteria);
    }

    @Override
    public ShiftRotation findById(Long id) {
        String key = CacheKey.SHIFT_ROTATION_ID + id;
        ShiftRotation rotation = redisUtils.get(key, ShiftRotation.class);
        if(rotation == null){
            rotation = shiftRotationMapper.selectById(id);
            redisUtils.set(key, rotation, 1, TimeUnit.DAYS);
        }
        return rotation;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ShiftRotation resources) {
        validateRotation(resources);
        save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ShiftRotation resources) {
        ShiftRotation rotation = getById(resources.getId());
        validateRotation(resources);
        resources.setId(rotation.getId());
        saveOrUpdate(resources);
        redisUtils.del(CacheKey.SHIFT_ROTATION_ID + resources.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            redisUtils.del(CacheKey.SHIFT_ROTATION_ID + id);
        }
        removeBatchByIds(ids);
    }

    @Override
    public void download(List<ShiftRotation> rotations, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ShiftRotation rotation : rotations) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("规则名称", rotation.getName());
            map.put("规则编码", rotation.getCode());
            map.put("周期天数", rotation.getCycleDays());
            map.put("状态", rotation.getEnabled() ? "启用" : "停用");
            map.put("描述", rotation.getRemark());
            map.put("创建日期", rotation.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> generateSchedules(RotationGenerateDTO generateDTO) {
        ShiftRotation rotation = findById(generateDTO.getRotationId());
        if(rotation == null || !rotation.getEnabled()){
            throw new BadRequestException("轮班规则不存在或未启用");
        }

        List<Long> userIds = generateDTO.getUserIds();
        if(CollectionUtil.isEmpty(userIds)){
            throw new BadRequestException("请选择要排班的用户");
        }

        // 解析轮班模式
        JSONArray shiftPatterns = JSONUtil.parseArray(rotation.getRotationPattern());
        if(shiftPatterns.isEmpty()){
            throw new BadRequestException("轮班规则未配置班次模式");
        }

        List<Schedule> schedules = new ArrayList<>();
        List<Long> scheduleIds = new ArrayList<>();
        Date startDate = generateDTO.getStartDate();
        Date endDate = generateDTO.getEndDate();

        // 获取所有用户信息
        List<User> users = userMapper.selectBatchIds(userIds);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, u -> u));

        // 遍历每一天
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);

        while (!cal.getTime().after(endDate)) {
            Date currentDate = DateUtil.beginOfDay(cal.getTime());

            // 计算当前日期在周期中的位置（使用生成参数的开始日期作为基准）
            LocalDate rotationStartLocal = rotation.getStartDate() != null ?
                    rotation.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() :
                    startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate generateStartLocal = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate currentLocal = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // 计算从轮班规则开始到生成开始日期的天数
            long daysFromRotationStart = ChronoUnit.DAYS.between(rotationStartLocal, generateStartLocal);
            long daysFromGenerateStart = ChronoUnit.DAYS.between(generateStartLocal, currentLocal);
            int cyclePosition = (int) ((daysFromRotationStart + daysFromGenerateStart) % rotation.getCycleDays());

            // 对每个用户生成排班
            for (Long userId : userIds) {
                User user = userMap.get(userId);
                if(user == null) continue;

                // 计算该用户的班次偏移
                int userOffset = getUserOffset(userId, rotation, shiftPatterns);
                int patternIndex = (cyclePosition + userOffset) % shiftPatterns.size();

                JSONObject pattern = shiftPatterns.getJSONObject(patternIndex);
                Long shiftId = pattern.getLong("shiftId");

                // 休息日跳过
                if(shiftId == null || shiftId == 0) {
                    continue;
                }

                // 检查冲突
                if(generateDTO.getCheckConflict()) {
                    List<Schedule> conflicts = scheduleMapper.findConflictSchedules(userId, currentDate, null);
                    if(!conflicts.isEmpty()) {
                        if(!generateDTO.getOverwriteExisting()) {
                            continue;
                        }
                        // 删除已存在的排班
                        for (Schedule conflict : conflicts) {
                            scheduleMapper.deleteById(conflict.getId());
                        }
                    }
                }

                Shift shift = shiftMapper.selectById(shiftId);
                if(shift == null) continue;

                Schedule schedule = new Schedule();
                schedule.setUserId(userId);
                schedule.setDeptId(user.getDeptId());
                schedule.setShiftId(shiftId);
                schedule.setScheduleDate(currentDate);
                schedule.setStartTime(shift.getStartTime());
                schedule.setEndTime(shift.getEndTime());
                schedule.setStatus("ACTIVE");
                schedule.setSourceType("AUTO");
                schedule.setConflictFlag(false);
                schedule.setRemark("自动排班 - " + rotation.getName());
                schedule.setCreateBy(SecurityUtils.getCurrentUsername());
                schedule.setCreateTime(DateTime.now().toTimestamp());

                schedules.add(schedule);
            }

            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        // 批量插入
        if(!schedules.isEmpty()) {
            scheduleMapper.batchInsert(schedules);
            scheduleIds = schedules.stream().map(Schedule::getId).collect(Collectors.toList());
        }

        return scheduleIds;
    }

    @Override
    public Map<String, Object> validateRotation(ShiftRotation resources) {
        Map<String, Object> result = new HashMap<>();
        try {
            if(!JSONUtil.isJsonArray(resources.getRotationPattern())) {
                throw new BadRequestException("轮班模式必须是JSON数组格式");
            }

            JSONArray patterns = JSONUtil.parseArray(resources.getRotationPattern());
            for (Object obj : patterns) {
                JSONObject pattern = (JSONObject) obj;
                if(!pattern.containsKey("shiftId")) {
                    throw new BadRequestException("每个轮班模式必须包含shiftId字段");
                }
            }

            if(resources.getCycleDays() == null || resources.getCycleDays() <= 0) {
                throw new BadRequestException("周期天数必须大于0");
            }

            if(patterns.size() != resources.getCycleDays()) {
                throw new BadRequestException("轮班模式数量必须等于周期天数");
            }

            result.put("success", true);
            result.put("message", "验证通过");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> generateSchedule(Long id, String startDateStr, String endDateStr) {
        Map<String, Object> result = new HashMap<>();
        try {
            ShiftRotation rotation = findById(id);
            if(rotation == null || !rotation.getEnabled()) {
                throw new BadRequestException("轮班规则不存在或未启用");
            }

            Date startDate = DateUtil.parse(startDateStr, "yyyy-MM-dd");
            Date endDate = DateUtil.parse(endDateStr, "yyyy-MM-dd");

            if(startDate.after(endDate)) {
                throw new BadRequestException("开始日期不能大于结束日期");
            }

            // 获取轮班规则关联的用户
            List<Long> userIds = getRotationUserIds(rotation);
            if(CollectionUtil.isEmpty(userIds)) {
                throw new BadRequestException("轮班规则未关联用户");
            }

            RotationGenerateDTO generateDTO = new RotationGenerateDTO();
            generateDTO.setRotationId(id);
            generateDTO.setUserIds(userIds);
            generateDTO.setStartDate(startDate);
            generateDTO.setEndDate(endDate);
            generateDTO.setCheckConflict(true);
            generateDTO.setOverwriteExisting(false);

            List<Long> scheduleIds = generateSchedules(generateDTO);

            result.put("success", true);
            result.put("scheduleCount", scheduleIds.size());
            result.put("scheduleIds", scheduleIds);
            result.put("message", "成功生成 " + scheduleIds.size() + " 条排班记录");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @Override
    public List<ShiftRotation> findByEnabled(Boolean enabled) {
        return shiftRotationMapper.findByEnabled(enabled);
    }

    private List<Long> getRotationUserIds(ShiftRotation rotation) {
        // 从轮班规则中获取用户列表
        // 这里假设轮班规则中有userIds字段，或者从部门中获取用户
        if(rotation.getUserIds() != null && !rotation.getUserIds().isEmpty()) {
            return Arrays.asList(rotation.getUserIds().split(","))
                    .stream().map(Long::valueOf).collect(Collectors.toList());
        }
        if(rotation.getDeptIds() != null && !rotation.getDeptIds().isEmpty()) {
            Set<Long> deptIds = Arrays.asList(rotation.getDeptIds().split(","))
                    .stream().map(Long::valueOf).collect(Collectors.toSet());
            return userMapper.findUserIdsByDeptIds(deptIds);
        }
        return new ArrayList<>();
    }

    private int getUserOffset(Long userId, ShiftRotation rotation, JSONArray shiftPatterns) {
        // 简单实现：根据用户ID哈希取模
        return Math.abs(userId.hashCode() % shiftPatterns.size());
    }
}
