package me.zhengjie.modules.forest.service.dto;

import lombok.Data;
import org.postgis.Geometry;

import java.io.Serializable;

/**
 * 森林资源DTO类
 * @author [你的名字]
 */
@Data
public class ForestResourceDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 林班编号
     */
    private String forestClassNo;

    /**
     * 小班编号
     */
    private String subClassNo;

    /**
     * 树种
     */
    private String treeSpecies;

    /**
     * 面积（公顷）
     */
    private Double area;

    /**
     * 蓄积量
     */
    private Double volume;

    /**
     * 几何边界（PostGIS的geometry类型）
     */
    private Geometry geometry;

}