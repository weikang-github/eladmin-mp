package me.zhengjie.modules.schedule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.schedule.domain.Schedule;
import me.zhengjie.modules.schedule.domain.dto.AutoScheduleRequest;
import me.zhengjie.modules.schedule.domain.dto.BatchScheduleRequest;
import me.zhengjie.modules.schedule.domain.dto.ScheduleConflictResult;
import me.zhengjie.modules.schedule.domain.dto.ScheduleQueryCriteria;
import me.zhengjie.utils.PageResult;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ScheduleService extends IService<Schedule> {

    Schedule findById(Long id);

    void create(Schedule resources);

    void update(Schedule resources);

    void delete(Set<Long> ids);

    PageResult<Schedule> queryAll(ScheduleQueryCriteria criteria, Page<Object> page);

    List<Schedule> queryAll(ScheduleQueryCriteria criteria);

    void download(List<Schedule> queryAll, HttpServletResponse response) throws IOException;

    List<Schedule> findByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);

    List<Schedule> findByDeptIdAndDateRange(Long deptId, LocalDate startDate, LocalDate endDate);

    ScheduleConflictResult detectConflicts(Schedule schedule);

    ScheduleConflictResult detectConflicts(BatchScheduleRequest request);

    void batchSchedule(BatchScheduleRequest request);

    void autoSchedule(AutoScheduleRequest request);

    void generateMonthlySchedule(Integer year, Integer month, Long groupId);

    void deleteByDateRange(LocalDate startDate, LocalDate endDate, Long userId);
}
