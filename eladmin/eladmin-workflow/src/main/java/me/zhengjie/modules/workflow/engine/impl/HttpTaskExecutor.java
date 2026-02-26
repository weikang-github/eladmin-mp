package me.zhengjie.modules.workflow.engine.impl;

import me.zhengjie.modules.workflow.domain.TaskExecution;
import me.zhengjie.modules.workflow.engine.TaskExecutor;
import me.zhengjie.modules.workflow.engine.model.Node;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP任务执行器
 * @author workflow-engine
 */
@Component
public class HttpTaskExecutor implements TaskExecutor {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getTaskType() {
        return "http";
    }

    @Override
    public Map<String, Object> execute(Node node, Map<String, Object> executionContext, TaskExecution taskExecution) {
        Map<String, Object> result = new HashMap<>();

        try {
            Map<String, Object> config = node.getConfig();
            String url = (String) config.get("url");
            String method = (String) config.getOrDefault("method", "GET");
            Map<String, Object> headers = (Map<String, Object>) config.getOrDefault("headers", new HashMap<>());
            Map<String, Object> body = (Map<String, Object>) config.getOrDefault("body", new HashMap<>());

            // 创建HTTP请求头
            HttpHeaders httpHeaders = new HttpHeaders();
            headers.forEach((key, value) -> httpHeaders.add(key, value.toString()));

            // 创建HTTP请求实体
            HttpEntity<Object> requestEntity = new HttpEntity<>(body, httpHeaders);

            // 发送HTTP请求
            ResponseEntity<Object> responseEntity = restTemplate.exchange(
                    url, 
                    HttpMethod.valueOf(method.toUpperCase()),
                    requestEntity,
                    Object.class
            );

            // 处理响应结果
            result.put("statusCode", responseEntity.getStatusCodeValue());
            result.put("body", responseEntity.getBody());
            result.put("headers", responseEntity.getHeaders());

            return result;
        } catch (Exception e) {
            throw new RuntimeException("HTTP task execution failed: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean validate(Node node) {
        Map<String, Object> config = node.getConfig();
        return config != null && StringUtils.isNotBlank((String) config.get("url"));
    }
}
