package com.workflow.engine.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class MonitoringConfig {
    
    @Bean
    public PrometheusMeterRegistry prometheusMeterRegistry() {
        return new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }
    
    @Bean
    public WorkflowMetrics workflowMetrics(MeterRegistry meterRegistry) {
        return new WorkflowMetrics(meterRegistry);
    }
    
    /**
     * 工作流指标收集器
     */
    public static class WorkflowMetrics {
        
        private final MeterRegistry meterRegistry;
        private final Counter workflowStartedCounter;
        private final Counter workflowCompletedCounter;
        private final Counter workflowFailedCounter;
        private final Counter taskExecutedCounter;
        private final Counter taskFailedCounter;
        private final Timer workflowExecutionTimer;
        private final Timer taskExecutionTimer;
        private final AtomicLong activeWorkflows = new AtomicLong(0);
        private final AtomicLong activeTasks = new AtomicLong(0);
        
        public WorkflowMetrics(MeterRegistry meterRegistry) {
            this.meterRegistry = meterRegistry;
            
            // 工作流相关指标
            this.workflowStartedCounter = Counter.builder("workflow_started_total")
                    .description("Total number of workflows started")
                    .register(meterRegistry);
            
            this.workflowCompletedCounter = Counter.builder("workflow_completed_total")
                    .description("Total number of workflows completed successfully")
                    .register(meterRegistry);
            
            this.workflowFailedCounter = Counter.builder("workflow_failed_total")
                    .description("Total number of workflows failed")
                    .register(meterRegistry);
            
            // 任务相关指标
            this.taskExecutedCounter = Counter.builder("task_executed_total")
                    .description("Total number of tasks executed")
                    .register(meterRegistry);
            
            this.taskFailedCounter = Counter.builder("task_failed_total")
                    .description("Total number of tasks failed")
                    .register(meterRegistry);
            
            // 执行时间指标
            this.workflowExecutionTimer = Timer.builder("workflow_execution_duration_seconds")
                    .description("Workflow execution duration in seconds")
                    .register(meterRegistry);
            
            this.taskExecutionTimer = Timer.builder("task_execution_duration_seconds")
                    .description("Task execution duration in seconds")
                    .register(meterRegistry);
            
            // 活跃工作流和任务数
            Gauge.builder("active_workflows")
                    .description("Number of currently active workflows")
                    .register(meterRegistry, this, metrics -> metrics.activeWorkflows.get());
            
            Gauge.builder("active_tasks")
                    .description("Number of currently active tasks")
                    .register(meterRegistry, this, metrics -> metrics.activeTasks.get());
        }
        
        public void recordWorkflowStarted() {
            workflowStartedCounter.increment();
            activeWorkflows.incrementAndGet();
        }
        
        public void recordWorkflowCompleted() {
            workflowCompletedCounter.increment();
            activeWorkflows.decrementAndGet();
        }
        
        public void recordWorkflowFailed() {
            workflowFailedCounter.increment();
            activeWorkflows.decrementAndGet();
        }
        
        public void recordTaskExecuted() {
            taskExecutedCounter.increment();
            activeTasks.incrementAndGet();
        }
        
        public void recordTaskFailed() {
            taskFailedCounter.increment();
            activeTasks.decrementAndGet();
        }
        
        public void recordTaskCompleted() {
            activeTasks.decrementAndGet();
        }
        
        public Timer.Sample startWorkflowTimer() {
            return Timer.start(meterRegistry);
        }
        
        public Timer.Sample startTaskTimer() {
            return Timer.start(meterRegistry);
        }
        
        public void recordWorkflowDuration(Timer.Sample sample) {
            sample.stop(workflowExecutionTimer);
        }
        
        public void recordTaskDuration(Timer.Sample sample) {
            sample.stop(taskExecutionTimer);
        }
        
        public MeterRegistry getMeterRegistry() {
            return meterRegistry;
        }
    }
}