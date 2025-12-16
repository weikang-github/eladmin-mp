package com.workflow.engine.exception;

/**
 * 流程验证异常
 * 
 * @author workflow-engine
 * @since 1.0.0
 */
public class WorkflowValidationException extends RuntimeException {

    public WorkflowValidationException(String message) {
        super(message);
    }

    public WorkflowValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkflowValidationException(Throwable cause) {
        super(cause);
    }
}