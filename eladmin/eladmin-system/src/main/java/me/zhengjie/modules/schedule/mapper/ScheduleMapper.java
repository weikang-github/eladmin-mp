package me.zhengjie.modules.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.schedule.domain.Schedule;
import me.zhengjie.modules.schedule.domain.dto.ScheduleQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {

    Long countAll(@Param("criteria") ScheduleQueryCriteria criteria);

    IPage<Schedule> findAll(@Param("criteria") ScheduleQueryCriteria criteria, Page<Object> page);

    List<Schedule> findByUserAndDateRange(@Param("userId") Long userId, 
                                          @Param("startDate") Date startDate, 
                                          @Param("endDate") Date endDate);

    List<Schedule> findByDeptAndDateRange(@Param("deptId") Long deptId, 
                                           @Param("startDate") Date startDate, 
                                           @Param("endDate") Date endDate);

    List<Schedule> findByDateRange(@Param("startDate") Date startDate, 
                                   @Param("endDate") Date endDate);

    int countByUserAndDate(@Param("userId") Long userId, @Param("scheduleDate") Date scheduleDate);

    void deleteByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    void deleteByUserIdsAndDateRange(@Param("userIds") Set<Long> userIds, 
                                     @Param("startDate") Date startDate, 
                                     @Param("endDate") Date endDate);
}
