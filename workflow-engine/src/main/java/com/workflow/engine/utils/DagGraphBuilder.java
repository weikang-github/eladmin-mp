package com.workflow.engine.utils;

import com.workflow.engine.dto.DagGraph;
import com.workflow.engine.dto.DagNode;
import com.workflow.engine.dto.WorkflowDefinitionDTO;
import com.workflow.engine.exception.WorkflowValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * DAG图构建器
 * 负责将流程定义转换为DAG图结构
 * 
 * @author workflow-engine
 * @since 1.0.0
 */
@Component
public class DagGraphBuilder {

    private static final Logger logger = LoggerFactory.getLogger(DagGraphBuilder.class);

    /**
     * 从流程定义构建DAG图
     */
    public DagGraph buildGraph(WorkflowDefinitionDTO definitionDTO) {
        logger.info("开始构建DAG图，流程定义ID: {}", definitionDTO.getId());
        
        DagGraph graph = new DagGraph();
        
        // 添加节点
        if (definitionDTO.getDefinitionData() != null && 
            definitionDTO.getDefinitionData().getNodes() != null) {
            for (WorkflowDefinitionDTO.Node node : definitionDTO.getDefinitionData().getNodes()) {
                DagNode dagNode = convertToDagNode(node);
                graph.addNode(dagNode);
                logger.debug("添加节点: {} - {}", node.getId(), node.getName());
            }
        }
        
        // 添加边（依赖关系）
        if (definitionDTO.getDefinitionData() != null && 
            definitionDTO.getDefinitionData().getEdges() != null) {
            for (WorkflowDefinitionDTO.Edge edge : definitionDTO.getDefinitionData().getEdges()) {
                try {
                    graph.addEdge(edge.getSource(), edge.getTarget());
                    logger.debug("添加边: {} -> {}", edge.getSource(), edge.getTarget());
                } catch (IllegalArgumentException e) {
                    throw new WorkflowValidationException("无效的边定义: " + e.getMessage());
                }
            }
        }
        
        // 验证图的有效性
        validateGraph(graph);
        
        logger.info("DAG图构建完成，节点数: {}, 边数: {}", 
                   graph.getAllNodes().size(), getEdgeCount(graph));
        
        return graph;
    }

    /**
     * 将流程定义节点转换为DAG节点
     */
    private DagNode convertToDagNode(WorkflowDefinitionDTO.Node node) {
        return new DagNode(
            node.getId(),
            node.getType(),
            node.getName(),
            node.getProperties()
        );
    }

    /**
     * 验证DAG图的有效性
     */
    private void validateGraph(DagGraph graph) {
        logger.info("开始验证DAG图");
        
        List<String> validationErrors = new ArrayList<>();
        
        // 1. 检查是否有节点
        if (graph.getAllNodes().isEmpty()) {
            validationErrors.add("流程定义中没有节点");
        }
        
        // 2. 检查是否有开始节点
        Set<String> startNodes = graph.getStartNodes();
        if (startNodes.isEmpty()) {
            validationErrors.add("流程定义中没有开始节点");
        } else if (startNodes.size() > 1) {
            validationErrors.add("流程定义中有多个开始节点: " + startNodes);
        }
        
        // 3. 检查是否有结束节点
        Set<String> endNodes = graph.getEndNodes();
        if (endNodes.isEmpty()) {
            validationErrors.add("流程定义中没有结束节点");
        }
        
        // 4. 检查是否有孤立节点
        Set<String> isolatedNodes = graph.getIsolatedNodes();
        if (!isolatedNodes.isEmpty()) {
            validationErrors.add("流程定义中存在孤立节点: " + isolatedNodes);
        }
        
        // 5. 检查是否有环
        if (graph.hasCycle()) {
            List<String> cycleNodes = graph.getCycleNodes();
            validationErrors.add("流程定义中存在环: " + cycleNodes);
        }
        
        // 6. 检查节点类型的有效性
        for (DagNode node : graph.getAllNodes()) {
            if (!isValidNodeType(node.getType())) {
                validationErrors.add("无效的节点类型: " + node.getType() + " (节点ID: " + node.getId() + ")");
            }
        }
        
        if (!validationErrors.isEmpty()) {
            String errorMessage = "DAG图验证失败: " + String.join("; ", validationErrors);
            logger.error(errorMessage);
            throw new WorkflowValidationException(errorMessage);
        }
        
        logger.info("DAG图验证通过");
    }

    /**
     * 检查节点类型是否有效
     */
    private boolean isValidNodeType(String nodeType) {
        return Arrays.asList("START", "END", "TASK", "CONDITION", "PARALLEL_GATEWAY", "SUB_WORKFLOW")
                    .contains(nodeType);
    }

    /**
     * 获取图中的边数量
     */
    private int getEdgeCount(DagGraph graph) {
        int count = 0;
        for (DagNode node : graph.getAllNodes()) {
            count += graph.getSuccessors(node.getId()).size();
        }
        return count;
    }

    /**
     * 获取流程定义中的开始节点
     */
    public DagNode getStartNode(DagGraph graph) {
        Set<String> startNodes = graph.getStartNodes();
        if (startNodes.isEmpty()) {
            return null;
        }
        return graph.getNode(startNodes.iterator().next());
    }

    /**
     * 获取流程定义中的结束节点
     */
    public Set<DagNode> getEndNodes(DagGraph graph) {
        Set<String> endNodeIds = graph.getEndNodes();
        Set<DagNode> endNodes = new HashSet<>();
        for (String nodeId : endNodeIds) {
            endNodes.add(graph.getNode(nodeId));
        }
        return endNodes;
    }

    /**
     * 获取节点的所有前置依赖节点
     */
    public Set<DagNode> getAllPredecessors(DagGraph graph, String nodeId) {
        Set<DagNode> predecessors = new HashSet<>();
        Set<String> visited = new HashSet<>();
        collectPredecessors(graph, nodeId, predecessors, visited);
        return predecessors;
    }

    /**
     * 递归收集所有前置依赖节点
     */
    private void collectPredecessors(DagGraph graph, String nodeId, Set<DagNode> predecessors, Set<String> visited) {
        if (visited.contains(nodeId)) {
            return;
        }
        visited.add(nodeId);

        Set<String> directPredecessors = graph.getPredecessors(nodeId);
        for (String predecessorId : directPredecessors) {
            predecessors.add(graph.getNode(predecessorId));
            collectPredecessors(graph, predecessorId, predecessors, visited);
        }
    }

    /**
     * 获取节点的所有后置依赖节点
     */
    public Set<DagNode> getAllSuccessors(DagGraph graph, String nodeId) {
        Set<DagNode> successors = new HashSet<>();
        Set<String> visited = new HashSet<>();
        collectSuccessors(graph, nodeId, successors, visited);
        return successors;
    }

    /**
     * 递归收集所有后置依赖节点
     */
    private void collectSuccessors(DagGraph graph, String nodeId, Set<DagNode> successors, Set<String> visited) {
        if (visited.contains(nodeId)) {
            return;
        }
        visited.add(nodeId);

        Set<String> directSuccessors = graph.getSuccessors(nodeId);
        for (String successorId : directSuccessors) {
            successors.add(graph.getNode(successorId));
            collectSuccessors(graph, successorId, successors, visited);
        }
    }
}