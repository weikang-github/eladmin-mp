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
import me.zhengjie.modules.schedule.domain.ShiftRotation;
import me.zhengjie.modules.schedule.domain.dto.RotationGenerateDTO;
import me.zhengjie.modules.schedule.domain.dto.ShiftRotationQueryCriteria;
import me.zhengjie.utils.PageResult;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Zheng Jie
 * @date 2025-04-10
 */
public interface ShiftRotationService extends IService<ShiftRotation> {

    /**
     * 查询所有轮班规则
     * @param criteria 查询条件
     * @param page 分页参数
     * @return /
     */
    PageResult<ShiftRotation> queryAll(ShiftRotationQueryCriteria criteria, Page<Object> page);

    /**
     * 查询所有轮班规则
     * @param criteria 查询条件
     * @return /
     */
    List<ShiftRotation> queryAll(ShiftRotationQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    ShiftRotation findById(Long id);

    /**
     * 创建
     * @param resources /
     */
    void create(ShiftRotation resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(ShiftRotation resources);

    /**
     * 删除
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 导出数据
     * @param rotations 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<ShiftRotation> rotations, HttpServletResponse response) throws IOException;

    /**
     * 根据轮班规则生成排班
     * @param generateDTO 生成参数
     * @return 生成的排班列表
     */
    List<Long> generateSchedules(RotationGenerateDTO generateDTO);

    /**
     * 验证轮班规则配置
     * @param resources /
     * @return 验证结果
     */
    Map<String, Object> validateRotation(ShiftRotation resources);

    /**
     * 根据ID生成排班
     * @param id 轮班规则ID
     * @param startDateStr 开始日期
     * @param endDateStr 结束日期
     * @return 生成结果
     */
    Map<String, Object> generateSchedule(Long id, String startDateStr, String endDateStr);

    /**
     * 根据状态查询轮班规则
     * @param enabled /
     * @return /
     */
    List<ShiftRotation> findByEnabled(Boolean enabled);
}
