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
package me.zhengjie.modules.forest.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.annotation.Query;

import java.math.BigDecimal;

/**
 * 森林资源查询条件类
 * @author Zheng Jie
 * @date 2025-01-01
 */
@Getter
@Setter
public class ForestResourceQueryCriteria {

    @ApiModelProperty(value = "林班编号")
    @Query(blurry = "forestClassNumber")
    private String forestClassNumber;

    @ApiModelProperty(value = "小班编号")
    @Query(blurry = "subClassNumber")
    private String subClassNumber;

    @ApiModelProperty(value = "树种")
    @Query(blurry = "treeSpecies")
    private String treeSpecies;

    @ApiModelProperty(value = "面积最小值（公顷）")
    @Query(gte = "area")
    private BigDecimal minArea;

    @ApiModelProperty(value = "面积最大值（公顷）")
    @Query(lte = "area")
    private BigDecimal maxArea;

    @ApiModelProperty(value = "蓄积量最小值")
    @Query(gte = "volume")
    private BigDecimal minVolume;

    @ApiModelProperty(value = "蓄积量最大值")
    @Query(lte = "volume")
    private BigDecimal maxVolume;

    @ApiModelProperty(value = "地图视窗范围（bbox），格式：minX,minY,maxX,maxY")
    private String bbox;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;

    @ApiModelProperty(value = "每页数据量", example = "10")
    private Integer size = 10;
}