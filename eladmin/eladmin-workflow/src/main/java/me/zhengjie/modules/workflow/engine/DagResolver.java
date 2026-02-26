package me.zhengjie.modules.workflow.engine;

import com.alibaba.fastjson2.JSON;
import me.zhengjie.modules.workflow.engine.model.Edge;
import me.zhengjie.modules.workflow.engine.model.Node;
import me.zhengjie.modules.workflow.engine.model.WorkflowGraph;

import java.util.*;

/**
 * DAG依赖解析器，用于拓扑排序
 * @author workflow-engine
 */
public class DagResolver {

    /**
     * 对工作流进行拓扑排序
     * @param workflowGraph 工作流程图
     * @return 拓扑排序后的节点列表
     */
    public List<Node> topologicalSort(WorkflowGraph workflowGraph) {
        // 构建邻接表和入度表
        Map<String, List<Node>> adjacencyList = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Map<String, Node> nodeMap = new HashMap<>();

        // 初始化节点映射
        for (Node node : workflowGraph.getNodes()) {
            nodeMap.put(node.getId(), node);
            adjacencyList.put(node.getId(), new ArrayList<>());
            inDegree.put(node.getId(), 0);
        }

        // 构建邻接表和计算入度
        for (Edge edge : workflowGraph.getEdges()) {
            String sourceId = edge.getSource();
            String targetId = edge.getTarget();

            Node sourceNode = nodeMap.get(sourceId);
            Node targetNode = nodeMap.get(targetId);

            if (sourceNode != null && targetNode != null) {
                adjacencyList.get(sourceId).add(targetNode);
                inDegree.put(targetId, inDegree.get(targetId) + 1);
            }
        }

        // 执行拓扑排序
        Queue<Node> queue = new LinkedList<>();
        List<Node> result = new ArrayList<>();

        // 将所有入度为0的节点加入队列
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(nodeMap.get(entry.getKey()));
            }
        }

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            result.add(currentNode);

            // 减少邻接节点的入度
            for (Node neighbor : adjacencyList.get(currentNode.getId())) {
                int newInDegree = inDegree.get(neighbor.getId()) - 1;
                inDegree.put(neighbor.getId(), newInDegree);
                if (newInDegree == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // 检查是否有环
        if (result.size() != workflowGraph.getNodes().size()) {
            throw new IllegalArgumentException("Workflow graph contains cycles");
        }

        return result;
    }

    /**
     * 检查工作流是否有孤立节点
     * @param workflowGraph 工作流程图
     * @return 是否有孤立节点
     */
    public boolean hasIsolatedNodes(WorkflowGraph workflowGraph) {
        Set<String> nodeIds = new HashSet<>();
        Set<String> connectedNodeIds = new HashSet<>();

        // 获取所有节点ID
        for (Node node : workflowGraph.getNodes()) {
            nodeIds.add(node.getId());
        }

        // 获取所有连接的节点ID
        for (Edge edge : workflowGraph.getEdges()) {
            connectedNodeIds.add(edge.getSource());
            connectedNodeIds.add(edge.getTarget());
        }

        // 检查是否有节点不在连接的节点集合中
        for (String nodeId : nodeIds) {
            if (!connectedNodeIds.contains(nodeId)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检查工作流是否有环
     * @param workflowGraph 工作流程图
     * @return 是否有环
     */
    public boolean hasCycles(WorkflowGraph workflowGraph) {
        try {
            topologicalSort(workflowGraph);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    /**
     * 从JSON字符串解析工作流程图
     * @param jsonString JSON字符串
     * @return 工作流程图
     */
    public WorkflowGraph parseFromJson(String jsonString) {
        return JSON.parseObject(jsonString, WorkflowGraph.class);
    }
}
