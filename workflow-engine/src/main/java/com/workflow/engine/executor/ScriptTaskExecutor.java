package com.workflow.engine.executor;

import com.fasterxml.jackson.databind.JsonNode;
import com.workflow.engine.dto.DagNode;
import com.workflow.engine.entity.TaskExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class ScriptTaskExecutor implements TaskExecutor {
    
    private static final Logger logger = LoggerFactory.getLogger(ScriptTaskExecutor.class);
    private static final String TASK_TYPE = "SCRIPT";
    private static final int DEFAULT_TIMEOUT_SECONDS = 300; // 5 minutes
    
    @Override
    public String getTaskType() {
        return TASK_TYPE;
    }
    
    @Override
    public TaskExecutionResult execute(TaskExecution taskExecution, DagNode dagNode) throws Exception {
        logger.info("Executing script task: {}", taskExecution.getTaskName());
        
        JsonNode properties = dagNode.getProperties();
        String scriptType = properties.get("scriptType").asText();
        String scriptContent = properties.get("scriptContent").asText();
        JsonNode parameters = properties.get("parameters");
        
        // 创建临时脚本文件
        Path scriptFile = createTempScriptFile(scriptType, scriptContent);
        
        try {
            // 设置执行权限
            setExecutablePermission(scriptFile);
            
            // 构建命令
            String[] command = buildCommand(scriptFile, scriptType);
            
            // 创建进程构建器
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            
            // 设置环境变量
            if (parameters != null) {
                Map<String, String> env = processBuilder.environment();
                parameters.fields().forEachRemaining(field -> {
                    env.put(field.getKey(), field.getValue().asText());
                });
            }
            
            // 启动进程
            Process process = processBuilder.start();
            
            // 读取输出
            StringBuilder output = new StringBuilder();
            StringBuilder error = new StringBuilder();
            
            try (BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                
                String line;
                while ((line = outputReader.readLine()) != null) {
                    output.append(line).append("\n");
                    logger.debug("Script output: {}", line);
                }
                
                while ((line = errorReader.readLine()) != null) {
                    error.append(line).append("\n");
                    logger.warn("Script error: {}", line);
                }
            }
            
            // 等待进程完成
            boolean finished = process.waitFor(getTimeoutSeconds(dagNode), TimeUnit.SECONDS);
            
            if (!finished) {
                process.destroyForcibly();
                return TaskExecutionResult.failure("Script execution timed out");
            }
            
            int exitCode = process.exitValue();
            
            if (exitCode == 0) {
                return TaskExecutionResult.success(Map.of(
                        "exitCode", exitCode,
                        "output", output.toString(),
                        "error", error.toString()
                ));
            } else {
                return TaskExecutionResult.failure(String.format("Script failed with exit code %d: %s", exitCode, error.toString()));
            }
            
        } finally {
            // 清理临时文件
            try {
                Files.deleteIfExists(scriptFile);
            } catch (Exception e) {
                logger.warn("Failed to delete temporary script file: {}", scriptFile, e);
            }
        }
    }
    
    @Override
    public void validateConfig(DagNode dagNode) throws IllegalArgumentException {
        JsonNode properties = dagNode.getProperties();
        
        if (properties == null || !properties.has("scriptType")) {
            throw new IllegalArgumentException("Script type is required");
        }
        
        if (!properties.has("scriptContent")) {
            throw new IllegalArgumentException("Script content is required");
        }
        
        String scriptType = properties.get("scriptType").asText();
        if (!isValidScriptType(scriptType)) {
            throw new IllegalArgumentException("Invalid script type: " + scriptType);
        }
    }
    
    @Override
    public Integer getTimeoutSeconds(DagNode dagNode) {
        JsonNode properties = dagNode.getProperties();
        if (properties != null && properties.has("timeoutSeconds")) {
            return properties.get("timeoutSeconds").asInt(DEFAULT_TIMEOUT_SECONDS);
        }
        return DEFAULT_TIMEOUT_SECONDS;
    }
    
    private Path createTempScriptFile(String scriptType, String scriptContent) throws Exception {
        String extension = getScriptExtension(scriptType);
        Path tempFile = Files.createTempFile("workflow_script_", extension);
        Files.write(tempFile, scriptContent.getBytes());
        return tempFile;
    }
    
    private void setExecutablePermission(Path scriptFile) throws Exception {
        try {
            Set<PosixFilePermission> permissions = new HashSet<>();
            permissions.add(PosixFilePermission.OWNER_READ);
            permissions.add(PosixFilePermission.OWNER_WRITE);
            permissions.add(PosixFilePermission.OWNER_EXECUTE);
            Files.setPosixFilePermissions(scriptFile, permissions);
        } catch (UnsupportedOperationException e) {
            // Windows系统不支持POSIX权限，跳过设置
            logger.debug("Skipping POSIX permission setting on Windows");
        }
    }
    
    private String[] buildCommand(Path scriptFile, String scriptType) {
        switch (scriptType.toUpperCase()) {
            case "BASH":
            case "SH":
                return new String[]{"bash", scriptFile.toString()};
            case "PYTHON":
                return new String[]{"python", scriptFile.toString()};
            case "PYTHON3":
                return new String[]{"python3", scriptFile.toString()};
            case "NODE":
            case "JAVASCRIPT":
                return new String[]{"node", scriptFile.toString()};
            case "POWERSHELL":
                return new String[]{"powershell", "-ExecutionPolicy", "Bypass", "-File", scriptFile.toString()};
            default:
                throw new IllegalArgumentException("Unsupported script type: " + scriptType);
        }
    }
    
    private String getScriptExtension(String scriptType) {
        switch (scriptType.toUpperCase()) {
            case "BASH":
            case "SH":
                return ".sh";
            case "PYTHON":
            case "PYTHON3":
                return ".py";
            case "NODE":
            case "JAVASCRIPT":
                return ".js";
            case "POWERSHELL":
                return ".ps1";
            default:
                return ".txt";
        }
    }
    
    private boolean isValidScriptType(String scriptType) {
        return scriptType != null && (
                "BASH".equalsIgnoreCase(scriptType) ||
                "SH".equalsIgnoreCase(scriptType) ||
                "PYTHON".equalsIgnoreCase(scriptType) ||
                "PYTHON3".equalsIgnoreCase(scriptType) ||
                "NODE".equalsIgnoreCase(scriptType) ||
                "JAVASCRIPT".equalsIgnoreCase(scriptType) ||
                "POWERSHELL".equalsIgnoreCase(scriptType)
        );
    }
}