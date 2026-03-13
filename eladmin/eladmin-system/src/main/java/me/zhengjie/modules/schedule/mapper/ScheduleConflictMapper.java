package me.zhengjie.modules.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.schedule.domain.ScheduleConflict;
import me.zhengjie.modules.schedule.domain.dto.ScheduleConflictQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ScheduleConflictMapper extends BaseMapper<ScheduleConflict> {

    List<ScheduleConflict> findAll(@Param("criteria") ScheduleConflictQueryCriteria criteria);

    IPage<ScheduleConflict> findAll(@Param("criteria") ScheduleConflictQueryCriteria criteria, Page<Object> page);

    List<ScheduleConflict> findByUserIdAndDateRange(@Param("userId") Long userId, 
                                                    @Param("startDate") LocalDate startDate, 
                                                    @Param("endDate") LocalDate endDate,
                                                    @Param("status") String status);

    void resolveConflict(@Param("conflictId") Long conflictId, @Param("status") String status);
}
