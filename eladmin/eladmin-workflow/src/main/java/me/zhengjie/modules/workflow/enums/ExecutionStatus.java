package me.zhengjie.modules.workflow.enums;

/**
 * 执行状态枚举
 * @author workflow-engine
 */
public enum ExecutionStatus {
    
    /** 待执行 */
    PENDING("PENDING"),
    
    /** 执行中 */
    RUNNING("RUNNING"),
    
    /** 已完成 */
    COMPLETED("COMPLETED"),
    
    /** 执行失败 */
    FAILED("FAILED"),
    
    /** 已暂停 */
    PAUSED("PAUSED"),
    
    /** 已取消 */
    CANCELLED("CANCELLED");
    
    private final String value;
    
    ExecutionStatus(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
