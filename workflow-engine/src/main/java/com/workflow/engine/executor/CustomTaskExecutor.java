package com.workflow.engine.executor;

import com.fasterxml.jackson.databind.JsonNode;
import com.workflow.engine.dto.DagNode;
import com.workflow.engine.entity.TaskExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomTaskExecutor implements TaskExecutor {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomTaskExecutor.class);
    private static final String TASK_TYPE = "CUSTOM";
    private static final int DEFAULT_TIMEOUT_SECONDS = 300; // 5 minutes
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Override
    public String getTaskType() {
        return TASK_TYPE;
    }
    
    @Override
    public TaskExecutionResult execute(TaskExecution taskExecution, DagNode dagNode) throws Exception {
        logger.info("Executing custom task: {}", taskExecution.getTaskName());
        
        JsonNode properties = dagNode.getProperties();
        
        // 获取自定义任务配置
        String className = properties.get("className").asText();
        String methodName = properties.get("methodName").asText();
        JsonNode parameters = properties.get("parameters");
        
        try {
            // 获取Spring Bean或创建实例
            Object beanInstance = getBeanInstance(className);
            
            // 获取方法
            Method method = findMethod(beanInstance.getClass(), methodName, parameters);
            
            // 准备方法参数
            Object[] methodArgs = prepareMethodArguments(method, parameters, taskExecution);
            
            // 调用方法
            Object result = method.invoke(beanInstance, methodArgs);
            
            // 处理返回结果
            return processMethodResult(result);
            
        } catch (Exception e) {
            logger.error("Failed to execute custom task", e);
            return TaskExecutionResult.failure("Failed to execute custom task: " + e.getMessage());
        }
    }
    
    @Override
    public void validateConfig(DagNode dagNode) throws IllegalArgumentException {
        JsonNode properties = dagNode.getProperties();
        
        if (properties == null) {
            throw new IllegalArgumentException("Custom task properties are required");
        }
        
        // 验证必填字段
        if (!properties.has("className") || properties.get("className").asText().trim().isEmpty()) {
            throw new IllegalArgumentException("Class name is required");
        }
        
        if (!properties.has("methodName") || properties.get("methodName").asText().trim().isEmpty()) {
            throw new IllegalArgumentException("Method name is required");
        }
        
        String className = properties.get("className").asText();
        String methodName = properties.get("methodName").asText();
        
        try {
            // 验证类是否存在
            Object beanInstance = getBeanInstance(className);
            
            // 验证方法是否存在
            JsonNode parameters = properties.get("parameters");
            Method method = findMethod(beanInstance.getClass(), methodName, parameters);
            
            if (method == null) {
                throw new IllegalArgumentException("Method '" + methodName + "' not found in class '" + className + "'");
            }
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid custom task configuration: " + e.getMessage());
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
    
    private Object getBeanInstance(String className) throws Exception {
        try {
            // 尝试从Spring容器中获取Bean
            return applicationContext.getBean(Class.forName(className));
        } catch (Exception e) {
            // 如果Spring容器中没有，则创建新实例
            return Class.forName(className).getDeclaredConstructor().newInstance();
        }
    }
    
    private Method findMethod(Class<?> clazz, String methodName, JsonNode parameters) {
        Method[] methods = clazz.getMethods();
        
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                // 简化处理：如果参数为空，返回无参方法
                if (parameters == null || parameters.size() == 0) {
                    if (method.getParameterCount() == 0) {
                        return method;
                    }
                } else {
                    // 如果有参数，返回第一个匹配的方法（简化处理）
                    return method;
                }
            }
        }
        
        return null;
    }
    
    private Object[] prepareMethodArguments(Method method, JsonNode parameters, TaskExecution taskExecution) {
        if (method.getParameterCount() == 0) {
            return new Object[0];
        }
        
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];
        
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> paramType = parameterTypes[i];
            
            if (TaskExecution.class.isAssignableFrom(paramType)) {
                args[i] = taskExecution;
            } else if (Map.class.isAssignableFrom(paramType)) {
                args[i] = convertParametersToMap(parameters);
            } else if (String.class.isAssignableFrom(paramType)) {
                // 简化处理：如果第一个参数是String，传入JSON字符串
                if (i == 0 && parameters != null) {
                    args[i] = parameters.toString();
                }
            } else {
                // 其他类型，尝试转换为JSON对象
                args[i] = convertParameterToObject(parameters, paramType);
            }
        }
        
        return args;
    }
    
    private Map<String, Object> convertParametersToMap(JsonNode parameters) {
        Map<String, Object> map = new HashMap<>();
        if (parameters != null) {
            parameters.fields().forEachRemaining(field -> {
                map.put(field.getKey(), convertJsonNodeToObject(field.getValue()));
            });
        }
        return map;
    }
    
    private Object convertJsonNodeToObject(JsonNode node) {
        if (node.isTextual()) {
            return node.asText();
        } else if (node.isNumber()) {
            return node.asDouble();
        } else if (node.isBoolean()) {
            return node.asBoolean();
        } else if (node.isArray()) {
            // 简化处理
            return node.toString();
        } else if (node.isObject()) {
            return convertParametersToMap(node);
        } else {
            return node.asText();
        }
    }
    
    private Object convertParameterToObject(JsonNode parameters, Class<?> targetType) {
        if (parameters == null) {
            return null;
        }
        
        // 简化处理：尝试转换为字符串
        return parameters.toString();
    }
    
    private TaskExecutionResult processMethodResult(Object result) {
        if (result instanceof TaskExecutionResult) {
            return (TaskExecutionResult) result;
        } else if (result instanceof Boolean) {
            boolean success = (Boolean) result;
            return success ? TaskExecutionResult.success(null) : TaskExecutionResult.failure("Custom task returned false");
        } else if (result instanceof String) {
            String resultStr = (String) result;
            return TaskExecutionResult.success(Map.of("result", resultStr));
        } else if (result instanceof Map) {
            return TaskExecutionResult.success((Map<String, Object>) result);
        } else if (result != null) {
            return TaskExecutionResult.success(Map.of("result", result.toString()));
        } else {
            return TaskExecutionResult.success(null);
        }
    }
}