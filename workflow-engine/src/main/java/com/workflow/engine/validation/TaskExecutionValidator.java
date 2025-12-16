package com.workflow.engine.validation;

import com.workflow.engine.entity.TaskExecution;
import com.workflow.engine.exception.WorkflowValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 任务执行参数验证器
 */
@Component
public class TaskExecutionValidator {
    
    private static final Logger logger = LoggerFactory.getLogger(TaskExecutionValidator.class);
    
    /**
     * 验证任务执行参数
     */
    public void validateTaskExecution(TaskExecution taskExecution) {
        if (taskExecution == null) {
            throw new WorkflowValidationException("任务执行记录不能为空");
        }
        
        // 验证基本字段
        if (taskExecution.getWorkflowExecutionId() == null || taskExecution.getWorkflowExecutionId() <= 0) {
            throw new WorkflowValidationException("工作流执行ID不能为空且必须大于0");
        }
        
        if (taskExecution.getTaskId() == null || taskExecution.getTaskId().trim().isEmpty()) {
            throw new WorkflowValidationException("任务ID不能为空");
        }
        
        if (taskExecution.getTaskId().length() > 100) {
            throw new WorkflowValidationException("任务ID长度不能超过100个字符");
        }
        
        if (taskExecution.getTaskName() == null || taskExecution.getTaskName().trim().isEmpty()) {
            throw new WorkflowValidationException("任务名称不能为空");
        }
        
        if (taskExecution.getTaskName().length() > 255) {
            throw new WorkflowValidationException("任务名称长度不能超过255个字符");
        }
        
        if (taskExecution.getTaskType() == null || taskExecution.getTaskType().trim().isEmpty()) {
            throw new WorkflowValidationException("任务类型不能为空");
        }
        
        if (taskExecution.getTaskType().length() > 50) {
            throw new WorkflowValidationException("任务类型长度不能超过50个字符");
        }
        
        // 验证状态
        if (taskExecution.getStatus() == null) {
            throw new WorkflowValidationException("任务状态不能为空");
        }
        
        // 验证重试配置
        if (taskExecution.getRetryCount() < 0) {
            throw new WorkflowValidationException("重试次数不能为负数");
        }
        
        if (taskExecution.getMaxRetries() < 0) {
            throw new WorkflowValidationException("最大重试次数不能为负数");
        }
        
        if (taskExecution.getRetryCount() > taskExecution.getMaxRetries()) {
            throw new WorkflowValidationException("当前重试次数不能超过最大重试次数");
        }
        
        // 验证任务配置
        if (taskExecution.getTaskConfig() != null) {
            validateTaskConfig(taskExecution.getTaskConfig());
        }
        
        logger.debug("Validated task execution: {} - {}", taskExecution.getTaskId(), taskExecution.getTaskName());
    }
    
    /**
     * 验证任务配置
     */
    public void validateTaskConfig(Map<String, Object> taskConfig) {
        if (taskConfig == null) {
            return;
        }
        
        // 验证配置大小限制（防止配置过大）
        if (taskConfig.size() > 100) {
            throw new WorkflowValidationException("任务配置项数量不能超过100个");
        }
        
        // 验证键名格式
        for (Map.Entry<String, Object> entry : taskConfig.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            if (key == null || key.trim().isEmpty()) {
                throw new WorkflowValidationException("任务配置键不能为空");
            }
            
            if (key.length() > 100) {
                throw new WorkflowValidationException("任务配置键长度不能超过100个字符: " + key);
            }
            
            if (!key.matches("^[a-zA-Z0-9_.-]+$")) {
                throw new WorkflowValidationException("任务配置键只能包含字母、数字、下划线、点和连字符: " + key);
            }
            
            // 验证值类型
            if (value != null && !isValidConfigValue(value)) {
                throw new WorkflowValidationException("不支持的配置值类型: " + value.getClass().getSimpleName() + " for key: " + key);
            }
        }
        
        logger.debug("Validated {} task configuration items", taskConfig.size());
    }
    
    /**
     * 验证任务输入参数
     */
    public void validateInputParameters(Map<String, Object> inputParameters) {
        if (inputParameters == null) {
            return;
        }
        
        // 验证参数数量限制
        if (inputParameters.size() > 1000) {
            throw new WorkflowValidationException("任务输入参数数量不能超过1000个");
        }
        
        // 验证参数键值
        for (Map.Entry<String, Object> entry : inputParameters.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            if (key == null || key.trim().isEmpty()) {
                throw new WorkflowValidationException("任务输入参数键不能为空");
            }
            
            if (key.length() > 200) {
                throw new WorkflowValidationException("任务输入参数键长度不能超过200个字符: " + key);
            }
            
            if (!key.matches("^[a-zA-Z0-9_.-]+$")) {
                throw new WorkflowValidationException("任务输入参数键只能包含字母、数字、下划线、点和连字符: " + key);
            }
            
            // 验证值大小（防止参数过大）
            if (value != null && value.toString().length() > 10000) {
                throw new WorkflowValidationException("任务输入参数值过大，不能超过10000个字符: " + key);
            }
        }
        
        logger.debug("Validated {} task input parameters", inputParameters.size());
    }
    
    /**
     * 验证任务输出数据
     */
    public void validateOutputData(Object outputData) {
        if (outputData == null) {
            return;
        }
        
        // 验证输出数据大小（防止输出过大）
        String outputString = outputData.toString();
        if (outputString.length() > 50000) { // 50KB限制
            throw new WorkflowValidationException("任务输出数据过大，不能超过50KB");
        }
        
        logger.debug("Validated task output data with size: {} bytes", outputString.length());
    }
    
    /**
     * 验证配置值类型
     */
    private boolean isValidConfigValue(Object value) {
        if (value == null) {
            return true;
        }
        
        Class<?> clazz = value.getClass();
        
        // 支持的基本类型
        return clazz == String.class ||
               clazz == Integer.class ||
               clazz == Long.class ||
               clazz == Double.class ||
               clazz == Float.class ||
               clazz == Boolean.class ||
               clazz == java.util.Map.class ||
               clazz == java.util.List.class ||
               clazz.isArray() ||
               Number.class.isAssignableFrom(clazz);
    }
}