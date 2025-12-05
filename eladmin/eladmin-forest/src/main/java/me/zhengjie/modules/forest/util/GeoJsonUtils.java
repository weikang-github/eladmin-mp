package me.zhengjie.modules.forest.util;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import me.zhengjie.modules.forest.domain.ForestResource;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GeoJSON工具类
 * 用于几何数据与GeoJSON格式之间的转换
 */
public class GeoJsonUtils {

    private static final WKTReader wktReader = new WKTReader();
    private static final WKTWriter wktWriter = new WKTWriter();

    /**
     * 将WKT字符串转换为Geometry对象
     * @param wkt WKT格式的几何数据
     * @return Geometry对象
     * @throws Exception 转换异常
     */
    public static Geometry wktToGeometry(String wkt) throws Exception {
        if (wkt == null || wkt.trim().isEmpty()) {
            return null;
        }
        return wktReader.read(wkt);
    }

    /**
     * 将Geometry对象转换为WKT字符串
     * @param geometry Geometry对象
     * @return WKT格式的几何数据
     */
    public static String geometryToWkt(Geometry geometry) {
        if (geometry == null) {
            return null;
        }
        return wktWriter.write(geometry);
    }

    /**
     * 将Geometry对象转换为GeoJSON格式的字符串
     * @param geometry Geometry对象
     * @return GeoJSON字符串
     */
    public static String geometryToGeoJson(Geometry geometry) {
        if (geometry == null) {
            return null;
        }

        JSONObject geoJson = new JSONObject();
        geoJson.put("type", "Feature");

        JSONObject properties = new JSONObject();
        geoJson.put("properties", properties);

        JSONObject geometryJson = convertGeometryToGeoJson(geometry);
        geoJson.put("geometry", geometryJson);

        return geoJson.toJSONString();
    }

    /**
     * 将森林资源列表转换为GeoJSON格式
     * @param resources 森林资源列表
     * @return GeoJSON格式的Map
     */
    public static Map<String, Object> convertToGeoJSON(List<ForestResource> resources) {
        Map<String, Object> geoJson = new HashMap<>();
        geoJson.put("type", "FeatureCollection");

        JSONArray features = new JSONArray();

        for (ForestResource resource : resources) {
            JSONObject feature = new JSONObject();
            feature.put("type", "Feature");

            // 转换属性
            JSONObject properties = new JSONObject();
            properties.put("id", resource.getId());
            properties.put("forestBlockNo", resource.getForestBlockNo());
            properties.put("subBlockNo", resource.getSubBlockNo());
            properties.put("treeSpecies", resource.getTreeSpecies());
            properties.put("area", resource.getArea());
            properties.put("volume", resource.getVolume());

            feature.put("properties", properties);

            // 转换几何数据
            if (resource.getBoundary() != null) {
                JSONObject geometryJson = convertGeometryToGeoJson(resource.getBoundary());
                feature.put("geometry", geometryJson);
            }

            features.add(feature);
        }

        geoJson.put("features", features);

        return geoJson;
    }

    /**
     * 将Geometry对象转换为GeoJSON的geometry部分
     * @param geometry Geometry对象
     * @return GeoJSON geometry对象
     */
    private static JSONObject convertGeometryToGeoJson(Geometry geometry) {
        JSONObject geometryJson = new JSONObject();

        if (geometry instanceof Point) {
            Point point = (Point) geometry;
            geometryJson.put("type", "Point");
            geometryJson.put("coordinates", convertCoordinateToGeoJson(point.getCoordinate()));
        } else if (geometry instanceof LineString) {
            LineString lineString = (LineString) geometry;
            geometryJson.put("type", "LineString");
            geometryJson.put("coordinates", convertCoordinateSequenceToGeoJson(lineString.getCoordinates()));
        } else if (geometry instanceof Polygon) {
            Polygon polygon = (Polygon) geometry;
            geometryJson.put("type", "Polygon");
            geometryJson.put("coordinates", convertPolygonToGeoJson(polygon));
        } else if (geometry instanceof MultiPoint) {
            MultiPoint multiPoint = (MultiPoint) geometry;
            geometryJson.put("type", "MultiPoint");
            geometryJson.put("coordinates", convertGeometryCollectionToGeoJson(multiPoint));
        } else if (geometry instanceof MultiLineString) {
            MultiLineString multiLineString = (MultiLineString) geometry;
            geometryJson.put("type", "MultiLineString");
            geometryJson.put("coordinates", convertGeometryCollectionToGeoJson(multiLineString));
        } else if (geometry instanceof MultiPolygon) {
            MultiPolygon multiPolygon = (MultiPolygon) geometry;
            geometryJson.put("type", "MultiPolygon");
            geometryJson.put("coordinates", convertMultiPolygonToGeoJson(multiPolygon));
        } else if (geometry instanceof GeometryCollection) {
            GeometryCollection geometryCollection = (GeometryCollection) geometry;
            geometryJson.put("type", "GeometryCollection");
            geometryJson.put("geometries", convertGeometryCollectionToGeoJson(geometryCollection));
        }

        return geometryJson;
    }

