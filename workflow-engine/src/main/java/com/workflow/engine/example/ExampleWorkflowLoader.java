package com.workflow.engine.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workflow.engine.dto.WorkflowDefinitionDTO;
import com.workflow.engine.entity.WorkflowDefinition;
import com.workflow.engine.service.WorkflowDefinitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 示例工作流定义加载器
 * 仅在开发环境中加载示例数据
 */
@Component
@Profile("dev")
public class ExampleWorkflowLoader implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(ExampleWorkflowLoader.class);
    
    @Autowired
    private WorkflowDefinitionService workflowDefinitionService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("Loading example workflow definitions...");
        
        try {
            // 创建HTTP请求工作流
            createHttpWorkflow();
            
            // 创建数据处理工作流
            createDataProcessingWorkflow();
            
            // 创建邮件通知工作流
            createEmailNotificationWorkflow();
            
            // 创建脚本执行工作流
            createScriptExecutionWorkflow();
            
            logger.info("Example workflow definitions loaded successfully");
        } catch (Exception e) {
            logger.error("Failed to load example workflow definitions", e);
        }
    }
    
    /**
     * 创建HTTP请求工作流
     */
    private void createHttpWorkflow() {
        logger.info("Creating HTTP workflow example...");
        
        WorkflowDefinitionDTO dto = new WorkflowDefinitionDTO();
        dto.setName("HTTP请求测试流程");
        dto.setDescription("测试HTTP请求任务的工作流");
        
        WorkflowDefinitionDTO.WorkflowDefinitionData data = new WorkflowDefinitionDTO.WorkflowDefinitionData();
        
        // 定义节点
        data.setNodes(List.of(
                createNode("start", "START", "开始", null),
                createNode("http_request", "TASK", "HTTP请求", Map.of(
                        "taskType", "HTTP",
                        "url", "https://api.github.com/users/github",
                        "method", "GET",
                        "headers", Map.of("Accept", "application/json"),
                        "timeoutSeconds", 30
                )),
                createNode("process_response", "TASK", "处理响应", Map.of(
                        "taskType", "CUSTOM",
                        "className", "com.workflow.engine.example.HttpResponseProcessor",
                        "methodName", "processResponse"
                )),
                createNode("end", "END", "结束", null)
        ));
        
        // 定义边
        data.setEdges(List.of(
                createEdge("start_to_http", "start", "http_request"),
                createEdge("http_to_process", "http_request", "process_response"),
                createEdge("process_to_end", "process_response", "end")
        ));
        
        data.setProperties(Map.of(
                "description", "HTTP请求测试流程",
                "timeout", 300
        ));
        
        dto.setDefinitionData(data);
        
        try {
            workflowDefinitionService.createWorkflowDefinition(dto);
            logger.info("HTTP workflow example created successfully");
        } catch (Exception e) {
            logger.warn("HTTP workflow example may already exist: {}", e.getMessage());
        }
    }
    
    /**
     * 创建数据处理工作流
     */
    private void createDataProcessingWorkflow() {
        logger.info("Creating data processing workflow example...");
        
        WorkflowDefinitionDTO dto = new WorkflowDefinitionDTO();
        dto.setName("数据处理流程");
        dto.setDescription("包含数据查询、转换和存储的工作流");
        
        WorkflowDefinitionDTO.WorkflowDefinitionData data = new WorkflowDefinitionDTO.WorkflowDefinitionData();
        
        // 定义节点
        data.setNodes(List.of(
                createNode("start", "START", "开始", null),
                createNode("query_data", "TASK", "查询数据", Map.of(
                        "taskType", "SQL",
                        "sql", "SELECT * FROM users WHERE created_at > :startDate",
                        "parameters", Map.of("startDate", "2024-01-01"),
                        "timeoutSeconds", 300
                )),
                createNode("transform_data", "TASK", "数据转换", Map.of(
                        "taskType", "CUSTOM",
                        "className", "com.workflow.engine.example.DataTransformer",
                        "methodName", "transformData",
                        "parameters", Map.of("format", "json")
                )),
                createNode("store_data", "TASK", "存储数据", Map.of(
                        "taskType", "SQL",
                        "sql", "INSERT INTO processed_data (data, processed_at) VALUES (:data, :processedAt)",
                        "timeoutSeconds", 120
                )),
                createNode("end", "END", "结束", null)
        ));
        
        // 定义边
        data.setEdges(List.of(
                createEdge("start_to_query", "start", "query_data"),
                createEdge("query_to_transform", "query_data", "transform_data"),
                createEdge("transform_to_store", "transform_data", "store_data"),
                createEdge("store_to_end", "store_data", "end")
        ));
        
        data.setProperties(Map.of(
                "description", "数据处理流程",
                "timeout", 600
        ));
        
        dto.setDefinitionData(data);
        
        try {
            workflowDefinitionService.createWorkflowDefinition(dto);
            logger.info("Data processing workflow example created successfully");
        } catch (Exception e) {
            logger.warn("Data processing workflow example may already exist: {}", e.getMessage());
        }
    }
    
    /**
     * 创建邮件通知工作流
     */
    private void createEmailNotificationWorkflow() {
        logger.info("Creating email notification workflow example...");
        
        WorkflowDefinitionDTO dto = new WorkflowDefinitionDTO();
        dto.setName("邮件通知流程");
        dto.setDescription("发送邮件通知的工作流");
        
        WorkflowDefinitionDTO.WorkflowDefinitionData data = new WorkflowDefinitionDTO.WorkflowDefinitionData();
        
        // 定义节点
        data.setNodes(List.of(
                createNode("start", "START", "开始", null),
                createNode("prepare_content", "TASK", "准备邮件内容", Map.of(
                        "taskType", "CUSTOM",
                        "className", "com.workflow.engine.example.EmailContentPreparer",
                        "methodName", "prepareContent",
                        "parameters", Map.of("template", "notification")
                )),
                createNode("send_email", "TASK", "发送邮件", Map.of(
                        "taskType", "EMAIL",
                        "smtpHost", "smtp.gmail.com",
                        "smtpPort", 587,
                        "username", "your-email@gmail.com",
                        "password", "your-password",
                        "from", "noreply@workflow-engine.com",
                        "to", "recipient@example.com",
                        "subject", "工作流执行通知",
                        "body", "您的工作流已执行完成",
                        "html", true,
                        "timeoutSeconds", 60
                )),
                createNode("end", "END", "结束", null)
        ));
        
        // 定义边
        data.setEdges(List.of(
                createEdge("start_to_prepare", "start", "prepare_content"),
                createEdge("prepare_to_send", "prepare_content", "send_email"),
                createEdge("send_to_end", "send_email", "end")
        ));
        
        data.setProperties(Map.of(
                "description", "邮件通知流程",
                "timeout", 180
        ));
        
        dto.setDefinitionData(data);
        
        try {
            workflowDefinitionService.createWorkflowDefinition(dto);
            logger.info("Email notification workflow example created successfully");
        } catch (Exception e) {
            logger.warn("Email notification workflow example may already exist: {}", e.getMessage());
        }
    }
    
    /**
     * 创建脚本执行工作流
     */
    private void createScriptExecutionWorkflow() {
        logger.info("Creating script execution workflow example...");
        
        WorkflowDefinitionDTO dto = new WorkflowDefinitionDTO();
        dto.setName("脚本执行流程");
        dto.setDescription("执行脚本任务的工作流");
        
        WorkflowDefinitionDTO.WorkflowDefinitionData data = new WorkflowDefinitionDTO.WorkflowDefinitionData();
        
        // 定义节点
        data.setNodes(List.of(
                createNode("start", "START", "开始", null),
                createNode("validate_input", "TASK", "验证输入", Map.of(
                        "taskType", "SCRIPT",
                        "scriptType", "PYTHON",
                        "scriptContent", """
                                import sys
                                import json
                                
                                # 获取输入参数
                                input_data = json.loads(sys.argv[1]) if len(sys.argv) > 1 else {}
                                
                                # 验证输入
                                if 'data' not in input_data:
                                    print("Error: Missing required field 'data'", file=sys.stderr)
                                    sys.exit(1)
                                
                                # 输出验证结果
                                result = {"valid": True, "message": "Input validation passed"}
                                print(json.dumps(result))
                                """,
                        "timeoutSeconds", 60
                )),
                createNode("process_data", "TASK", "处理数据", Map.of(
                        "taskType", "SCRIPT",
                        "scriptType", "BASH",
                        "scriptContent", """
                                #!/bin/bash
                                
                                # 获取环境变量
                                INPUT_DATA="${INPUT_DATA:-'{}'}"
                                
                                echo "Processing data: $INPUT_DATA"
                                
                                # 模拟数据处理
                                sleep 2
                                
                                # 输出结果
                                echo '{"status": "completed", "processedItems": 100}'
                                """,
                        "parameters", Map.of("INPUT_DATA", "{}", "MAX_ITEMS", "1000"),
                        "timeoutSeconds", 120
                )),
                createNode("generate_report", "TASK", "生成报告", Map.of(
                        "taskType", "SCRIPT",
                        "scriptType", "PYTHON",
                        "scriptContent", """
                                import json
                                import datetime
                                
                                # 生成报告
                                report = {
                                    "timestamp": datetime.datetime.now().isoformat(),
                                    "status": "success",
                                    "summary": {
                                        "totalItems": 100,
                                        "processedItems": 100,
                                        "failedItems": 0
                                    }
                                }
                                
                                print(json.dumps(report, indent=2))
                                """,
                        "timeoutSeconds", 60
                )),
                createNode("end", "END", "结束", null)
        ));
        
        // 定义边
        data.setEdges(List.of(
                createEdge("start_to_validate", "start", "validate_input"),
                createEdge("validate_to_process", "validate_input", "process_data"),
                createEdge("process_to_report", "process_data", "generate_report"),
                createEdge("report_to_end", "generate_report", "end")
        ));
        
        data.setProperties(Map.of(
                "description", "脚本执行流程",
                "timeout", 300
        ));
        
        dto.setDefinitionData(data);
        
        try {
            workflowDefinitionService.createWorkflowDefinition(dto);
            logger.info("Script execution workflow example created successfully");
        } catch (Exception e) {
            logger.warn("Script execution workflow example may already exist: {}", e.getMessage());
        }
    }
    
    /**
     * 创建节点
     */
    private WorkflowDefinitionDTO.Node createNode(String id, String type, String name, Map<String, Object> properties) {
        WorkflowDefinitionDTO.Node node = new WorkflowDefinitionDTO.Node();
        node.setId(id);
        node.setType(type);
        node.setName(name);
        node.setProperties(properties);
        return node;
    }
    
    /**
     * 创建边
     */
    private WorkflowDefinitionDTO.Edge createEdge(String id, String source, String target) {
        WorkflowDefinitionDTO.Edge edge = new WorkflowDefinitionDTO.Edge();
        edge.setId(id);
        edge.setSource(source);
        edge.setTarget(target);
        return edge;
    }
}