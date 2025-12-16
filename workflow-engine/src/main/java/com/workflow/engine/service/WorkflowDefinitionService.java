package com.workflow.engine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workflow.engine.dto.WorkflowDefinitionDTO;
import com.workflow.engine.dto.WorkflowDefinitionDTO.WorkflowDefinitionData;
import com.workflow.engine.entity.WorkflowDefinition;
import com.workflow.engine.entity.WorkflowDefinitionStatus;
import com.workflow.engine.exception.WorkflowValidationException;
import com.workflow.engine.repository.WorkflowDefinitionRepository;
import com.workflow.engine.utils.DagGraphBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WorkflowDefinitionService {
    
    private static final Logger logger = LoggerFactory.getLogger(WorkflowDefinitionService.class);
    
    @Autowired
    private WorkflowDefinitionRepository workflowDefinitionRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * 创建流程定义
     */
    @Transactional
    public WorkflowDefinition createWorkflowDefinition(WorkflowDefinitionDTO dto) {
        logger.info("Creating workflow definition: {}", dto.getName());
        
        // 验证流程定义数据
        validateWorkflowDefinition(dto);
        
        // 检查名称是否已存在
        if (workflowDefinitionRepository.existsByNameAndStatusNot(dto.getName(), WorkflowDefinitionStatus.DELETED)) {
            throw new IllegalArgumentException("Workflow definition with name '" + dto.getName() + "' already exists");
        }
        
        // 创建流程定义
        WorkflowDefinition definition = new WorkflowDefinition();
        definition.setName(dto.getName());
        definition.setDescription(dto.getDescription());
        definition.setVersion(1);
        definition.setStatus(WorkflowDefinitionStatus.ACTIVE);
        definition.setDefinitionData(dto.getDefinitionData());
        definition.setCreatedBy("system"); // TODO: 从当前用户获取
        definition.setCreatedAt(LocalDateTime.now());
        definition.setUpdatedAt(LocalDateTime.now());
        
        return workflowDefinitionRepository.save(definition);
    }
    
    /**
     * 更新流程定义
     */
    @Transactional
    public WorkflowDefinition updateWorkflowDefinition(Long id, WorkflowDefinitionDTO dto) {
        logger.info("Updating workflow definition: {}", id);
        
        // 获取现有流程定义
        WorkflowDefinition existingDefinition = workflowDefinitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Workflow definition not found: " + id));
        
        // 验证流程定义数据
        validateWorkflowDefinition(dto);
        
        // 如果状态是ACTIVE，创建新版本
        if (existingDefinition.getStatus() == WorkflowDefinitionStatus.ACTIVE) {
            // 停用旧版本
            existingDefinition.setStatus(WorkflowDefinitionStatus.INACTIVE);
            existingDefinition.setUpdatedAt(LocalDateTime.now());
            workflowDefinitionRepository.save(existingDefinition);
            
            // 创建新版本
            WorkflowDefinition newDefinition = new WorkflowDefinition();
            newDefinition.setName(dto.getName());
            newDefinition.setDescription(dto.getDescription());
            newDefinition.setVersion(existingDefinition.getVersion() + 1);
            newDefinition.setStatus(WorkflowDefinitionStatus.ACTIVE);
            newDefinition.setDefinitionData(dto.getDefinitionData());
            newDefinition.setCreatedBy("system"); // TODO: 从当前用户获取
            newDefinition.setCreatedAt(LocalDateTime.now());
            newDefinition.setUpdatedAt(LocalDateTime.now());
            
            return workflowDefinitionRepository.save(newDefinition);
        } else {
            // 直接更新
            existingDefinition.setName(dto.getName());
            existingDefinition.setDescription(dto.getDescription());
            existingDefinition.setDefinitionData(dto.getDefinitionData());
            existingDefinition.setUpdatedAt(LocalDateTime.now());
            
            return workflowDefinitionRepository.save(existingDefinition);
        }
    }
    
    /**
     * 删除流程定义
     */
    @Transactional
    public void deleteWorkflowDefinition(Long id) {
        logger.info("Deleting workflow definition: {}", id);
        
        WorkflowDefinition definition = workflowDefinitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Workflow definition not found: " + id));
        
        // 逻辑删除
        definition.setStatus(WorkflowDefinitionStatus.DELETED);
        definition.setUpdatedAt(LocalDateTime.now());
        workflowDefinitionRepository.save(definition);
    }
    
    /**
     * 获取流程定义
     */
    public WorkflowDefinition getWorkflowDefinition(Long id) {
        return workflowDefinitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Workflow definition not found: " + id));
    }
    
    /**
     * 根据名称获取最新版本的流程定义
     */
    public WorkflowDefinition getLatestWorkflowDefinition(String name) {
        return workflowDefinitionRepository.findByNameAndStatusOrderByVersionDesc(name, WorkflowDefinitionStatus.ACTIVE)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Active workflow definition not found: " + name));
    }
    
    /**
     * 分页查询流程定义
     */
    public Page<WorkflowDefinition> listWorkflowDefinitions(String name, String status, Pageable pageable) {
        if (StringUtils.hasText(name)) {
            if (StringUtils.hasText(status)) {
                return workflowDefinitionRepository.findByNameContainingAndStatusOrderByCreatedAtDesc(name, WorkflowDefinitionStatus.valueOf(status), pageable);
            } else {
                return workflowDefinitionRepository.findByNameContainingOrderByCreatedAtDesc(name, pageable);
            }
        } else {
            if (StringUtils.hasText(status)) {
                return workflowDefinitionRepository.findByStatusOrderByCreatedAtDesc(WorkflowDefinitionStatus.valueOf(status), pageable);
            } else {
                return workflowDefinitionRepository.findByStatusNotOrderByCreatedAtDesc(WorkflowDefinitionStatus.DELETED, pageable);
            }
        }
    }
    
    /**
     * 获取流程定义的所有版本
     */
    public List<WorkflowDefinition> getWorkflowDefinitionVersions(String name) {
        return workflowDefinitionRepository.findByNameOrderByVersionDesc(name);
    }
    
    /**
     * 激活流程定义版本
     */
    @Transactional
    public WorkflowDefinition activateWorkflowDefinitionVersion(Long id) {
        logger.info("Activating workflow definition version: {}", id);
        
        WorkflowDefinition definition = workflowDefinitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Workflow definition not found: " + id));
        
        // 停用同名的其他版本
        List<WorkflowDefinition> existingVersions = workflowDefinitionRepository.findByNameAndStatus(definition.getName(), WorkflowDefinitionStatus.ACTIVE);
        for (WorkflowDefinition existing : existingVersions) {
            existing.setStatus(WorkflowDefinitionStatus.INACTIVE);
            existing.setUpdatedAt(LocalDateTime.now());
            workflowDefinitionRepository.save(existing);
        }
        
        // 激活指定版本
        definition.setStatus(WorkflowDefinitionStatus.ACTIVE);
        definition.setUpdatedAt(LocalDateTime.now());
        return workflowDefinitionRepository.save(definition);
    }
    
    /**
     * 验证流程定义
     */
    private void validateWorkflowDefinition(WorkflowDefinitionDTO dto) {
        if (!StringUtils.hasText(dto.getName())) {
            throw new IllegalArgumentException("Workflow name is required");
        }
        
        if (dto.getDefinitionData() == null) {
            throw new IllegalArgumentException("Workflow definition data is required");
        }
        
        WorkflowDefinitionData data = dto.getDefinitionData();
        
        if (data.getNodes() == null || data.getNodes().isEmpty()) {
            throw new IllegalArgumentException("Workflow must have at least one node");
        }
        
        if (data.getEdges() == null || data.getEdges().isEmpty()) {
            throw new IllegalArgumentException("Workflow must have at least one edge");
        }
        
        // 验证DAG结构
        try {
            DagGraphBuilder.buildFromDefinitionData(data);
        } catch (Exception e) {
            throw new WorkflowValidationException("Invalid workflow structure: " + e.getMessage());
        }
        
        // 验证开始和结束节点
        boolean hasStartNode = data.getNodes().stream()
                .anyMatch(node -> "START".equals(node.getType()));
        boolean hasEndNode = data.getNodes().stream()
                .anyMatch(node -> "END".equals(node.getType()));
        
        if (!hasStartNode) {
            throw new IllegalArgumentException("Workflow must have a START node");
        }
        
        if (!hasEndNode) {
            throw new IllegalArgumentException("Workflow must have an END node");
        }
        
        // 验证任务节点配置
        data.getNodes().stream()
                .filter(node -> "TASK".equals(node.getType()))
                .forEach(node -> {
                    if (node.getProperties() == null || !node.getProperties().containsKey("taskType")) {
                        throw new IllegalArgumentException("Task node '" + node.getName() + "' must have taskType property");
                    }
                });
    }
    
    /**
     * 测试流程定义
     */
    public Map<String, Object> testWorkflowDefinition(WorkflowDefinitionDTO dto) {
        try {
            validateWorkflowDefinition(dto);
            
            // 构建DAG图进行验证
            DagGraphBuilder.buildFromDefinitionData(dto.getDefinitionData());
            
            return Map.of(
                    "valid", true,
                    "message", "Workflow definition is valid"
            );
        } catch (Exception e) {
            return Map.of(
                    "valid", false,
                    "message", e.getMessage()
            );
        }
    }
}