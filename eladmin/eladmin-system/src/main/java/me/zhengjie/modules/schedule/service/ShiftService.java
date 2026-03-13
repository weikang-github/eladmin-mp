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
import me.zhengjie.modules.schedule.domain.Shift;
import me.zhengjie.modules.schedule.domain.dto.ShiftQueryCriteria;
import me.zhengjie.utils.PageResult;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author Zheng Jie
 * @date 2025-04-10
 */
public interface ShiftService extends IService<Shift> {

    /**
     * 查询所有班次
     * @param criteria 查询条件
     * @param page 分页参数
     * @return /
     */
    PageResult<Shift> queryAll(ShiftQueryCriteria criteria, Page<Object> page);

    /**
     * 查询所有班次
     * @param criteria 查询条件
     * @return /
     */
    List<Shift> queryAll(ShiftQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    Shift findById(Long id);

    /**
     * 创建
     * @param resources /
     */
    void create(Shift resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(Shift resources);

    /**
     * 删除
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 导出数据
     * @param shifts 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<Shift> shifts, HttpServletResponse response) throws IOException;

    /**
     * 根据编码查询
     * @param code /
     * @return /
     */
    Shift findByCode(String code);

    /**
     * 根据类型查询
     * @param type /
     * @return /
     */
    List<Shift> findByType(String type);

    /**
     * 根据状态查询
     * @param enabled /
     * @return /
     */
    List<Shift> findByEnabled(Boolean enabled);
}
