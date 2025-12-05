package me.zhengjie.modules.forest.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.postgis.Geometry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

/**
 * 森林资源实体类
 * @author [你的名字]
 */
@Data
@Entity
@TableName("forest_resource")
public class ForestResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(columnDefinition = "geometry(Polygon, 4326)")
    private Geometry geometry;

}