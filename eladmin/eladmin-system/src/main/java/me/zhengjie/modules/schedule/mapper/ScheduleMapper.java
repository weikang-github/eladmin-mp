package me.zhengjie.modules.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.schedule.domain.Schedule;
import me.zhengjie.modules.schedule.domain.dto.ScheduleQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.sql.Date;
import java.util.List;

@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {

    List<Schedule> findAll(@Param("criteria") ScheduleQueryCriteria criteria);

    IPage<Schedule> findAll(@Param("criteria") ScheduleQueryCriteria criteria, Page<Object> page);

    List<Schedule> findByUserIdAndDateRange(@Param("userId") Long userId,
                                             @Param("startDate") Date startDate,
                                             @Param("endDate") Date endDate);

    List<Schedule> findByDeptIdAndDateRange(@Param("deptId") Long deptId,
                                             @Param("startDate") Date startDate,
                                             @Param("endDate") Date endDate);

    @Select("SELECT * FROM sch_schedule WHERE user_id = #{userId} AND schedule_date = #{date}")
    Schedule findByUserIdAndDate(@Param("userId") Long userId, @Param("date") Date date);

    void batchInsert(@Param("list") List<Schedule> schedules);

    void batchUpdate(@Param("list") List<Schedule> schedules);

    int deleteByDateRange(@Param("startDate") Date startDate,
                          @Param("endDate") Date endDate,
                          @Param("excludeLocked") Boolean excludeLocked);
}
