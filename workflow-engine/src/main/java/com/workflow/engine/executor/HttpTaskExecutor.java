package com.workflow.engine.executor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workflow.engine.dto.DagNode;
import com.workflow.engine.entity.TaskExecution;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * HTTP任务执行器
 * 负责执行HTTP请求类型的任务
 * 
 * @author workflow-engine
 * @since 1.0.0
 */
@Component
public class HttpTaskExecutor implements TaskExecutor {

    private static final Logger logger = LoggerFactory.getLogger(HttpTaskExecutor.class);

    private static final String TASK_TYPE = "HTTP";
    private static final int DEFAULT_TIMEOUT_SECONDS = 30;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String getTaskType() {
        return TASK_TYPE;
    }

    @Override
    public TaskExecutionResult execute(TaskExecution taskExecution, DagNode dagNode) throws Exception {
        logger.info("开始执行HTTP任务: {} - {}", taskExecution.getTaskId(), taskExecution.getTaskName());

        try {
            // 获取任务配置
            JsonNode config = taskExecution.getTaskConfig();
            if (config == null) {
                return TaskExecutionResult.failure("任务配置不能为空");
            }

            // 解析配置参数
            String url = config.get("url").asText();
            String method = config.get("method").asText("GET").toUpperCase();
            JsonNode headersNode = config.get("headers");
            JsonNode bodyNode = config.get("body");
            Integer timeout = config.has("timeout") ? config.get("timeout").asInt(DEFAULT_TIMEOUT_SECONDS) : DEFAULT_TIMEOUT_SECONDS;

            // 创建HTTP客户端
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(timeout * 1000)
                    .setSocketTimeout(timeout * 1000)
                    .setConnectionRequestTimeout(timeout * 1000)
                    .build();

            try (CloseableHttpClient httpClient = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .build()) {

                // 创建HTTP请求
                HttpUriRequest request = createHttpRequest(url, method, headersNode, bodyNode);

                // 执行任务
                long startTime = System.currentTimeMillis();
                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    long duration = System.currentTimeMillis() - startTime;
                    
                    int statusCode = response.getStatusLine().getStatusCode();
                    String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

                    logger.info("HTTP任务执行完成: {} - 状态码: {} - 耗时: {}ms", 
                               taskExecution.getTaskId(), statusCode, duration);

                    // 构建响应结果
                    Map<String, Object> result = Map.of(
                        "statusCode", statusCode,
                        "responseBody", responseBody,
                        "duration", duration,
                        "headers", extractHeaders(response)
                    );

                    // 检查响应状态
                    if (statusCode >= 200 && statusCode < 300) {
                        return TaskExecutionResult.success(result);
                    } else {
                        return TaskExecutionResult.failure(
                            String.format("HTTP请求失败 - 状态码: %d, 响应: %s", statusCode, responseBody)
                        );
                    }
                }
            }

        } catch (Exception e) {
            logger.error("HTTP任务执行失败: {} - {}", taskExecution.getTaskId(), e.getMessage());
            return TaskExecutionResult.failure("HTTP请求执行失败: " + e.getMessage());
        }
    }

    /**
     * 创建HTTP请求
     */
    private HttpUriRequest createHttpRequest(String url, String method, JsonNode headersNode, JsonNode bodyNode) {
        HttpUriRequest request;

        switch (method) {
            case "GET":
                request = new HttpGet(url);
                break;
            case "POST":
                HttpPost post = new HttpPost(url);
                if (bodyNode != null) {
                    StringEntity entity = new StringEntity(bodyNode.toString(), ContentType.APPLICATION_JSON);
                    post.setEntity(entity);
                }
                request = post;
                break;
            case "PUT":
                HttpPut put = new HttpPut(url);
                if (bodyNode != null) {
                    StringEntity entity = new StringEntity(bodyNode.toString(), ContentType.APPLICATION_JSON);
                    put.setEntity(entity);
                }
                request = put;
                break;
            case "DELETE":
                request = new HttpDelete(url);
                break;
            case "PATCH":
                HttpPatch patch = new HttpPatch(url);
                if (bodyNode != null) {
                    StringEntity entity = new StringEntity(bodyNode.toString(), ContentType.APPLICATION_JSON);
                    patch.setEntity(entity);
                }
                request = patch;
                break;
            default:
                throw new IllegalArgumentException("不支持的HTTP方法: " + method);
        }

        // 设置请求头
        if (headersNode != null && headersNode.isObject()) {
            headersNode.fields().forEachRemaining(field -> {
                request.setHeader(field.getKey(), field.getValue().asText());
            });
        }

        // 默认设置Content-Type
        if (request instanceof HttpEntityEnclosingRequestBase && !request.containsHeader("Content-Type")) {
            request.setHeader("Content-Type", "application/json");
        }

        return request;
    }

    /**
     * 提取响应头
     */
    private Map<String, String> extractHeaders(HttpResponse response) {
        Map<String, String> headers = new HashMap<>();
        Arrays.stream(response.getAllHeaders()).forEach(header -> {
            headers.put(header.getName(), header.getValue());
        });
        return headers;
    }

    @Override
    public void validateConfig(DagNode dagNode) throws IllegalArgumentException {
        Map<String, Object> properties = dagNode.getProperties();
        if (properties == null) {
            throw new IllegalArgumentException("HTTP任务配置不能为空");
        }

        // 验证URL
        String url = (String) properties.get("url");
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("HTTP任务URL不能为空");
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new IllegalArgumentException("HTTP任务URL必须以http://或https://开头");
        }

        // 验证HTTP方法
        String method = (String) properties.getOrDefault("method", "GET");
        if (!Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH").contains(method.toUpperCase())) {
            throw new IllegalArgumentException("不支持的HTTP方法: " + method);
        }

        logger.debug("HTTP任务配置验证通过: {}", dagNode.getId());
    }

    @Override
    public Integer getTimeoutSeconds(DagNode dagNode) {
        Map<String, Object> properties = dagNode.getProperties();
        if (properties != null && properties.containsKey("timeout")) {
            Object timeoutValue = properties.get("timeout");
            if (timeoutValue instanceof Integer) {
                return (Integer) timeoutValue;
            } else if (timeoutValue instanceof String) {
                try {
                    return Integer.parseInt((String) timeoutValue);
                } catch (NumberFormatException e) {
                    logger.warn("无效的超时时间配置: {}, 使用默认值", timeoutValue);
                }
            }
        }
        return DEFAULT_TIMEOUT_SECONDS;
    }
}