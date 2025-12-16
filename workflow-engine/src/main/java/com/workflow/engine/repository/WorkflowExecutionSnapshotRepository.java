package com.workflow.engine.repository;

import com.workflow.engine.entity.WorkflowExecutionSnapshot;
import com.workflow.engine.entity.WorkflowExecutionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowExecutionSnapshotRepository extends JpaRepository<WorkflowExecutionSnapshot, Long> {
    
    /**
     * 根据工作流执行ID查找所有快照
     */
    List<WorkflowExecutionSnapshot> findByWorkflowExecutionIdOrderByCreatedAtDesc(Long workflowExecutionId);
    
    /**
     * 根据工作流执行ID和状态查找快照
     */
    List<WorkflowExecutionSnapshot> findByWorkflowExecutionIdAndStatusOrderByCreatedAtDesc(
            Long workflowExecutionId, WorkflowExecutionStatus status);
    
    /**
     * 获取最新的快照
     */
    Optional<WorkflowExecutionSnapshot> findFirstByWorkflowExecutionIdOrderByCreatedAtDesc(Long workflowExecutionId);
    
    /**
     * 获取指定时间之前创建的快照
     */
    List<WorkflowExecutionSnapshot> findByCreatedAtBefore(LocalDateTime date);
    
    /**
     * 根据工作流定义ID查找快照
     */
    List<WorkflowExecutionSnapshot> findByWorkflowDefinitionIdOrderByCreatedAtDesc(Long workflowDefinitionId);
    
    /**
     * 统计指定工作流执行ID的快照数量
     */
    long countByWorkflowExecutionId(Long workflowExecutionId);
    
    /**
     * 根据状态查找过期快照
     */
    @Query("SELECT s FROM WorkflowExecutionSnapshot s WHERE s.status = :status AND s.createdAt < :cutoffDate")
    List<WorkflowExecutionSnapshot> findExpiredSnapshots(
            @Param("status") WorkflowExecutionStatus status, 
            @Param("cutoffDate") LocalDateTime cutoffDate);
    
    /**
     * 删除指定工作流执行ID的所有快照
     */
    void deleteByWorkflowExecutionId(Long workflowExecutionId);
    
    /**
     * 删除指定工作流定义ID的所有快照
     */
    void deleteByWorkflowDefinitionId(Long workflowDefinitionId);
}