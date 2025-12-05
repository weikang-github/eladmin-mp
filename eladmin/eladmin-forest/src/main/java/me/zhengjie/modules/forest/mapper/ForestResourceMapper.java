package me.zhengjie.modules.forest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.modules.forest.domain.ForestResource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.postgis.Geometry;
import org.postgis.PGgeometry;

import java.util.List;

/**
 * 森林资源Mapper接口
 * @author [你的名字]
 */
public interface ForestResourceMapper extends BaseMapper<ForestResource> {

    /**
     * 按地图视窗范围查询（使用ST_Within）
     * @param bbox 边界框，格式：minx,miny,maxx,maxy
     * @return 森林资源列表
     */
    @Select("<script>" +
            "SELECT * FROM forest_resource WHERE ST_Within(geometry, ST_MakeEnvelope(#{minx}, #{miny}, #{maxx}, #{maxy}, 4326))" +
            "</script>")
    List<ForestResource> findByBbox(@Param("minx") Double minx, @Param("miny") Double miny, @Param("maxx") Double maxx, @Param("miny") Double maxy);

}