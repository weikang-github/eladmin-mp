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
package me.zhengjie.modules.forest.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.forest.domain.ForestResource;
import me.zhengjie.modules.forest.domain.dto.ForestResourceQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 森林资源Service接口
 * @author Zheng Jie
 * @date 2025-01-01
 */
public interface ForestResourceService extends IService<ForestResource> {

    /**
     * 根据ID查询森林资源
     * @param id ID
     * @return 森林资源
     */
    ForestResource findById(Long id);

    /**
     * 新增森林资源
     * @param resources 森林资源
     */
    void create(ForestResource resources);

    /**
     * 更新森林资源
     * @param resources 森林资源
     */
    void update(ForestResource resources);

    /**
     * 删除森林资源
     * @param ids 森林资源ID集合
     */
    void delete(Set<Long> ids);

    /**
     * 查询全部森林资源
     * @param criteria 查询条件
     * @param page 分页参数
     * @return 分页结果
     */
    PageResult<ForestResource> queryAll(ForestResourceQueryCriteria criteria, Page<Object> page);

    /**
     * 查询全部森林资源不分页
     * @param criteria 查询条件
     * @return 森林资源列表
     */
    List<ForestResource> queryAll(ForestResourceQueryCriteria criteria);

    /**
     * 根据地图视窗范围查询森林资源
     * @param bbox 地图视窗范围，格式：minX,minY,maxX,maxY
     * @return 森林资源列表
     */
    List<ForestResource> findByBbox(String bbox);

    /**
     * 导出森林资源数据
     * @param resources 森林资源列表
     * @param response 响应对象
     * @throws IOException IO异常
     */
    void download(List<ForestResource> resources, HttpServletResponse response) throws IOException;
}