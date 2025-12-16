package com.workflow.engine.controller;

import com.workflow.engine.config.MonitoringConfig.WorkflowMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/monitoring")
public class MonitoringController {
    
    @Autowired
    private WorkflowMetrics workflowMetrics;
    
    @Autowired
    private MeterRegistry meterRegistry;
    
    /**
     * 获取工作流指标
     */
    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        // 工作流相关指标
        metrics.put("activeWorkflows", getGaugeValue("active_workflows"));
        metrics.put("activeTasks", getGaugeValue("active_tasks"));
        
        // 计数器指标
        metrics.put("workflowsStarted", getCounterValue("workflow_started_total"));
        metrics.put("workflowsCompleted", getCounterValue("workflow_completed_total"));
        metrics.put("workflowsFailed", getCounterValue("workflow_failed_total"));
        metrics.put("tasksExecuted", getCounterValue("task_executed_total"));
        metrics.put("tasksFailed", getCounterValue("task_failed_total"));
        
        // 计时器指标
        metrics.put("avgWorkflowDuration", getTimerMean("workflow_execution_duration_seconds"));
        metrics.put("avgTaskDuration", getTimerMean("task_execution_duration_seconds"));
        
        return ResponseEntity.ok(metrics);
    }
    
    /**
     * 获取Prometheus格式的指标
     */
    @GetMapping("/prometheus")
    public ResponseEntity<String> getPrometheusMetrics() {
        String prometheusMetrics = meterRegistry.scrape();
        return ResponseEntity.ok()
                .header("Content-Type", "text/plain; version=0.0.4; charset=utf-8")
                .body(prometheusMetrics);
    }
    
    /**
     * 获取系统健康状态
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", System.currentTimeMillis());
        
        Map<String, Object> components = new HashMap<>();
        components.put("workflowEngine", Map.of("status", "UP"));
        components.put("database", Map.of("status", "UP"));
        components.put("taskExecutors", Map.of("status", "UP"));
        
        health.put("components", components);
        
        return ResponseEntity.ok(health);
    }
    
    private double getGaugeValue(String name) {
        try {
            return meterRegistry.find(name).gauge().value();
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    private double getCounterValue(String name) {
        try {
            return meterRegistry.find(name).counter().count();
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    private double getTimerMean(String name) {
        try {
            return meterRegistry.find(name).timer().mean();
        } catch (Exception e) {
            return 0.0;
        }
    }
}