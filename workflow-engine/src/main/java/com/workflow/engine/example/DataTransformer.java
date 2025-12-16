package com.workflow.engine.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据转换器示例
 */
@Component
public class DataTransformer {
    
    private static final Logger logger = LoggerFactory.getLogger(DataTransformer.class);
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * 转换数据格式
     */
    public Map<String, Object> transformData(String dataJson, String format) {
        logger.info("Transforming data to format: {}", format);
        
        try {
            JsonNode data = objectMapper.readTree(dataJson);
            
            Map<String, Object> result = new HashMap<>();
            
            switch (format.toLowerCase()) {
                case "json":
                    result = transformToJson(data);
                    break;
                case "csv":
                    result = transformToCsv(data);
                    break;
                case "xml":
                    result = transformToXml(data);
                    break;
                default:
                    result.put("status", "error");
                    result.put("message", "Unsupported format: " + format);
            }
            
            result.put("format", format);
            result.put("timestamp", System.currentTimeMillis());
            
            logger.info("Data transformation completed successfully");
            return result;
            
        } catch (Exception e) {
            logger.error("Failed to transform data", e);
            
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "Failed to transform data: " + e.getMessage());
            
            return errorResult;
        }
    }
    
    /**
     * 转换为JSON格式
     */
    private Map<String, Object> transformToJson(JsonNode data) {
        Map<String, Object> result = new HashMap<>();
        
        if (data.isArray()) {
            List<Map<String, Object>> items = new ArrayList<>();
            data.forEach(item -> {
                Map<String, Object> itemMap = new HashMap<>();
                item.fields().forEachRemaining(field -> {
                    itemMap.put(field.getKey(), field.getValue().asText());
                });
                items.add(itemMap);
            });
            
            result.put("data", items);
            result.put("count", items.size());
        } else {
            result.put("data", data);
            result.put("count", 1);
        }
        
        result.put("status", "success");
        return result;
    }
    
    /**
     * 转换为CSV格式
     */
    private Map<String, Object> transformToCsv(JsonNode data) {
        Map<String, Object> result = new HashMap<>();
        
        if (data.isArray() && !data.isEmpty()) {
            StringBuilder csv = new StringBuilder();
            
            // 获取标题行
            JsonNode firstItem = data.get(0);
            List<String> headers = new ArrayList<>();
            firstItem.fieldNames().forEachRemaining(headers::add);
            
            // 添加标题行
            csv.append(String.join(",", headers)).append("\n");
            
            // 添加数据行
            data.forEach(item -> {
                List<String> values = new ArrayList<>();
                headers.forEach(header -> {
                    JsonNode value = item.get(header);
                    values.add(value != null ? value.asText() : "");
                });
                csv.append(String.join(",", values)).append("\n");
            });
            
            result.put("data", csv.toString());
            result.put("count", data.size());
        } else {
            result.put("status", "error");
            result.put("message", "CSV transformation requires an array of objects");
        }
        
        result.put("status", "success");
        return result;
    }
    
    /**
     * 转换为XML格式
     */
    private Map<String, Object> transformToXml(JsonNode data) {
        Map<String, Object> result = new HashMap<>();
        
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<root>\n");
        
        if (data.isArray()) {
            xml.append("<items>\n");
            data.forEach(item -> {
                xml.append("  <item>\n");
                item.fields().forEachRemaining(field -> {
                    xml.append("    <").append(field.getKey()).append(">")
                       .append(field.getValue().asText())
                       .append("</").append(field.getKey()).append(">\n");
                });
                xml.append("  </item>\n");
            });
            xml.append("</items>\n");
            result.put("count", data.size());
        } else {
            xml.append("<item>\n");
            data.fields().forEachRemaining(field -> {
                xml.append("  <").append(field.getKey()).append(">")
                   .append(field.getValue().asText())
                   .append("</").append(field.getKey()).append(">\n");
            });
            xml.append("</item>\n");
            result.put("count", 1);
        }
        
        xml.append("</root>");
        
        result.put("data", xml.toString());
        result.put("status", "success");
        return result;
    }
}