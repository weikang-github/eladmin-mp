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
package me.zhengjie.modules.forest.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import org.locationtech.jts.geom.Geometry;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * 森林资源实体类
 * @author Zheng Jie
 * @date 2025-01-01
 */
@Getter
@Setter
@TableName("forest_resource")
public class ForestResource extends BaseEntity implements Serializable {

    public interface Update {}


    @NotNull(groups = Update.class)
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "林班编号")
    private String forestClassNumber;

    @NotBlank
    @ApiModelProperty(value = "小班编号")
    private String subClassNumber;

    @NotBlank
    @ApiModelProperty(value = "树种")
    private String treeSpecies;

    @NotNull
    @ApiModelProperty(value = "面积（公顷）")
    private BigDecimal area;

    @NotNull
    @ApiModelProperty(value = "蓄积量")
    private BigDecimal volume;

    @NotNull
    @ApiModelProperty(value = "几何边界")
    @TableField(typeHandler = org.locationtech.jts.geom.Geometry.class)
    private Geometry geometry;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ForestResource that = (ForestResource) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(forestClassNumber, that.forestClassNumber) &&
                Objects.equals(subClassNumber, that.subClassNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, forestClassNumber, subClassNumber);
    }
}