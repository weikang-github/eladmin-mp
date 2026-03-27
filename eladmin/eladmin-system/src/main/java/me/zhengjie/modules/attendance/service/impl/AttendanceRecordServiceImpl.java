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
package me.zhengjie.modules.attendance.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.attendance.domain.AttendanceGroup;
import me.zhengjie.modules.attendance.domain.AttendanceHoliday;
import me.zhengjie.modules.attendance.domain.AttendanceRecord;
import me.zhengjie.modules.attendance.domain.AttendanceStatistics;
import me.zhengjie.modules.attendance.domain.dto.AttendanceCheckInDto;
import me.zhengjie.modules.attendance.domain.dto.AttendanceRecordQueryCriteria;
import me.zhengjie.modules.attendance.domain.vo.AttendanceRecordExcelVo;
import me.zhengjie.modules.attendance.domain.vo.AttendanceStatisticsExcelVo;
import me.zhengjie.modules.attendance.mapper.AttendanceRecordMapper;
import me.zhengjie.modules.attendance.service.AttendanceGroupService;
import me.zhengjie.modules.attendance.service.AttendanceHolidayService;
import me.zhengjie.modules.attendance.service.AttendanceRecordService;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 考勤记录ServiceImpl
 * @author Zheng Jie
 * @date 2025-03-27
 */
@Service
@RequiredArgsConstructor
public class AttendanceRecordServiceImpl extends ServiceImpl<AttendanceRecordMapper, AttendanceRecord> implements AttendanceRecordService {

    private final AttendanceRecordMapper attendanceRecordMapper;
    private final AttendanceGroupService attendanceGroupService;
    private final AttendanceHolidayService attendanceHolidayService;

    @Override
    public PageResult<AttendanceRecord> queryAll(AttendanceRecordQueryCriteria criteria, Page<Object> page) {
        Page<AttendanceRecord> pageResult = attendanceRecordMapper.findAll(page, criteria);
        return PageUtil.toPage(pageResult);
    }

    @Override
    public List<AttendanceRecord> queryAll(AttendanceRecordQueryCriteria criteria) {
        return attendanceRecordMapper.findAll(criteria);
    }

    @Override
    public AttendanceRecord findById(Long id) {
        return attendanceRecordMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AttendanceRecord checkIn(AttendanceCheckInDto checkInDto) {
        AttendanceGroup group = attendanceGroupService.findByUserId(checkInDto.getUserId());
        if (group == null) {
            throw new BadRequestException("用户未分配考勤组");
        }

        Date today = DateUtil.date();
        Date attendanceDate = DateUtil.beginOfDay(today);

        if (!isWorkDay(group, attendanceDate)) {
            throw new BadRequestException("今日非工作日");
        }

        if (!validateCheckInLocation(group, checkInDto)) {
            throw new BadRequestException("打卡地点不在有效范围内");
        }

        if (!validateCheckInWifi(group, checkInDto)) {
            throw new BadRequestException("WiFi信息不匹配");
        }

        AttendanceRecord record = findOrCreateRecord(checkInDto.getUserId(), group.getId(), attendanceDate);

        Date now = DateUtil.date();
        if (checkInDto.getCheckType().equals("IN")) {
            if (record.getCheckInTime() != null) {
                throw new BadRequestException("今日已打卡上班");
            }
            record.setCheckInTime(now);
            record.setCheckInLatitude(checkInDto.getLatitude());
            record.setCheckInLongitude(checkInDto.getLongitude());
            record.setCheckInType(checkInDto.getCheckInMethod());
            record.setCheckInWifiName(checkInDto.getWifiName());
            record.setCheckInWifiMac(checkInDto.getWifiMac());
            record.setDevice(checkInDto.getDevice());
            record.setIpAddress(checkInDto.getIpAddress());
            record.setCheckInStatus(calculateCheckInStatus(group, now));
        } else if (checkInDto.getCheckType().equals("OUT")) {
            if (record.getCheckOutTime() != null) {
                throw new BadRequestException("今日已打卡下班");
            }
            record.setCheckOutTime(now);
            record.setCheckOutLatitude(checkInDto.getLatitude());
            record.setCheckOutLongitude(checkInDto.getLongitude());
            record.setCheckOutType(checkInDto.getCheckInMethod());
            record.setCheckOutWifiName(checkInDto.getWifiName());
            record.setCheckOutWifiMac(checkInDto.getWifiMac());
            record.setDevice(checkInDto.getDevice());
            record.setIpAddress(checkInDto.getIpAddress());
            record.setCheckOutStatus(calculateCheckOutStatus(group, now));
        }

        record.setAttendanceStatus(calculateAttendanceStatus(record));

        if (record.getId() == null) {
            attendanceRecordMapper.insert(record);
        } else {
            attendanceRecordMapper.updateById(record);
        }

        return record;
    }

    private AttendanceRecord findOrCreateRecord(Long userId, Long groupId, Date attendanceDate) {
        LambdaQueryWrapper<AttendanceRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AttendanceRecord::getUserId, userId)
                .eq(AttendanceRecord::getGroupId, groupId)
                .eq(AttendanceRecord::getAttendanceDate, attendanceDate);
        AttendanceRecord record = attendanceRecordMapper.selectOne(queryWrapper);
        if (record == null) {
            record = new AttendanceRecord();
            record.setUserId(userId);
            record.setGroupId(groupId);
            record.setAttendanceDate(attendanceDate);
        }
        return record;
    }

