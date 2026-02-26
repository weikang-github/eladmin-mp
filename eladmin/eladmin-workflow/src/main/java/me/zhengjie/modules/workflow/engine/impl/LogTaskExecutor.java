package me.zhengjie.modules.workflow.engine.impl;

import me.zhengjie.modules.workflow.domain.TaskExecution;
import me.zhengjie.modules.workflow.engine.TaskExecutor;
import me.zhengjie.modules.workflow.engine.model.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志任务执行器
 * @author workflow-engine
 */
@Component
public class LogTaskExecutor implements TaskExecutor {

    private static final Logger logger = LoggerFactory.getLogger(LogTaskExecutor.class);

    @Override
    public String getTaskType() {
        return "log";
    }

    @Override
    public Map<String, Object> execute(Node node, Map<String, Object> executionContext, TaskExecution taskExecution) {
        Map<String, Object> result = new HashMap<>();

        try {
            Map<String, Object> config = node.getConfig();
            String level = (String) config.getOrDefault("level", "INFO");
            String message = (String) config.getOrDefault("message", "Default log message");

            // 根据日志级别记录日志
            switch (level.toUpperCase()) {
                case "TRACE":
                    logger.trace(message);
                    break;
                case "DEBUG":
                    logger.debug(message);
                    break;
                case "INFO":
                    logger.info(message);
                    break;
                case "WARN":
                    logger.warn(message);
                    break;
                case "ERROR":
                    logger.error(message);
                    break;
                default:
                    logger.info(message);
            }

            // 返回结果
            result.put("logLevel", level);
            result.put("logMessage", message);
            result.put("status", "SUCCESS");

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Log task execution failed: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean validate(Node node) {
        Map<String, Object> config = node.getConfig();
        // 日志任务只需要message参数
        return config != null && config.containsKey("message");
    }
}