package me.zhengjie.modules.schedule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.schedule.domain.Schedule;
import me.zhengjie.modules.schedule.domain.dto.BatchScheduleRequest;
import me.zhengjie.modules.schedule.domain.dto.ScheduleQueryCriteria;
import me.zhengjie.utils.PageResult;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface ScheduleService extends IService<Schedule> {

    List<Schedule> queryAll(ScheduleQueryCriteria criteria);

    PageResult<Schedule> queryAll(ScheduleQueryCriteria criteria, Page<Object> page);

    Schedule findById(Long id);

    void create(Schedule schedule);

    void update(Schedule schedule);

    void delete(Long id);

    void deleteAll(Long[] ids);

    List<Schedule> findByUserIdAndDateRange(Long userId, Date startDate, Date endDate);

    List<Schedule> findByDeptIdAndDateRange(Long deptId, Date startDate, Date endDate);

    void batchSchedule(BatchScheduleRequest request);

    void autoGenerateSchedule(Long deptId, Date startDate, Date endDate);

    Map<String, Object> checkConflicts(BatchScheduleRequest request);

    void generateMonthlySchedule(Long deptId, int year, int month);

    List<Schedule> findByDateRange(Date startDate, Date endDate);
}
