package me.zhengjie.modules.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.schedule.domain.Shift;
import me.zhengjie.modules.schedule.domain.dto.ShiftQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ShiftMapper extends BaseMapper<Shift> {

    List<Shift> findAll(@Param("criteria") ShiftQueryCriteria criteria);

    IPage<Shift> findAll(@Param("criteria") ShiftQueryCriteria criteria, Page<Object> page);
}
