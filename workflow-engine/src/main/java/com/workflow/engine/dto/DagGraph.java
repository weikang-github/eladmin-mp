package com.workflow.engine.dto;

import java.util.*;

/**
 * DAG图结构定义
 * 用于表示流程定义中的节点和依赖关系
 * 
 * @author workflow-engine
 * @since 1.0.0
 */
public class DagGraph {

    private final Map<String, DagNode> nodes;
    private final Map<String, Set<String>> adjacencyList;
    private final Map<String, Set<String>> reverseAdjacencyList;

    public DagGraph() {
        this.nodes = new HashMap<>();
        this.adjacencyList = new HashMap<>();
        this.reverseAdjacencyList = new HashMap<>();
    }

    /**
     * 添加节点
     */
    public void addNode(DagNode node) {
        nodes.put(node.getId(), node);
        adjacencyList.putIfAbsent(node.getId(), new HashSet<>());
        reverseAdjacencyList.putIfAbsent(node.getId(), new HashSet<>());
    }

    /**
     * 添加边（依赖关系）
     */
    public void addEdge(String fromNodeId, String toNodeId) {
        // 检查节点是否存在
        if (!nodes.containsKey(fromNodeId) || !nodes.containsKey(toNodeId)) {
            throw new IllegalArgumentException("节点不存在: " + fromNodeId + " -> " + toNodeId);
        }

        // 添加正向边
        adjacencyList.get(fromNodeId).add(toNodeId);
        
        // 添加反向边（用于反向遍历）
        reverseAdjacencyList.get(toNodeId).add(fromNodeId);
    }

    /**
     * 获取节点
     */
    public DagNode getNode(String nodeId) {
        return nodes.get(nodeId);
    }

    /**
     * 获取所有节点
     */
    public Collection<DagNode> getAllNodes() {
        return nodes.values();
    }

    /**
     * 获取节点的直接后继节点
     */
    public Set<String> getSuccessors(String nodeId) {
        return new HashSet<>(adjacencyList.getOrDefault(nodeId, Collections.emptySet()));
    }

    /**
     * 获取节点的直接前驱节点
     */
    public Set<String> getPredecessors(String nodeId) {
        return new HashSet<>(reverseAdjacencyList.getOrDefault(nodeId, Collections.emptySet()));
    }

    /**
     * 检查是否存在环
     */
    public boolean hasCycle() {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        for (String nodeId : nodes.keySet()) {
            if (!visited.contains(nodeId)) {
                if (hasCycleUtil(nodeId, visited, recursionStack)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 深度优先搜索检测环
     */
    private boolean hasCycleUtil(String nodeId, Set<String> visited, Set<String> recursionStack) {
        visited.add(nodeId);
        recursionStack.add(nodeId);

        for (String neighbor : adjacencyList.get(nodeId)) {
            if (!visited.contains(neighbor)) {
                if (hasCycleUtil(neighbor, visited, recursionStack)) {
                    return true;
                }
            } else if (recursionStack.contains(neighbor)) {
                return true;
            }
        }

        recursionStack.remove(nodeId);
        return false;
    }

    /**
     * 获取环中的节点（如果存在）
     */
    public List<String> getCycleNodes() {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();
        List<String> cyclePath = new ArrayList<>();

        for (String nodeId : nodes.keySet()) {
            if (!visited.contains(nodeId)) {
                if (getCyclePath(nodeId, visited, recursionStack, cyclePath)) {
                    return cyclePath;
                }
            }
        }
        return Collections.emptyList();
    }

    /**
     * 获取环路径
     */
    private boolean getCyclePath(String nodeId, Set<String> visited, Set<String> recursionStack, List<String> cyclePath) {
        visited.add(nodeId);
        recursionStack.add(nodeId);
        cyclePath.add(nodeId);

        for (String neighbor : adjacencyList.get(nodeId)) {
            if (!visited.contains(neighbor)) {
                if (getCyclePath(neighbor, visited, recursionStack, cyclePath)) {
                    return true;
                }
            } else if (recursionStack.contains(neighbor)) {
                // 找到环，截取从环开始到结束的部分
                int cycleStart = cyclePath.indexOf(neighbor);
                if (cycleStart != -1) {
                    List<String> cycle = cyclePath.subList(cycleStart, cyclePath.size());
                    cyclePath.clear();
                    cyclePath.addAll(cycle);
                    cyclePath.add(neighbor); // 闭合环
                    return true;
                }
            }
        }

        recursionStack.remove(nodeId);
        cyclePath.remove(cyclePath.size() - 1);
        return false;
    }

    /**
     * 拓扑排序
     * 使用Kahn算法
     */
    public List<String> topologicalSort() {
        if (hasCycle()) {
            throw new IllegalStateException("图中存在环，无法进行拓扑排序");
        }

        Map<String, Integer> inDegree = new HashMap<>();
        for (String nodeId : nodes.keySet()) {
            inDegree.put(nodeId, 0);
        }

        // 计算每个节点的入度
        for (String nodeId : adjacencyList.keySet()) {
            for (String neighbor : adjacencyList.get(nodeId)) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        // 找到所有入度为0的节点
        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        List<String> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            // 减少相邻节点的入度
            for (String neighbor : adjacencyList.get(current)) {
                int newInDegree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, newInDegree);
                if (newInDegree == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // 检查是否所有节点都被访问（无环）
        if (result.size() != nodes.size()) {
            throw new IllegalStateException("图中存在环，无法进行拓扑排序");
        }

        return result;
    }

    /**
     * 获取并行执行分组
     * 将可以并行执行的节点分组
     */
    public List<Set<String>> getParallelGroups() {
        List<String> topologicalOrder = topologicalSort();
        Map<String, Integer> levels = new HashMap<>();

        // 计算每个节点的层级
        for (String nodeId : topologicalOrder) {
            int maxLevel = -1;
            for (String predecessor : getPredecessors(nodeId)) {
                maxLevel = Math.max(maxLevel, levels.get(predecessor));
            }
            levels.put(nodeId, maxLevel + 1);
        }

        // 按层级分组
        Map<Integer, Set<String>> levelGroups = new HashMap<>();
        for (Map.Entry<String, Integer> entry : levels.entrySet()) {
            levelGroups.computeIfAbsent(entry.getValue(), k -> new HashSet<>())
                      .add(entry.getKey());
        }

        return new ArrayList<>(levelGroups.values());
    }

    /**
     * 获取孤立节点（没有前驱也没有后继的节点）
     */
    public Set<String> getIsolatedNodes() {
        Set<String> isolated = new HashSet<>();
        for (String nodeId : nodes.keySet()) {
            if (getPredecessors(nodeId).isEmpty() && getSuccessors(nodeId).isEmpty()) {
                isolated.add(nodeId);
            }
        }
        return isolated;
    }

    /**
     * 获取开始节点（没有前驱的节点）
     */
    public Set<String> getStartNodes() {
        Set<String> startNodes = new HashSet<>();
        for (String nodeId : nodes.keySet()) {
            if (getPredecessors(nodeId).isEmpty()) {
                startNodes.add(nodeId);
            }
        }
        return startNodes;
    }

    /**
     * 获取结束节点（没有后继的节点）
     */
    public Set<String> getEndNodes() {
        Set<String> endNodes = new HashSet<>();
        for (String nodeId : nodes.keySet()) {
            if (getSuccessors(nodeId).isEmpty()) {
                endNodes.add(nodeId);
            }
        }
        return endNodes;
    }

    @Override
    public String toString() {
        return "DagGraph{" +
                "nodes=" + nodes.keySet() +
                ", edges=" + adjacencyList +
                '}';
    }
}