package com.workflow.engine.executor;

import com.fasterxml.jackson.databind.JsonNode;
import com.workflow.engine.dto.DagNode;
import com.workflow.engine.entity.TaskExecution;
import com.workflow.engine.exception.WorkflowValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

/**
 * SQL任务执行器
 * 负责执行SQL查询类型的任务
 * 
 * @author workflow-engine
 * @since 1.0.0
 */
@Component
public class SqlTaskExecutor implements TaskExecutor {

    private static final Logger logger = LoggerFactory.getLogger(SqlTaskExecutor.class);

    private static final String TASK_TYPE = "SQL";
    private static final int DEFAULT_TIMEOUT_SECONDS = 300; // 5分钟

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String getTaskType() {
        return TASK_TYPE;
    }

    @Override
    public TaskExecutionResult execute(TaskExecution taskExecution, DagNode dagNode) throws Exception {
        logger.info("开始执行SQL任务: {} - {}", taskExecution.getTaskId(), taskExecution.getTaskName());

        try {
            // 获取任务配置
            JsonNode config = taskExecution.getTaskConfig();
            if (config == null) {
                return TaskExecutionResult.failure("任务配置不能为空");
            }

            // 解析配置参数
            String sql = config.get("sql").asText();
            JsonNode parametersNode = config.get("parameters");
            String operation = config.get("operation").asText("SELECT").toUpperCase();

            logger.debug("执行SQL: {}", sql);

            long startTime = System.currentTimeMillis();

            // 根据操作类型执行不同的SQL
            Object result;
            int affectedRows = 0;

            switch (operation) {
                case "SELECT":
                    result = executeQuery(sql, parametersNode);
                    break;
                case "INSERT":
                case "UPDATE":
                case "DELETE":
                    affectedRows = executeUpdate(sql, parametersNode);
                    result = Map.of(
                        "affectedRows", affectedRows,
                        "operation", operation
                    );
                    break;
                case "DDL":
                    executeDdl(sql);
                    result = Map.of(
                        "operation", operation,
                        "message", "DDL操作执行成功"
                    );
                    break;
                default:
                    return TaskExecutionResult.failure("不支持的SQL操作类型: " + operation);
            }

            long duration = System.currentTimeMillis() - startTime;

            logger.info("SQL任务执行完成: {} - 操作: {} - 耗时: {}ms", 
                       taskExecution.getTaskId(), operation, duration);

            // 构建执行结果
            Map<String, Object> executionResult = new HashMap<>();
            executionResult.put("result", result);
            executionResult.put("duration", duration);
            executionResult.put("operation", operation);
            if (!operation.equals("SELECT")) {
                executionResult.put("affectedRows", affectedRows);
            }

            return TaskExecutionResult.success(executionResult);

        } catch (Exception e) {
            logger.error("SQL任务执行失败: {} - {}", taskExecution.getTaskId(), e.getMessage());
            return TaskExecutionResult.failure("SQL执行失败: " + e.getMessage());
        }
    }

    /**
     * 执行查询操作
     */
    private List<Map<String, Object>> executeQuery(String sql, JsonNode parametersNode) {
        NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        if (parametersNode != null && parametersNode.isObject()) {
            parametersNode.fields().forEachRemaining(field -> {
                parameters.addValue(field.getKey(), convertParameterValue(field.getValue()));
            });
        }

        return namedTemplate.queryForList(sql, parameters);
    }

    /**
     * 执行更新操作
     */
    private int executeUpdate(String sql, JsonNode parametersNode) {
        NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        if (parametersNode != null && parametersNode.isObject()) {
            parametersNode.fields().forEachRemaining(field -> {
                parameters.addValue(field.getKey(), convertParameterValue(field.getValue()));
            });
        }

        return namedTemplate.update(sql, parameters);
    }

    /**
     * 执行DDL操作
     */
    private void executeDdl(String sql) {
        jdbcTemplate.execute(sql);
    }

    /**
     * 转换参数值
     */
    private Object convertParameterValue(JsonNode valueNode) {
        if (valueNode.isTextual()) {
            return valueNode.asText();
        } else if (valueNode.isNumber()) {
            if (valueNode.isInt()) {
                return valueNode.asInt();
            } else if (valueNode.isLong()) {
                return valueNode.asLong();
            } else if (valueNode.isDouble()) {
                return valueNode.asDouble();
            }
        } else if (valueNode.isBoolean()) {
            return valueNode.asBoolean();
        } else if (valueNode.isNull()) {
            return null;
        }
        return valueNode.toString();
    }

    @Override
    public void validateConfig(DagNode dagNode) throws IllegalArgumentException {
        Map<String, Object> properties = dagNode.getProperties();
        if (properties == null) {
            throw new IllegalArgumentException("SQL任务配置不能为空");
        }

        // 验证SQL语句
        String sql = (String) properties.get("sql");
        if (sql == null || sql.trim().isEmpty()) {
            throw new IllegalArgumentException("SQL语句不能为空");
        }

        // 验证SQL类型
        String operation = (String) properties.getOrDefault("operation", "SELECT");
        if (!Arrays.asList("SELECT", "INSERT", "UPDATE", "DELETE", "DDL").contains(operation.toUpperCase())) {
            throw new IllegalArgumentException("不支持的SQL操作类型: " + operation);
        }

        // 验证SQL语句基本格式
        String upperSql = sql.trim().toUpperCase();
        if (operation.equals("SELECT") && !upperSql.startsWith("SELECT")) {
            throw new IllegalArgumentException("SELECT操作必须以SELECT开头");
        } else if (operation.equals("INSERT") && !upperSql.startsWith("INSERT")) {
            throw new IllegalArgumentException("INSERT操作必须以INSERT开头");
        } else if (operation.equals("UPDATE") && !upperSql.startsWith("UPDATE")) {
            throw new IllegalArgumentException("UPDATE操作必须以UPDATE开头");
        } else if (operation.equals("DELETE") && !upperSql.startsWith("DELETE")) {
            throw new IllegalArgumentException("DELETE操作必须以DELETE开头");
        }

        logger.debug("SQL任务配置验证通过: {}", dagNode.getId());
    }

    @Override
    public Integer getTimeoutSeconds(DagNode dagNode) {
        Map<String, Object> properties = dagNode.getProperties();
        if (properties != null && properties.containsKey("timeout")) {
            Object timeoutValue = properties.get("timeout");
            if (timeoutValue instanceof Integer) {
                return (Integer) timeoutValue;
            } else if (timeoutValue instanceof String) {
                try {
                    return Integer.parseInt((String) timeoutValue);
                } catch (NumberFormatException e) {
                    logger.warn("无效的超时时间配置: {}, 使用默认值", timeoutValue);
                }
            }
        }
        return DEFAULT_TIMEOUT_SECONDS;
    }
}