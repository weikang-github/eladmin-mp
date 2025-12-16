package com.workflow.engine.validation;

import com.workflow.engine.dto.WorkflowDefinitionDTO;
import com.workflow.engine.exception.WorkflowValidationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 工作流定义验证器
 */
@Component
public class WorkflowDefinitionValidator {
    
    /**
     * 验证工作流定义
     */
    public void validate(WorkflowDefinitionDTO dto) {
        if (dto == null) {
            throw new WorkflowValidationException("工作流定义不能为空");
        }
        
        // 验证基本信息
        validateBasicInfo(dto);
        
        // 验证定义数据
        if (dto.getDefinitionData() == null) {
            throw new WorkflowValidationException("工作流定义数据不能为空");
        }
        
        validateDefinitionData(dto.getDefinitionData());
    }
    
    /**
     * 验证基本信息
     */
    private void validateBasicInfo(WorkflowDefinitionDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new WorkflowValidationException("工作流名称不能为空");
        }
        
        if (dto.getName().length() > 255) {
            throw new WorkflowValidationException("工作流名称长度不能超过255个字符");
        }
        
        if (dto.getDescription() != null && dto.getDescription().length() > 1000) {
            throw new WorkflowValidationException("工作流描述长度不能超过1000个字符");
        }
    }
    
    /**
     * 验证定义数据
     */
    private void validateDefinitionData(WorkflowDefinitionDTO.WorkflowDefinitionData data) {
        if (data.getNodes() == null || data.getNodes().isEmpty()) {
            throw new WorkflowValidationException("工作流节点不能为空");
        }
        
        if (data.getEdges() == null || data.getEdges().isEmpty()) {
            throw new WorkflowValidationException("工作流连接不能为空");
        }
        
        // 验证节点
        validateNodes(data.getNodes());
        
        // 验证连接
        validateEdges(data.getEdges());
        
        // 验证流程结构
        validateWorkflowStructure(data);
    }
    
    /**
     * 验证节点
     */
    private void validateNodes(java.util.List<WorkflowDefinitionDTO.Node> nodes) {
        Map<String, WorkflowDefinitionDTO.Node> nodeMap = new HashMap<>();
        
        for (WorkflowDefinitionDTO.Node node : nodes) {
            if (node.getId() == null || node.getId().trim().isEmpty()) {
                throw new WorkflowValidationException("节点ID不能为空");
            }
            
            if (node.getType() == null || node.getType().trim().isEmpty()) {
                throw new WorkflowValidationException("节点类型不能为空");
            }
            
            if (node.getName() == null || node.getName().trim().isEmpty()) {
                throw new WorkflowValidationException("节点名称不能为空");
            }
            
            // 验证节点ID唯一性
            if (nodeMap.containsKey(node.getId())) {
                throw new WorkflowValidationException("节点ID重复: " + node.getId());
            }
            nodeMap.put(node.getId(), node);
            
            // 验证任务节点配置
            if ("TASK".equals(node.getType())) {
                validateTaskNode(node);
            }
        }
        
        // 验证必须有开始和结束节点
        boolean hasStartNode = nodes.stream().anyMatch(n -> "START".equals(n.getType()));
        boolean hasEndNode = nodes.stream().anyMatch(n -> "END".equals(n.getType()));
        
        if (!hasStartNode) {
            throw new WorkflowValidationException("工作流必须包含开始节点");
        }
        
        if (!hasEndNode) {
            throw new WorkflowValidationException("工作流必须包含结束节点");
        }
    }
    
    /**
     * 验证任务节点
     */
    private void validateTaskNode(WorkflowDefinitionDTO.Node node) {
        if (node.getTaskConfig() == null) {
            throw new WorkflowValidationException("任务节点配置不能为空: " + node.getId());
        }
        
        String taskType = (String) node.getTaskConfig().get("taskType");
        if (taskType == null || taskType.trim().isEmpty()) {
            throw new WorkflowValidationException("任务类型不能为空: " + node.getId());
        }
        
        // 验证特定任务类型的配置
        switch (taskType) {
            case "HTTP":
                validateHttpTaskConfig(node);
                break;
            case "SQL":
                validateSqlTaskConfig(node);
                break;
            case "SCRIPT":
                validateScriptTaskConfig(node);
                break;
            case "EMAIL":
                validateEmailTaskConfig(node);
                break;
            case "CUSTOM":
                validateCustomTaskConfig(node);
                break;
            default:
                throw new WorkflowValidationException("不支持的任务类型: " + taskType);
        }
    }
    
    /**
     * 验证HTTP任务配置
     */
    private void validateHttpTaskConfig(WorkflowDefinitionDTO.Node node) {
        Map<String, Object> config = node.getTaskConfig();
        
        String url = (String) config.get("url");
        if (url == null || url.trim().isEmpty()) {
            throw new WorkflowValidationException("HTTP任务URL不能为空: " + node.getId());
        }
        
        String method = (String) config.get("method");
        if (method == null || method.trim().isEmpty()) {
            throw new WorkflowValidationException("HTTP任务方法不能为空: " + node.getId());
        }
        
        // 验证HTTP方法
        if (!Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH").contains(method.toUpperCase())) {
            throw new WorkflowValidationException("不支持的HTTP方法: " + method);
        }
        
        // 验证超时时间
        Integer timeout = (Integer) config.get("timeoutSeconds");
        if (timeout != null && timeout <= 0) {
            throw new WorkflowValidationException("HTTP任务超时时间必须大于0: " + node.getId());
        }
    }
    
    /**
     * 验证SQL任务配置
     */
    private void validateSqlTaskConfig(WorkflowDefinitionDTO.Node node) {
        Map<String, Object> config = node.getTaskConfig();
        
        String sql = (String) config.get("sql");
        if (sql == null || sql.trim().isEmpty()) {
            throw new WorkflowValidationException("SQL任务SQL语句不能为空: " + node.getId());
        }
        
        // 验证SQL类型
        String sqlType = (String) config.get("sqlType");
        if (sqlType != null && !Arrays.asList("SELECT", "INSERT", "UPDATE", "DELETE").contains(sqlType.toUpperCase())) {
            throw new WorkflowValidationException("不支持的SQL类型: " + sqlType);
        }
    }
    
    /**
     * 验证脚本任务配置
     */
    private void validateScriptTaskConfig(WorkflowDefinitionDTO.Node node) {
        Map<String, Object> config = node.getTaskConfig();
        
        String script = (String) config.get("script");
        if (script == null || script.trim().isEmpty()) {
            throw new WorkflowValidationException("脚本任务脚本内容不能为空: " + node.getId());
        }
        
        String scriptType = (String) config.get("scriptType");
        if (scriptType == null || scriptType.trim().isEmpty()) {
            throw new WorkflowValidationException("脚本任务类型不能为空: " + node.getId());
        }
        
        // 验证脚本类型
        if (!Arrays.asList("SHELL", "PYTHON", "JAVASCRIPT", "GROOVY").contains(scriptType.toUpperCase())) {
            throw new WorkflowValidationException("不支持的脚本类型: " + scriptType);
        }
    }
    
    /**
     * 验证邮件任务配置
     */
    private void validateEmailTaskConfig(WorkflowDefinitionDTO.Node node) {
        Map<String, Object> config = node.getTaskConfig();
        
        String to = (String) config.get("to");
        if (to == null || to.trim().isEmpty()) {
            throw new WorkflowValidationException("邮件任务收件人不能为空: " + node.getId());
        }
        
        String subject = (String) config.get("subject");
        if (subject == null || subject.trim().isEmpty()) {
            throw new WorkflowValidationException("邮件任务主题不能为空: " + node.getId());
        }
        
        String content = (String) config.get("content");
        if (content == null || content.trim().isEmpty()) {
            throw new WorkflowValidationException("邮件任务内容不能为空: " + node.getId());
        }
    }
    
    /**
     * 验证自定义任务配置
     */
    private void validateCustomTaskConfig(WorkflowDefinitionDTO.Node node) {
        Map<String, Object> config = node.getTaskConfig();
        
        String className = (String) config.get("className");
        if (className == null || className.trim().isEmpty()) {
            throw new WorkflowValidationException("自定义任务类名不能为空: " + node.getId());
        }
        
        String methodName = (String) config.get("methodName");
        if (methodName == null || methodName.trim().isEmpty()) {
            throw new WorkflowValidationException("自定义任务方法名不能为空: " + node.getId());
        }
    }
    
    /**
     * 验证连接
     */
    private void validateEdges(java.util.List<WorkflowDefinitionDTO.Edge> edges) {
        for (WorkflowDefinitionDTO.Edge edge : edges) {
            if (edge.getId() == null || edge.getId().trim().isEmpty()) {
                throw new WorkflowValidationException("连接ID不能为空");
            }
            
            if (edge.getSource() == null || edge.getSource().trim().isEmpty()) {
                throw new WorkflowValidationException("连接源节点不能为空: " + edge.getId());
            }
            
            if (edge.getTarget() == null || edge.getTarget().trim().isEmpty()) {
                throw new WorkflowValidationException("连接目标节点不能为空: " + edge.getId());
            }
        }
    }
    
    /**
     * 验证工作流结构
     */
    private void validateWorkflowStructure(WorkflowDefinitionDTO.WorkflowDefinitionData data) {
        // 这里可以添加更复杂的结构验证逻辑
        // 例如：验证流程是否连通、是否存在循环依赖等
        
        // 简化验证：确保所有连接的节点都存在
        java.util.Set<String> nodeIds = data.getNodes().stream()
                .map(WorkflowDefinitionDTO.Node::getId)
                .collect(java.util.stream.Collectors.toSet());
        
        for (WorkflowDefinitionDTO.Edge edge : data.getEdges()) {
            if (!nodeIds.contains(edge.getSource())) {
                throw new WorkflowValidationException("连接源节点不存在: " + edge.getSource());
            }
            
            if (!nodeIds.contains(edge.getTarget())) {
                throw new WorkflowValidationException("连接目标节点不存在: " + edge.getTarget());
            }
        }
    }
}