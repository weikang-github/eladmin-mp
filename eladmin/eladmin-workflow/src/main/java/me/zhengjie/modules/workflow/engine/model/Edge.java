package me.zhengjie.modules.workflow.engine.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 工作流边
 * @author workflow-engine
 */
@Getter
@Setter
public class Edge {
    private String id;
    private String type;
    private String source;
    private String target;
    private Map<String, Object> data;
}
