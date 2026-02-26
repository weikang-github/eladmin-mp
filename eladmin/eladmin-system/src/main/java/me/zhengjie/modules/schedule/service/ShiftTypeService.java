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
import me.zhengjie.modules.schedule.domain.ShiftType;
import me.zhengjie.modules.schedule.domain.dto.ShiftTypeQueryCriteria;
import me.zhengjie.utils.PageResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 班次类型Service
 * @author Zheng Jie
 * @date 2026-02-26
 */
public interface ShiftTypeService extends IService<ShiftType> {

    /**
     * 分页查询
     */
    PageResult<ShiftType> queryAll(ShiftTypeQueryCriteria criteria, Page<Object> page);

    /**
     * 查询全部
     */
    List<ShiftType> queryAll(ShiftTypeQueryCriteria criteria);

    /**
     * 根据ID查询
     */
    ShiftType findById(Long id);

    /**
     * 根据编码查询
     */
    ShiftType findByCode(String code);

    /**
     * 查询所有启用的班次类型
     */
    List<ShiftType> findAllEnabled();

    /**
     * 创建
     */
    void create(ShiftType resources);

    /**
     * 编辑
     */
    void update(ShiftType resources);

    /**
     * 删除
     */
    void delete(List<Long> ids);

    /**
     * 导出数据
     */
    void download(List<ShiftType> data, HttpServletResponse response) throws IOException;
}
