package com.workflow.engine.config;

import com.workflow.engine.executor.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TaskExecutorConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    public Map<String, TaskExecutor> taskExecutors(
            HttpTaskExecutor httpTaskExecutor,
            SqlTaskExecutor sqlTaskExecutor,
            ScriptTaskExecutor scriptTaskExecutor,
            EmailTaskExecutor emailTaskExecutor,
            CustomTaskExecutor customTaskExecutor) {
        
        Map<String, TaskExecutor> executors = new HashMap<>();
        executors.put(httpTaskExecutor.getTaskType(), httpTaskExecutor);
        executors.put(sqlTaskExecutor.getTaskType(), sqlTaskExecutor);
        executors.put(scriptTaskExecutor.getTaskType(), scriptTaskExecutor);
        executors.put(emailTaskExecutor.getTaskType(), emailTaskExecutor);
        executors.put(customTaskExecutor.getTaskType(), customTaskExecutor);
        
        return executors;
    }
    
    @Bean
    public HttpTaskExecutor httpTaskExecutor() {
        return new HttpTaskExecutor();
    }
    
    @Bean
    public SqlTaskExecutor sqlTaskExecutor() {
        return new SqlTaskExecutor();
    }
    
    @Bean
    public ScriptTaskExecutor scriptTaskExecutor() {
        return new ScriptTaskExecutor();
    }
    
    @Bean
    public EmailTaskExecutor emailTaskExecutor() {
        return new EmailTaskExecutor();
    }
    
    @Bean
    public CustomTaskExecutor customTaskExecutor() {
        return new CustomTaskExecutor();
    }
}