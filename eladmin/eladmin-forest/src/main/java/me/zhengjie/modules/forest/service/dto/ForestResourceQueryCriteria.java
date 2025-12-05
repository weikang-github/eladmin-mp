package me.zhengjie.modules.forest.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * 森林资源查询条件类
 * @author [你的名字]
 */
@Data
public class ForestResourceQueryCriteria {

    /**
     * 林班编号模糊查询
     */
    @Query(type = Query.Type.INNER_LIKE)
    private String forestClassNo;

    /**
     * 小班编号模糊查询
     */
    @Query(type = Query.Type.INNER_LIKE)
    private String subClassNo;

    /**
     * 树种模糊查询
     */
    @Query(type = Query.Type.INNER_LIKE)
    private String treeSpecies;

    /**
     * 面积大于等于
     */
    @Query(type = Query.Type.GREATER_THAN_EQUAL, propName = "area")
    private Double minArea;

    /**
     * 面积小于等于
     */
    @Query(type = Query.Type.LESS_THAN_EQUAL, propName = "area")
    private Double maxArea;

    /**
     * 蓄积量大于等于
     */
    @Query(type = Query.Type.GREATER_THAN_EQUAL, propName = "volume")
    private Double minVolume;

    /**
     * 蓄积量小于等于
     */
    @Query(type = Query.Type.LESS_THAN_EQUAL, propName = "volume")
    private Double maxVolume;

}