    private boolean isWorkDay(AttendanceGroup group, Date date) {
        if (attendanceHolidayService.isHoliday(date)) {
            return false;
        }
        int dayOfWeek = DateUtil.dayOfWeek(date) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        String workDays = group.getWorkDays();
        return workDays != null && workDays.contains(String.valueOf(dayOfWeek));
    }

    private boolean validateCheckInLocation(AttendanceGroup group, AttendanceCheckInDto checkInDto) {
        if ("WIFI".equals(checkInDto.getCheckInMethod())) {
            return true;
        }
        if (group.getCompanyLatitude() == null || group.getCompanyLongitude() == null) {
            return true;
        }
        if (checkInDto.getLatitude() == null || checkInDto.getLongitude() == null) {
            return false;
        }
        double distance = calculateDistance(
                group.getCompanyLatitude().doubleValue(),
                group.getCompanyLongitude().doubleValue(),
                checkInDto.getLatitude().doubleValue(),
                checkInDto.getLongitude().doubleValue()
        );
        return distance <= (group.getAllowDistance() != null ? group.getAllowDistance() : 100);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private boolean validateCheckInWifi(AttendanceGroup group, AttendanceCheckInDto checkInDto) {
        if ("GPS".equals(checkInDto.getCheckInMethod())) {
            return true;
        }
        if (group.getWifiName() == null || group.getWifiMac() == null) {
            return true;
        }
        return group.getWifiName().equals(checkInDto.getWifiName())
                && group.getWifiMac().equals(checkInDto.getWifiMac());
    }

    private String calculateCheckInStatus(AttendanceGroup group, Date checkInTime) {
        if (group.getWorkStartTime() == null) {
            return "NORMAL";
        }
        Date workStartTime = parseTimeString(group.getWorkStartTime());
        int lateMinutes = group.getLateMinutes() != null ? group.getLateMinutes() : 0;
        Date lateTime = DateUtil.offsetMinute(workStartTime, lateMinutes);
        if (checkInTime.after(lateTime)) {
            return "LATE";
        }
        return "NORMAL";
    }

    private String calculateCheckOutStatus(AttendanceGroup group, Date checkOutTime) {
        if (group.getWorkEndTime() == null) {
            return "NORMAL";
        }
        Date workEndTime = parseTimeString(group.getWorkEndTime());
        int earlyLeaveMinutes = group.getEarlyLeaveMinutes() != null ? group.getEarlyLeaveMinutes() : 0;
        Date earlyTime = DateUtil.offsetMinute(workEndTime, -earlyLeaveMinutes);
        if (checkOutTime.before(earlyTime)) {
            return "EARLY";
        }
        return "NORMAL";
    }

    private String calculateAttendanceStatus(AttendanceRecord record) {
        boolean hasCheckIn = record.getCheckInTime() != null;
        boolean hasCheckOut = record.getCheckOutTime() != null;

        if (!hasCheckIn && !hasCheckOut) {
            return "ABSENT";
        }
        if (!hasCheckIn) {
            return "MISS";
        }
        if (!hasCheckOut) {
            return "MISS";
        }
        if ("LATE".equals(record.getCheckInStatus()) || "EARLY".equals(record.getCheckOutStatus())) {
            return "LATE";
        }
        return "NORMAL";
    }

    private Date parseTimeString(String timeStr) {
        String[] parts = timeStr.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        return DateUtil.date()
                .setField(DateField.HOUR_OF_DAY, hour)
                .setField(DateField.MINUTE, minute)
                .setField(DateField.SECOND, 0)
                .setField(DateField.MILLISECOND, 0);
    }

    @Override
    public AttendanceRecord findByUserAndDate(Long userId, Date attendanceDate) {
        LambdaQueryWrapper<AttendanceRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AttendanceRecord::getUserId, userId)
                .eq(AttendanceRecord::getAttendanceDate, DateUtil.beginOfDay(attendanceDate));
        return attendanceRecordMapper.selectOne(queryWrapper);
    }

