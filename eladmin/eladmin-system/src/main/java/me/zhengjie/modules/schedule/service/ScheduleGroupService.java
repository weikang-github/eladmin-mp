package me.zhengjie.modules.schedule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.schedule.domain.ScheduleGroup;
import me.zhengjie.modules.schedule.domain.dto.ScheduleGroupQueryCriteria;
import me.zhengjie.utils.PageResult;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ScheduleGroupService extends IService<ScheduleGroup> {

    ScheduleGroup findById(Long id);

    void create(ScheduleGroup resources);

    void update(ScheduleGroup resources);

    void delete(Set<Long> ids);

    PageResult<ScheduleGroup> queryAll(ScheduleGroupQueryCriteria criteria, Page<Object> page);

    List<ScheduleGroup> queryAll(ScheduleGroupQueryCriteria criteria);

    void download(List<ScheduleGroup> queryAll, HttpServletResponse response) throws IOException;

    void addMembers(Long groupId, List<Long> userIds);

    void removeMembers(Long groupId, List<Long> userIds);

    List<ScheduleGroup> findByDeptId(Long deptId);
}
