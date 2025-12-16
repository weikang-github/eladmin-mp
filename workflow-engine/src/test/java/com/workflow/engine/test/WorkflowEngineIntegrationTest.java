package com.workflow.engine.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workflow.engine.dto.WorkflowDefinitionDTO;
import com.workflow.engine.entity.WorkflowDefinition;
import com.workflow.engine.entity.WorkflowExecution;
import com.workflow.engine.entity.WorkflowExecutionStatus;
import com.workflow.engine.repository.WorkflowDefinitionRepository;
import com.workflow.engine.repository.WorkflowExecutionRepository;
import com.workflow.engine.service.WorkflowDefinitionService;
import com.workflow.engine.service.WorkflowExecutionEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 工作流引擎集成测试
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class WorkflowEngineIntegrationTest {
    
    private static final Logger logger = LoggerFactory.getLogger(WorkflowEngineIntegrationTest.class);
    
    @Autowired
    private WorkflowDefinitionService workflowDefinitionService;
    
    @Autowired
    private WorkflowExecutionEngine workflowExecutionEngine;
    
    @Autowired
    private WorkflowDefinitionRepository workflowDefinitionRepository;
    
    @Autowired
    private WorkflowExecutionRepository workflowExecutionRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private Long testWorkflowId;
    
    @BeforeEach
    public void setUp() {
        // 清理测试数据
        workflowExecutionRepository.deleteAll();
        workflowDefinitionRepository.deleteAll();
        
        // 创建测试工作流定义
        testWorkflowId = createTestWorkflowDefinition();
    }
    
    /**
     * 测试简单工作流执行
     */
    @Test
    public void testSimpleWorkflowExecution() throws Exception {
        logger.info("Testing simple workflow execution...");
        
        // 准备输入参数
        Map<String, Object> inputParameters = Map.of(
                "inputData", "test data",
                "timeout", 30
        );
        
        // 执行工作流
        Long executionId = workflowExecutionEngine.startWorkflow(
                testWorkflowId, 
                inputParameters, 
                "test-user"
        );
        
        assertNotNull(executionId, "Execution ID should not be null");
        logger.info("Workflow execution started with ID: {}", executionId);
        
        // 等待执行完成（异步执行）
        Thread.sleep(5000);
        
        // 验证执行状态
        WorkflowExecution execution = workflowExecutionRepository.findById(executionId)
                .orElseThrow(() -> new AssertionError("Execution not found"));
        
        logger.info("Workflow execution status: {}", execution.getStatus());
        
        // 断言执行状态
        assertTrue(
                execution.getStatus() == WorkflowExecutionStatus.COMPLETED ||
                execution.getStatus() == WorkflowExecutionStatus.RUNNING,
                "Workflow should be completed or running"
        );
        
        assertNotNull(execution.getStartedAt(), "Start time should not be null");
        assertEquals("test-user", execution.getStartedBy(), "Started by should match");
    }
    
    /**
     * 测试工作流定义创建
     */
    @Test
    public void testWorkflowDefinitionCreation() {
        logger.info("Testing workflow definition creation...");
        
        // 创建新的工作流定义
        WorkflowDefinitionDTO dto = createSampleWorkflowDefinition();
        
        WorkflowDefinition definition = workflowDefinitionService.createWorkflowDefinition(dto);
        
        assertNotNull(definition.getId(), "Definition ID should not be null");
        assertEquals("测试工作流", definition.getName(), "Name should match");
        assertEquals("测试用工作流定义", definition.getDescription(), "Description should match");
        assertNotNull(definition.getCreatedAt(), "Creation time should not be null");
        
        logger.info("Created workflow definition with ID: {}", definition.getId());
    }
    
    /**
     * 测试工作流查询
     */
    @Test
    public void testWorkflowQuery() {
        logger.info("Testing workflow query...");
        
        // 查询所有工作流定义
        List<WorkflowDefinition> definitions = workflowDefinitionRepository.findAll();
        
        assertFalse(definitions.isEmpty(), "Should have at least one workflow definition");
        assertTrue(definitions.size() >= 1, "Should have at least one workflow definition");
        
        logger.info("Found {} workflow definitions", definitions.size());
        
        // 验证工作流定义
        WorkflowDefinition definition = definitions.get(0);
        assertNotNull(definition.getId(), "Definition ID should not be null");
        assertNotNull(definition.getName(), "Name should not be null");
        assertNotNull(definition.getDefinitionData(), "Definition data should not be null");
    }
    
    /**
     * 测试工作流执行查询
     */
    @Test
    public void testWorkflowExecutionQuery() throws Exception {
        logger.info("Testing workflow execution query...");
        
        // 执行工作流
        Map<String, Object> inputParameters = Map.of("test", "data");
        Long executionId = workflowExecutionEngine.startWorkflow(testWorkflowId, inputParameters, "test-user");
        
        // 等待执行
        Thread.sleep(2000);
        
        // 查询执行记录
        List<WorkflowExecution> executions = workflowExecutionRepository.findByWorkflowDefinitionId(testWorkflowId);
        
        assertFalse(executions.isEmpty(), "Should have at least one execution");
        
        WorkflowExecution execution = executions.get(0);
        assertEquals(testWorkflowId, execution.getWorkflowDefinitionId(), "Workflow definition ID should match");
        assertEquals("test-user", execution.getStartedBy(), "Started by should match");
        assertNotNull(execution.getStartedAt(), "Start time should not be null");
        
        logger.info("Found {} executions for workflow {}", executions.size(), testWorkflowId);
    }
    
    /**
     * 测试并发工作流执行
     */
    @Test
    public void testConcurrentWorkflowExecution() throws Exception {
        logger.info("Testing concurrent workflow execution...");
        
        // 并发执行多个工作流实例
        int concurrentExecutions = 3;
        
        for (int i = 0; i < concurrentExecutions; i++) {
            final int index = i;
            
            Map<String, Object> inputParameters = Map.of(
                    "executionIndex", index,
                    "data", "test-data-" + index
            );
            
            Long executionId = workflowExecutionEngine.startWorkflow(
                    testWorkflowId, 
                    inputParameters, 
                    "concurrent-user-" + index
            );
            
            logger.info("Started concurrent execution {} with ID: {}", index, executionId);
        }
        
        // 等待所有执行完成
        Thread.sleep(10000);
        
        // 验证执行数量
        List<WorkflowExecution> executions = workflowExecutionRepository.findByWorkflowDefinitionId(testWorkflowId);
        
        assertEquals(concurrentExecutions, executions.size(), 
                "Should have exactly " + concurrentExecutions + " executions");
        
        logger.info("Successfully executed {} concurrent workflows", concurrentExecutions);
    }
    
    /**
     * 测试工作流定义更新
     */
    @Test
    public void testWorkflowDefinitionUpdate() {
        logger.info("Testing workflow definition update...");
        
        // 获取现有工作流定义
        WorkflowDefinition existingDefinition = workflowDefinitionRepository.findById(testWorkflowId)
                .orElseThrow(() -> new AssertionError("Test workflow definition not found"));
        
        // 更新定义
        existingDefinition.setDescription("更新后的描述");
        existingDefinition.setVersion(existingDefinition.getVersion() + 1);
        
        WorkflowDefinition updatedDefinition = workflowDefinitionRepository.save(existingDefinition);
        
        assertEquals("更新后的描述", updatedDefinition.getDescription(), "Description should be updated");
        assertEquals(existingDefinition.getVersion() + 1, updatedDefinition.getVersion(), "Version should be incremented");
        
        logger.info("Updated workflow definition with ID: {}", updatedDefinition.getId());
    }
    
    /**
     * 创建测试工作流定义
     */
    private Long createTestWorkflowDefinition() {
        WorkflowDefinitionDTO dto = createSampleWorkflowDefinition();
        WorkflowDefinition definition = workflowDefinitionService.createWorkflowDefinition(dto);
        return definition.getId();
    }
    
    /**
     * 创建示例工作流定义
     */
    private WorkflowDefinitionDTO createSampleWorkflowDefinition() {
        WorkflowDefinitionDTO dto = new WorkflowDefinitionDTO();
        dto.setName("测试工作流");
        dto.setDescription("测试用工作流定义");
        
        WorkflowDefinitionDTO.WorkflowDefinitionData data = new WorkflowDefinitionDTO.WorkflowDefinitionData();
        
        // 定义节点
        data.setNodes(List.of(
                createNode("start", "START", "开始", null),
                createNode("task1", "TASK", "HTTP请求任务", Map.of(
                        "taskType", "HTTP",
                        "url", "https://api.github.com/users/github",
                        "method", "GET",
                        "timeoutSeconds", 30
                )),
                createNode("task2", "TASK", "数据处理任务", Map.of(
                        "taskType", "CUSTOM",
                        "className", "com.workflow.engine.example.HttpResponseProcessor",
                        "methodName", "processResponse"
                )),
                createNode("end", "END", "结束", null)
        ));
        
        // 定义边
        data.setEdges(List.of(
                createEdge("start_to_task1", "start", "task1"),
                createEdge("task1_to_task2", "task1", "task2"),
                createEdge("task2_to_end", "task2", "end")
        ));
        
        data.setProperties(Map.of(
                "description", "测试工作流",
                "timeout", 300
        ));
        
        dto.setDefinitionData(data);
        return dto;
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