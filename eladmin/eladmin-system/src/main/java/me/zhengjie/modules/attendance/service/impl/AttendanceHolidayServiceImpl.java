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

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.attendance.domain.AttendanceHoliday;
import me.zhengjie.modules.attendance.domain.dto.AttendanceHolidayQueryCriteria;
import me.zhengjie.modules.attendance.domain.vo.AttendanceHolidayExcelVo;
import me.zhengjie.modules.attendance.mapper.AttendanceHolidayMapper;
import me.zhengjie.modules.attendance.service.AttendanceHolidayService;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 考勤节假日ServiceImpl
 * @author Zheng Jie
 * @date 2025-03-27
 */
@Service
@RequiredArgsConstructor
public class AttendanceHolidayServiceImpl extends ServiceImpl<AttendanceHolidayMapper, AttendanceHoliday> implements AttendanceHolidayService {

    private final AttendanceHolidayMapper attendanceHolidayMapper;

    @Override
    public PageResult<AttendanceHoliday> queryAll(AttendanceHolidayQueryCriteria criteria, Page<Object> page) {
        return PageUtil.toPage(attendanceHolidayMapper.findAll(page, criteria));
    }

    @Override
    public List<AttendanceHoliday> queryAll(AttendanceHolidayQueryCriteria criteria) {
        return attendanceHolidayMapper.findAll(criteria);
    }

    @Override
    public AttendanceHoliday findById(Long id) {
        return attendanceHolidayMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(AttendanceHoliday resources) {
        attendanceHolidayMapper.insert(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AttendanceHoliday resources) {
        attendanceHolidayMapper.updateById(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            attendanceHolidayMapper.deleteById(id);
        }
    }

    @Override
    public AttendanceHoliday findByDate(Date holidayDate) {
        return attendanceHolidayMapper.findByDate(holidayDate);
    }

    @Override
    public List<AttendanceHoliday> findByYear(Integer year) {
        return attendanceHolidayMapper.findByYear(year);
    }

    @Override
    public boolean isWorkDay(Date date, String workDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        // Calendar中周日是1,周一是2...周六是7,转换为1-7表示周一到周日
        int convertedDay = dayOfWeek == 1 ? 7 : dayOfWeek - 1;
        
        AttendanceHoliday holiday = findByDate(date);
        if (holiday != null) {
            // 如果是调休工作日,则需要上班
            return "WORKDAY".equals(holiday.getHolidayType());
        }
        
        // 检查是否是工作日
        if (workDays != null && !workDays.isEmpty()) {
            String[] days = workDays.split(",");
            for (String day : days) {
                if (Integer.parseInt(day.trim()) == convertedDay) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void download(List<AttendanceHoliday> list, HttpServletResponse response) throws IOException {
        List<AttendanceHolidayExcelVo> excelVoList = list.stream().map(holiday -> {
            AttendanceHolidayExcelVo vo = new AttendanceHolidayExcelVo();
            BeanUtils.copyProperties(holiday, vo);
            if ("HOLIDAY".equals(holiday.getHolidayType())) {
                vo.setHolidayType("节假日");
            } else if ("WORKDAY".equals(holiday.getHolidayType())) {
                vo.setHolidayType("调休工作日");
            }
            return vo;
        }).collect(Collectors.toList());

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("节假日", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), AttendanceHolidayExcelVo.class)
                .sheet("节假日")
                .doWrite(excelVoList);
    }
}
