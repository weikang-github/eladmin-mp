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
import me.zhengjie.modules.schedule.domain.RotationRule;
import me.zhengjie.modules.schedule.domain.dto.RotationRuleQueryCriteria;
import me.zhengjie.utils.PageResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 轮班规则Service
 * @author Zheng Jie
 * @date 2026-02-26
 */
public interface RotationRuleService extends IService<RotationRule> {

    /**
     * 分页查询
     */
    PageResult<RotationRule> queryAll(RotationRuleQueryCriteria criteria, Page<Object> page);

    /**
     * 查询全部
     */
    List<RotationRule> queryAll(RotationRuleQueryCriteria criteria);

    /**
     * 根据ID查询
     */
    RotationRule findById(Long id);

    /**
     * 根据编码查询
     */
    RotationRule findByCode(String code);

    /**
     * 查询所有启用的轮班规则
     */
    List<RotationRule> findAllEnabled();

    /**
     * 创建
     */
    void create(RotationRule resources);

    /**
     * 编辑
     */
    void update(RotationRule resources);

    /**
     * 删除
     */
    void delete(List<Long> ids);

    /**
     * 导出数据
     */
    void download(List<RotationRule> data, HttpServletResponse response) throws IOException;
}
