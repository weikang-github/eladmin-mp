package com.workflow.engine.executor;

import com.fasterxml.jackson.databind.JsonNode;
import com.workflow.engine.dto.DagNode;
import com.workflow.engine.entity.TaskExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class EmailTaskExecutor implements TaskExecutor {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailTaskExecutor.class);
    private static final String TASK_TYPE = "EMAIL";
    private static final int DEFAULT_TIMEOUT_SECONDS = 60; // 1 minute
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Override
    public String getTaskType() {
        return TASK_TYPE;
    }
    
    @Override
    public TaskExecutionResult execute(TaskExecution taskExecution, DagNode dagNode) throws Exception {
        logger.info("Executing email task: {}", taskExecution.getTaskName());
        
        JsonNode properties = dagNode.getProperties();
        
        // 获取邮件配置
        String smtpHost = properties.get("smtpHost").asText();
        int smtpPort = properties.get("smtpPort").asInt(587);
        String username = properties.get("username").asText();
        String password = properties.get("password").asText();
        boolean useTls = properties.get("useTls").asBoolean(true);
        
        // 获取邮件内容
        String from = properties.get("from").asText();
        String to = properties.get("to").asText();
        String cc = properties.has("cc") ? properties.get("cc").asText() : null;
        String bcc = properties.has("bcc") ? properties.get("bcc").asText() : null;
        String subject = properties.get("subject").asText();
        String body = properties.get("body").asText();
        boolean html = properties.has("html") && properties.get("html").asBoolean();
        
        try {
            // 构建邮件请求
            Map<String, Object> emailRequest = new HashMap<>();
            emailRequest.put("smtpHost", smtpHost);
            emailRequest.put("smtpPort", smtpPort);
            emailRequest.put("username", username);
            emailRequest.put("password", password);
            emailRequest.put("useTls", useTls);
            emailRequest.put("from", from);
            emailRequest.put("to", to);
            emailRequest.put("cc", cc);
            emailRequest.put("bcc", bcc);
            emailRequest.put("subject", subject);
            emailRequest.put("body", body);
            emailRequest.put("html", html);
            
            // 调用邮件服务API（这里模拟调用，实际应该配置真实的邮件服务）
            String emailServiceUrl = properties.has("emailServiceUrl") ? 
                    properties.get("emailServiceUrl").asText() : "http://localhost:8080/api/email/send";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(emailRequest, headers);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(emailServiceUrl, request, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                boolean success = responseBody != null && Boolean.TRUE.equals(responseBody.get("success"));
                
                if (success) {
                    return TaskExecutionResult.success(Map.of(
                            "messageId", responseBody.get("messageId"),
                            "recipient", to,
                            "subject", subject
                    ));
                } else {
                    String errorMessage = responseBody != null && responseBody.containsKey("message") ? 
                            responseBody.get("message").toString() : "Unknown error";
                    return TaskExecutionResult.failure("Failed to send email: " + errorMessage);
                }
            } else {
                return TaskExecutionResult.failure("Email service returned status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            logger.error("Failed to send email", e);
            return TaskExecutionResult.failure("Failed to send email: " + e.getMessage());
        }
    }
    
    @Override
    public void validateConfig(DagNode dagNode) throws IllegalArgumentException {
        JsonNode properties = dagNode.getProperties();
        
        if (properties == null) {
            throw new IllegalArgumentException("Email properties are required");
        }
        
        // 验证必填字段
        validateRequiredField(properties, "smtpHost", "SMTP host");
        validateRequiredField(properties, "smtpPort", "SMTP port");
        validateRequiredField(properties, "username", "Username");
        validateRequiredField(properties, "password", "Password");
        validateRequiredField(properties, "from", "From email");
        validateRequiredField(properties, "to", "To email");
        validateRequiredField(properties, "subject", "Subject");
        validateRequiredField(properties, "body", "Body");
        
        // 验证邮箱格式
        String from = properties.get("from").asText();
        String to = properties.get("to").asText();
        
        if (!isValidEmail(from)) {
            throw new IllegalArgumentException("Invalid from email format: " + from);
        }
        
        if (!isValidEmail(to)) {
            throw new IllegalArgumentException("Invalid to email format: " + to);
        }
        
        // 验证CC和BCC邮箱格式（如果提供）
        if (properties.has("cc")) {
            String cc = properties.get("cc").asText();
            if (!isValidEmail(cc)) {
                throw new IllegalArgumentException("Invalid cc email format: " + cc);
            }
        }
        
        if (properties.has("bcc")) {
            String bcc = properties.get("bcc").asText();
            if (!isValidEmail(bcc)) {
                throw new IllegalArgumentException("Invalid bcc email format: " + bcc);
            }
        }
    }
    
    @Override
    public Integer getTimeoutSeconds(DagNode dagNode) {
        JsonNode properties = dagNode.getProperties();
        if (properties != null && properties.has("timeoutSeconds")) {
            return properties.get("timeoutSeconds").asInt(DEFAULT_TIMEOUT_SECONDS);
        }
        return DEFAULT_TIMEOUT_SECONDS;
    }
    
    private void validateRequiredField(JsonNode properties, String fieldName, String fieldLabel) {
        if (!properties.has(fieldName) || properties.get(fieldName).asText().trim().isEmpty()) {
            throw new IllegalArgumentException(fieldLabel + " is required");
        }
    }
    
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        // 简单的邮箱格式验证
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}