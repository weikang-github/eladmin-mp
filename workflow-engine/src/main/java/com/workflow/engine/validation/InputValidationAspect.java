package com.workflow.engine.validation;

import com.workflow.engine.exception.WorkflowValidationException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * 输入验证切面
 */
@Aspect
@Component
public class InputValidationAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(InputValidationAspect.class);
    
    @Autowired
    private WorkflowDefinitionValidator workflowDefinitionValidator;
    
    @Autowired
    private WorkflowExecutionValidator workflowExecutionValidator;
    
    @Autowired
    private TaskExecutionValidator taskExecutionValidator;
    
    /**
     * 验证工作流定义输入参数
     */
    @Before("execution(* com.workflow.engine.controller.WorkflowDefinitionController.*(..))")
    public void validateWorkflowDefinitionInput(JoinPoint joinPoint) {
        logger.debug("Validating workflow definition input for method: {}", joinPoint.getSignature().getName());
        
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof com.workflow.engine.dto.WorkflowDefinitionDTO) {
                com.workflow.engine.dto.WorkflowDefinitionDTO dto = (com.workflow.engine.dto.WorkflowDefinitionDTO) arg;
                try {
                    workflowDefinitionValidator.validate(dto);
                } catch (WorkflowValidationException e) {
                    logger.warn("Workflow definition validation failed: {}", e.getMessage());
                    throw e;
                }
            }
        }
    }
    
    /**
     * 验证ID参数
     */
    @Before("execution(* com.workflow.engine.controller.*Controller.*(..)) && args(id,..)")
    public void validateIdParameter(JoinPoint joinPoint, Long id) {
        if (id == null || id <= 0) {
            throw new WorkflowValidationException("无效的ID参数: " + id);
        }
    }
    
    /**
     * 验证执行ID参数
     */
    @Before("execution(* com.workflow.engine.controller.*Controller.startWorkflow(..))")
    public void validateStartWorkflowInput(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length >= 2) {
            Long workflowId = (Long) args[0];
            Map<String, Object> parameters = (Map<String, Object>) args[1];
            String startedBy = (String) args[2];
            
            if (workflowId == null || workflowId <= 0) {
                throw new WorkflowValidationException("无效的工作流ID: " + workflowId);
            }
            
            if (startedBy == null || startedBy.trim().isEmpty()) {
                throw new WorkflowValidationException("启动人不能为空");
            }
            
            if (startedBy.length() > 100) {
                throw new WorkflowValidationException("启动人名称长度不能超过100个字符");
            }
        }
    }
    
    /**
     * 验证工作流执行参数
     */
    @Before("execution(* com.workflow.engine.controller.WorkflowExecutionController.startWorkflow(..))")
    public void validateWorkflowExecutionInput(JoinPoint joinPoint) {
        logger.debug("Validating workflow execution input for method: {}", joinPoint.getSignature().getName());
        
        Object[] args = joinPoint.getArgs();
        if (args.length >= 2) {
            Long workflowDefinitionId = (Long) args[0];
            Map<String, Object> inputParameters = (Map<String, Object>) args[1];
            
            workflowExecutionValidator.validateExecutionParameters(workflowDefinitionId, inputParameters);
        }
    }
    
    /**
     * 验证任务执行参数
     */
    @Before("execution(* com.workflow.engine.controller.*Controller.*(..)) && args(taskExecution,..)")
    public void validateTaskExecutionInput(JoinPoint joinPoint, com.workflow.engine.entity.TaskExecution taskExecution) {
        logger.debug("Validating task execution input for method: {}", joinPoint.getSignature().getName());
        
        if (taskExecution != null) {
            taskExecutionValidator.validateTaskExecution(taskExecution);
            
            if (taskExecution.getInputParameters() != null) {
                taskExecutionValidator.validateInputParameters(taskExecution.getInputParameters());
            }
            
            if (taskExecution.getOutput() != null) {
                taskExecutionValidator.validateOutputData(taskExecution.getOutput());
            }
        }
    }
    
    /**
     * 验证状态管理参数
     */
    @Before("execution(* com.workflow.engine.controller.WorkflowStateController.*(..))")
    public void validateStateManagementInput(JoinPoint joinPoint) {
        logger.debug("Validating state management input for method: {}", joinPoint.getSignature().getName());
        
        Object[] args = joinPoint.getArgs();
        
        for (Object arg : args) {
            if (arg instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> params = (Map<String, Object>) arg;
                
                // 验证节点ID
                if (params.containsKey("nodeId")) {
                    String nodeId = (String) params.get("nodeId");
                    if (nodeId == null || nodeId.trim().isEmpty()) {
                        throw new WorkflowValidationException("节点ID不能为空");
                    }
                    if (nodeId.length() > 100) {
                        throw new WorkflowValidationException("节点ID长度不能超过100个字符");
                    }
                }
                
                // 验证上下文数据
                if (params.containsKey("contextData")) {
                    Object contextData = params.get("contextData");
                    if (contextData != null && !(contextData instanceof Map)) {
                        throw new WorkflowValidationException("上下文数据必须是Map类型");
                    }
                    
                    if (contextData != null) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> contextMap = (Map<String, Object>) contextData;
                        workflowExecutionValidator.validateContextData(contextMap);
                    }
                }
            }
        }
    }
    
    /**
     * 验证监控参数
     */
    @Before("execution(* com.workflow.engine.controller.MonitoringController.*(..))")
    public void validateMonitoringInput(JoinPoint joinPoint) {
        logger.debug("Validating monitoring input for method: {}", joinPoint.getSignature().getName());
        
        Object[] args = joinPoint.getArgs();
        
        for (Object arg : args) {
            if (arg instanceof String) {
                String metricName = (String) arg;
                if (metricName != null && metricName.length() > 255) {
                    throw new WorkflowValidationException("指标名称长度不能超过255个字符");
                }
            }
            
            if (arg instanceof Integer) {
                Integer hours = (Integer) arg;
                if (hours != null && (hours < 1 || hours > 168)) {
                    throw new WorkflowValidationException("时间范围必须在1-168小时之间");
                }
            }
        }
    }
}