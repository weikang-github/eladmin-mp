package me.zhengjie.modules.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.schedule.domain.ScheduleGroup;
import me.zhengjie.modules.schedule.domain.dto.ScheduleGroupQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ScheduleGroupMapper extends BaseMapper<ScheduleGroup> {

    List<ScheduleGroup> findAll(@Param("criteria") ScheduleGroupQueryCriteria criteria);

    IPage<ScheduleGroup> findAll(@Param("criteria") ScheduleGroupQueryCriteria criteria, Page<Object> page);

    List<ScheduleGroup> findByDeptId(@Param("deptId") Long deptId);
}
