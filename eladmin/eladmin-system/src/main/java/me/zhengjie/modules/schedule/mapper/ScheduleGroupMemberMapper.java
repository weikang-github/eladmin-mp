package me.zhengjie.modules.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.modules.schedule.domain.ScheduleGroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ScheduleGroupMemberMapper extends BaseMapper<ScheduleGroupMember> {

    List<ScheduleGroupMember> findByGroupId(@Param("groupId") Long groupId);

    List<ScheduleGroupMember> findByUserId(@Param("userId") Long userId);

    void deleteByGroupId(@Param("groupId") Long groupId);

    void deleteByGroupIdAndUserIds(@Param("groupId") Long groupId, @Param("userIds") List<Long> userIds);

    void batchInsert(@Param("members") List<ScheduleGroupMember> members);
}
