package me.zhengjie.modules.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.schedule.domain.Shift;
import me.zhengjie.modules.schedule.domain.dto.ShiftQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShiftMapper extends BaseMapper<Shift> {

    Long countAll(@Param("criteria") ShiftQueryCriteria criteria);

    IPage<Shift> findAll(@Param("criteria") ShiftQueryCriteria criteria, Page<Object> page);

    Shift findByCode(@Param("shiftCode") String shiftCode);
}
