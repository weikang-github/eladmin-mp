/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.schedule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.schedule.domain.ScheduleRule;
import me.zhengjie.modules.schedule.domain.dto.ScheduleRuleQueryCriteria;
import me.zhengjie.utils.PageResult;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author Zheng Jie
 * @date 2025-04-10
 */
public interface ScheduleRuleService extends IService<ScheduleRule> {

    /**
     * 查询所有排班规则
     * @param criteria 查询条件
     * @param page 分页参数
     * @return /
     */
    PageResult<ScheduleRule> queryAll(ScheduleRuleQueryCriteria criteria, Page<Object> page);

    /**
     * 查询所有排班规则
     * @param criteria 查询条件
     * @return /
     */
    List<ScheduleRule> queryAll(ScheduleRuleQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    ScheduleRule findById(Long id);

    /**
     * 创建
     * @param resources /
     */
    void create(ScheduleRule resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(ScheduleRule resources);

    /**
     * 删除
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 导出数据
     * @param rules 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<ScheduleRule> rules, HttpServletResponse response) throws IOException;

    /**
     * 根据类型查询启用的规则
     * @param type /
     * @return /
     */
    List<ScheduleRule> findEnabledByType(String type);

    /**
     * 获取所有启用的规则
     * @return /
     */
    List<ScheduleRule> findAllEnabled();

    /**
     * 验证排班规则
     * @param resources /
     */
    void validateRule(ScheduleRule resources);
}