    @Override
    public List<AttendanceRecord> findByUserAndDateRange(Long userId, Date startDate, Date endDate) {
        LambdaQueryWrapper<AttendanceRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AttendanceRecord::getUserId, userId)
                .between(AttendanceRecord::getAttendanceDate, DateUtil.beginOfDay(startDate), DateUtil.endOfDay(endDate))
                .orderByAsc(AttendanceRecord::getAttendanceDate);
        return attendanceRecordMapper.selectList(queryWrapper);
    }

    @Override
    public List<AttendanceRecord> findByDeptAndDateRange(Long deptId, Date startDate, Date endDate) {
        return attendanceRecordMapper.findByDeptAndDateRange(deptId, DateUtil.beginOfDay(startDate), DateUtil.endOfDay(endDate));
    }

    @Override
    public AttendanceStatistics statisticsByDay(Long userId, Date date) {
        AttendanceRecord record = findByUserAndDate(userId, date);
        AttendanceStatistics statistics = new AttendanceStatistics();
        statistics.setUserId(userId);
        statistics.setStartDate(DateUtil.beginOfDay(date));
        statistics.setEndDate(DateUtil.endOfDay(date));
        statistics.setStatisticsType("DAY");

        if (record != null) {
            statistics.setWorkDays(1L);
            statistics.setLateDays("LATE".equals(record.getAttendanceStatus()) ? 1L : 0L);
            statistics.setEarlyLeaveDays("EARLY".equals(record.getCheckOutStatus()) ? 1L : 0L);
            statistics.setAbsentDays("ABSENT".equals(record.getAttendanceStatus()) ? 1L : 0L);
            statistics.setMissCardDays("MISS".equals(record.getAttendanceStatus()) ? 1L : 0L);
            statistics.setActualWorkDays(!"ABSENT".equals(record.getAttendanceStatus()) ? 1L : 0L);
        } else {
            statistics.setWorkDays(0L);
            statistics.setLateDays(0L);
            statistics.setEarlyLeaveDays(0L);
            statistics.setAbsentDays(1L);
            statistics.setMissCardDays(0L);
            statistics.setActualWorkDays(0L);
        }

        return statistics;
    }

    @Override
    public AttendanceStatistics statisticsByWeek(Long userId, Date startDate, Date endDate) {
        List<AttendanceRecord> records = findByUserAndDateRange(userId, startDate, endDate);
        return calculateStatistics(userId, startDate, endDate, "WEEK", records);
    }

    @Override
    public AttendanceStatistics statisticsByMonth(Long userId, Integer year, Integer month) {
        Date startDate = DateUtil.beginOfMonth(DateUtil.parse(year + "-" + month, "yyyy-MM"));
        Date endDate = DateUtil.endOfMonth(startDate);
        List<AttendanceRecord> records = findByUserAndDateRange(userId, startDate, endDate);
        return calculateStatistics(userId, startDate, endDate, "MONTH", records);
    }

    private AttendanceStatistics calculateStatistics(Long userId, Date startDate, Date endDate, String type, List<AttendanceRecord> records) {
        AttendanceStatistics statistics = new AttendanceStatistics();
        statistics.setUserId(userId);
        statistics.setStartDate(startDate);
        statistics.setEndDate(endDate);
        statistics.setStatisticsType(type);

        long workDays = 0;
        long lateDays = 0;
        long earlyLeaveDays = 0;
        long absentDays = 0;
        long missCardDays = 0;
        long actualWorkDays = 0;

        for (AttendanceRecord record : records) {
            workDays++;
            if ("LATE".equals(record.getAttendanceStatus())) {
                lateDays++;
            }
            if ("EARLY".equals(record.getCheckOutStatus())) {
                earlyLeaveDays++;
            }
            if ("ABSENT".equals(record.getAttendanceStatus())) {
                absentDays++;
            }
            if ("MISS".equals(record.getAttendanceStatus())) {
                missCardDays++;
            }
            if (!"ABSENT".equals(record.getAttendanceStatus())) {
                actualWorkDays++;
            }
        }

        statistics.setWorkDays(workDays);
        statistics.setLateDays(lateDays);
        statistics.setEarlyLeaveDays(earlyLeaveDays);
        statistics.setAbsentDays(absentDays);
        statistics.setMissCardDays(missCardDays);
        statistics.setActualWorkDays(actualWorkDays);

        return statistics;
    }

    @Override
    public void download(List<AttendanceRecord> list, HttpServletResponse response) throws IOException {
        List<AttendanceRecordExcelVo> excelVoList = list.stream().map(record -> {
            AttendanceRecordExcelVo vo = new AttendanceRecordExcelVo();
            BeanUtils.copyProperties(record, vo);
            vo.setAttendanceDate(DateUtil.format(record.getAttendanceDate(), "yyyy-MM-dd"));
            if (record.getCheckInTime() != null) {
                vo.setCheckInTime(DateUtil.format(record.getCheckInTime(), "yyyy-MM-dd HH:mm:ss"));
            }
            if (record.getCheckOutTime() != null) {
                vo.setCheckOutTime(DateUtil.format(record.getCheckOutTime(), "yyyy-MM-dd HH:mm:ss"));
            }
            return vo;
        }).collect(Collectors.toList());

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("考勤记录", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), AttendanceRecordExcelVo.class)
                .sheet("考勤记录")
                .doWrite(excelVoList);
    }

    @Override
    public void downloadStatistics(List<AttendanceStatistics> list, HttpServletResponse response) throws IOException {
        List<AttendanceStatisticsExcelVo> excelVoList = list.stream().map(statistics -> {
            AttendanceStatisticsExcelVo vo = new AttendanceStatisticsExcelVo();
            BeanUtils.copyProperties(statistics, vo);
            vo.setStartDate(DateUtil.format(statistics.getStartDate(), "yyyy-MM-dd"));
            vo.setEndDate(DateUtil.format(statistics.getEndDate(), "yyyy-MM-dd"));
            return vo;
        }).collect(Collectors.toList());

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("考勤统计", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), AttendanceStatisticsExcelVo.class)
                .sheet("考勤统计")
                .doWrite(excelVoList);
    }
}
