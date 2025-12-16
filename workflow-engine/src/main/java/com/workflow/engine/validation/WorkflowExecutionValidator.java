package com.workflow.engine.validation;

import com.workflow.engine.entity.WorkflowExecution;
import com.workflow.engine.exception.WorkflowValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * 工作流执行参数验证器
 */
@Component
public class WorkflowExecutionValidator {
    
    private static final Logger logger = LoggerFactory.getLogger(WorkflowExecutionValidator.class);
    
    /**
     * 验证工作流执行参数
     */
    public void validateExecutionParameters(Long workflowDefinitionId, Map<String, Object> inputParameters) {
        if (workflowDefinitionId == null || workflowDefinitionId <= 0) {
            throw new WorkflowValidationException("工作流定义ID不能为空且必须大于0");
        }
        
        if (inputParameters != null) {
            // 验证输入参数格式
            for (Map.Entry<String, Object> entry : inputParameters.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                
                if (key == null || key.trim().isEmpty()) {
                    throw new WorkflowValidationException("输入参数键不能为空");
                }
                
                if (key.length() > 100) {
                    throw new WorkflowValidationException("输入参数键长度不能超过100个字符: " + key);
                }
                
                // 验证参数值类型
                if (value != null && !isValidParameterType(value)) {
                    throw new WorkflowValidationException("不支持的参数值类型: " + value.getClass().getSimpleName() + " for key: " + key);
                }
            }
            
            logger.debug("Validated {} input parameters for workflow execution", inputParameters.size());
        }
    }
    
    /**
     * 验证执行状态转换
     */
    public void validateStatusTransition(WorkflowExecution currentExecution, WorkflowExecution.ExecutionStatus newStatus) {
        if (currentExecution == null || newStatus == null) {
            throw new WorkflowValidationException("当前执行状态和新状态都不能为空");
        }
        
        WorkflowExecution.ExecutionStatus currentStatus = currentExecution.getStatus();
        
        // 验证状态转换的合法性
        switch (currentStatus) {
            case PENDING:
                if (newStatus != WorkflowExecution.ExecutionStatus.RUNNING && 
                    newStatus != WorkflowExecution.ExecutionStatus.FAILED &&
                    newStatus != WorkflowExecution.ExecutionStatus.CANCELLED) {
                    throw new WorkflowValidationException("状态转换不合法: " + currentStatus + " -> " + newStatus);
                }
                break;
                
            case RUNNING:
                if (newStatus != WorkflowExecution.ExecutionStatus.COMPLETED && 
                    newStatus != WorkflowExecution.ExecutionStatus.FAILED &&
                    newStatus != WorkflowExecution.ExecutionStatus.CANCELLED) {
                    throw new WorkflowValidationException("状态转换不合法: " + currentStatus + " -> " + newStatus);
                }
                break;
                
            case COMPLETED:
            case FAILED:
            case CANCELLED:
                throw new WorkflowValidationException("执行已完成，不能转换状态: " + currentStatus);
                
            default:
                throw new WorkflowValidationException("未知的状态: " + currentStatus);
        }
        
        logger.debug("Validated status transition: {} -> {}", currentStatus, newStatus);
    }
    
    /**
     * 验证执行上下文数据
     */
    public void validateContextData(Map<String, Object> contextData) {
        if (contextData == null) {
            return;
        }
        
        // 验证上下文数据大小限制（防止内存溢出）
        int estimatedSize = estimateContextDataSize(contextData);
        if (estimatedSize > 10 * 1024 * 1024) { // 10MB限制
            throw new WorkflowValidationException("执行上下文数据过大，不能超过10MB");
        }
        
        // 验证键名格式
        for (String key : contextData.keySet()) {
            if (key == null || key.trim().isEmpty()) {
                throw new WorkflowValidationException("上下文数据键不能为空");
            }
            
            if (key.length() > 200) {
                throw new WorkflowValidationException("上下文数据键长度不能超过200个字符: " + key);
            }
            
            if (!key.matches("^[a-zA-Z0-9_.-]+$")) {
                throw new WorkflowValidationException("上下文数据键只能包含字母、数字、下划线、点和连字符: " + key);
            }
        }
        
        logger.debug("Validated context data with {} keys", contextData.size());
    }
    
    /**
     * 验证参数值类型
     */
    private boolean isValidParameterType(Object value) {
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
               clazz == java.util.Date.class ||
               clazz == java.time.LocalDate.class ||
               clazz == java.time.LocalDateTime.class ||
               clazz == java.util.Map.class ||
               clazz == java.util.List.class ||
               clazz.isArray() ||
               Number.class.isAssignableFrom(clazz);
    }
    
    /**
     * 估算上下文数据大小（粗略估计）
     */
    private int estimateContextDataSize(Map<String, Object> contextData) {
        int size = 0;
        for (Map.Entry<String, Object> entry : contextData.entrySet()) {
            size += entry.getKey().length() * 2; // 字符串的字符数 * 2（UTF-16）
            if (entry.getValue() != null) {
                size += entry.getValue().toString().length() * 2;
            }
        }
        return size;
    }
}