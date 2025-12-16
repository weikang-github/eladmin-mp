package me.zhengjie.modules.workflow.engine.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 工作流程图定义
 * @author workflow-engine
 */
@Getter
@Setter
public class WorkflowGraph {
    private List<Node> nodes;
    private List<Edge> edges;
    private Map<String, Object> data;
}