    /**
     * 将Coordinate转换为GeoJSON坐标数组
     * @param coordinate Coordinate对象
     * @return GeoJSON坐标数组
     */
    private static JSONArray convertCoordinateToGeoJson(Coordinate coordinate) {
        JSONArray coordinates = new JSONArray();
        coordinates.add(coordinate.x);
        coordinates.add(coordinate.y);
        if (!Double.isNaN(coordinate.z)) {
            coordinates.add(coordinate.z);
        }
        return coordinates;
    }

    /**
     * 将Coordinate数组转换为GeoJSON坐标数组
     * @param coordinates Coordinate数组
     * @return GeoJSON坐标数组
     */
    private static JSONArray convertCoordinateSequenceToGeoJson(Coordinate[] coordinates) {
        JSONArray coordinateArray = new JSONArray();
        for (Coordinate coordinate : coordinates) {
            coordinateArray.add(convertCoordinateToGeoJson(coordinate));
        }
        return coordinateArray;
    }

    /**
     * 将Polygon转换为GeoJSON坐标数组
     * @param polygon Polygon对象
     * @return GeoJSON坐标数组
     */
    private static JSONArray convertPolygonToGeoJson(Polygon polygon) {
        JSONArray polygonArray = new JSONArray();

        // 添加外环
        polygonArray.add(convertCoordinateSequenceToGeoJson(polygon.getExteriorRing().getCoordinates()));

        // 添加内环
        for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
            polygonArray.add(convertCoordinateSequenceToGeoJson(polygon.getInteriorRingN(i).getCoordinates()));
        }

        return polygonArray;
    }

    /**
     * 将GeometryCollection转换为GeoJSON坐标数组
     * @param geometryCollection GeometryCollection对象
     * @return GeoJSON坐标数组
     */
    private static JSONArray convertGeometryCollectionToGeoJson(GeometryCollection geometryCollection) {
        JSONArray geometryArray = new JSONArray();
        for (int i = 0; i < geometryCollection.getNumGeometries(); i++) {
            Geometry geometry = geometryCollection.getGeometryN(i);
            if (geometry instanceof Point) {
                geometryArray.add(convertCoordinateToGeoJson(((Point) geometry).getCoordinate()));
            } else if (geometry instanceof LineString) {
                geometryArray.add(convertCoordinateSequenceToGeoJson(((LineString) geometry).getCoordinates()));
            } else if (geometry instanceof Polygon) {
                geometryArray.add(convertPolygonToGeoJson((Polygon) geometry));
            }
        }
        return geometryArray;
    }

    /**
     * 将MultiPolygon转换为GeoJSON坐标数组
     * @param multiPolygon MultiPolygon对象
     * @return GeoJSON坐标数组
     */
    private static JSONArray convertMultiPolygonToGeoJson(MultiPolygon multiPolygon) {
        JSONArray multiPolygonArray = new JSONArray();
        for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
            Polygon polygon = (Polygon) multiPolygon.getGeometryN(i);
            multiPolygonArray.add(convertPolygonToGeoJson(polygon));
        }
        return multiPolygonArray;
    }

    /**
     * 验证WKT是否为有效的多边形
     * @param wkt WKT字符串
     * @return 是否为有效的多边形
     */
    public static boolean isValidPolygonWkt(String wkt) {
        try {
            Geometry geometry = wktToGeometry(wkt);
            return geometry != null && geometry instanceof Polygon && geometry.isValid();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证Geometry是否为有效的多边形
     * @param geometry Geometry对象
     * @return 是否为有效的多边形
     */
    public static boolean isValidPolygon(Geometry geometry) {
        return geometry != null && geometry instanceof Polygon && geometry.isValid();
    }
}
