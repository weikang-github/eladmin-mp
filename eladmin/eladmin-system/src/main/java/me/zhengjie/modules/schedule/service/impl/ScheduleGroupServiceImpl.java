package me.zhengjie.modules.schedule.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.schedule.domain.ScheduleGroup;
import me.zhengjie.modules.schedule.domain.ScheduleGroupMember;
import me.zhengjie.modules.schedule.domain.dto.ScheduleGroupQueryCriteria;
import me.zhengjie.modules.schedule.mapper.ScheduleGroupMapper;
import me.zhengjie.modules.schedule.mapper.ScheduleGroupMemberMapper;
import me.zhengjie.modules.schedule.service.ScheduleGroupService;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ScheduleGroupServiceImpl extends ServiceImpl<ScheduleGroupMapper, ScheduleGroup> implements ScheduleGroupService {

    private final ScheduleGroupMapper scheduleGroupMapper;
    private final ScheduleGroupMemberMapper scheduleGroupMemberMapper;

    @Override
    public ScheduleGroup findById(Long id) {
        return getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ScheduleGroup resources) {
        save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScheduleGroup resources) {
        updateById(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        removeByIds(ids);
        for (Long groupId : ids) {
            scheduleGroupMemberMapper.deleteByGroupId(groupId);
        }
    }

    @Override
    public PageResult<ScheduleGroup> queryAll(ScheduleGroupQueryCriteria criteria, Page<Object> page) {
        criteria.setOffset(page.offset());
        List<ScheduleGroup> groups = scheduleGroupMapper.findAll(criteria);
        long total = count();
        return PageUtil.toPage(groups, total);
    }

    @Override
    public List<ScheduleGroup> queryAll(ScheduleGroupQueryCriteria criteria) {
        return scheduleGroupMapper.findAll(criteria);
    }

    @Override
    public void download(List<ScheduleGroup> queryAll, HttpServletResponse response) throws IOException {
        // Implementation for Excel download
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMembers(Long groupId, List<Long> userIds) {
        List<ScheduleGroupMember> members = new ArrayList<>();
        for (Long userId : userIds) {
            ScheduleGroupMember member = new ScheduleGroupMember();
            member.setGroupId(groupId);
            member.setUserId(userId);
            members.add(member);
        }
        scheduleGroupMemberMapper.batchInsert(members);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMembers(Long groupId, List<Long> userIds) {
        scheduleGroupMemberMapper.deleteByGroupIdAndUserIds(groupId, userIds);
    }

    @Override
    public List<ScheduleGroup> findByDeptId(Long deptId) {
        return lambdaQuery().eq(ScheduleGroup::getDeptId, deptId).list();
    }
}
