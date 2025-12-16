package com.workflow.engine.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP响应处理器示例
 */
@Component
public class HttpResponseProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseProcessor.class);
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * 处理HTTP响应
     */
    public Map<String, Object> processResponse(String responseJson) {
        logger.info("Processing HTTP response: {}", responseJson);
        
        try {
            JsonNode response = objectMapper.readTree(responseJson);
            
            Map<String, Object> result = new HashMap<>();
            
            // 提取关键信息
            if (response.has("login")) {
                result.put("username", response.get("login").asText());
            }
            
            if (response.has("name")) {
                result.put("fullName", response.get("name").asText());
            }
            
            if (response.has("public_repos")) {
                result.put("publicRepos", response.get("public_repos").asInt());
            }
            
            if (response.has("followers")) {
                result.put("followers", response.get("followers").asInt());
            }
            
            result.put("status", "success");
            result.put("message", "HTTP response processed successfully");
            
            logger.info("HTTP response processed successfully: {}", result);
            return result;
            
        } catch (Exception e) {
            logger.error("Failed to process HTTP response", e);
            
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "Failed to process HTTP response: " + e.getMessage());
            
            return errorResult;
        }
    }
    
    /**
     * 处理错误响应
     */
    public Map<String, Object> processError(String errorMessage) {
        logger.error("Processing HTTP error: {}", errorMessage);
        
        Map<String, Object> result = new HashMap<>();
        result.put("status", "error");
        result.put("message", errorMessage);
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }
}