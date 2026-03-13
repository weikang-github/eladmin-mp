package me.zhengjie.modules.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.schedule.domain.Schedule;
import me.zhengjie.modules.schedule.domain.dto.ScheduleQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {

    List<Schedule> findAll(@Param("criteria") ScheduleQueryCriteria criteria);

    IPage<Schedule> findAll(@Param("criteria") ScheduleQueryCriteria criteria, Page<Object> page);

    List<Schedule> findByUserIdAndDateRange(@Param("userId") Long userId, 
                                           @Param("startDate") LocalDate startDate, 
                                           @Param("endDate") LocalDate endDate);

    List<Schedule> findByDeptIdAndDateRange(@Param("deptId") Long deptId, 
                                           @Param("startDate") LocalDate startDate, 
                                           @Param("endDate") LocalDate endDate);

    List<Schedule> findConflicts(@Param("userId") Long userId, 
                                @Param("scheduleDate") LocalDate scheduleDate, 
                                @Param("excludeScheduleId") Long excludeScheduleId);

    void batchInsert(@Param("schedules") List<Schedule> schedules);

    void deleteByDateRange(@Param("startDate") LocalDate startDate, 
                          @Param("endDate") LocalDate endDate,
                          @Param("userId") Long userId);
}
