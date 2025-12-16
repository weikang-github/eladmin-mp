package me.zhengjie.modules.workflow.engine.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 工作流节点
 * @author workflow-engine
 */
@Getter
@Setter
public class Node {
    private String id;
    private String type;
    private String name;
    private Map<String, Object> config;
    private Map<String, Object> position;
    private Map<String, Object> data;
}
