package me.zhengjie.modules.schedule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.schedule.domain.Schedule;
import me.zhengjie.modules.schedule.domain.dto.ScheduleQueryCriteria;
import me.zhengjie.utils.PageResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ScheduleService extends IService<Schedule> {

    PageResult<Schedule> queryAll(ScheduleQueryCriteria criteria, Page<Object> page);

    List<Schedule> queryAll(ScheduleQueryCriteria criteria);

    void create(Schedule resources);

    void update(Schedule resources);

    void delete(Set<Long> ids);

    void deleteByDateRange(Date startDate, Date endDate);

    void deleteByUserIdsAndDateRange(Set<Long> userIds, Date startDate, Date endDate);

    void download(List<Schedule> queryAll, HttpServletResponse response) throws IOException;

    List<Schedule> findByUserAndDateRange(Long userId, Date startDate, Date endDate);

    List<Schedule> findByDeptAndDateRange(Long deptId, Date startDate, Date endDate);

    Map<String, Object> checkConflict(Schedule schedule);

    void generateSchedule(Long ruleId, Set<Long> userIds, Date startDate, Date endDate);

    void generateScheduleByDept(Long ruleId, Long deptId, Date startDate, Date endDate);

    List<Schedule> getCalendarData(Date startDate, Date endDate);
}
