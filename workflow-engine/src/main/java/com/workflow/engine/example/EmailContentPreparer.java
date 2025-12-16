package com.workflow.engine.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 邮件内容准备器示例
 */
@Component
public class EmailContentPreparer {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailContentPreparer.class);
    
    /**
     * 准备邮件内容
     */
    public Map<String, Object> prepareContent(String template) {
        logger.info("Preparing email content with template: {}", template);
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            String subject;
            String body;
            
            switch (template.toLowerCase()) {
                case "notification":
                    subject = "工作流执行通知";
                    body = createNotificationEmail();
                    break;
                    
                case "report":
                    subject = "工作流执行报告";
                    body = createReportEmail();
                    break;
                    
                case "alert":
                    subject = "工作流执行告警";
                    body = createAlertEmail();
                    break;
                    
                default:
                    subject = "工作流执行通知";
                    body = createDefaultEmail();
            }
            
            result.put("subject", subject);
            result.put("body", body);
            result.put("template", template);
            result.put("status", "success");
            result.put("message", "Email content prepared successfully");
            
            logger.info("Email content prepared successfully for template: {}", template);
            
        } catch (Exception e) {
            logger.error("Failed to prepare email content", e);
            
            result.put("status", "error");
            result.put("message", "Failed to prepare email content: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 创建通知邮件
     */
    private String createNotificationEmail() {
        return """
                <html>
                <body>
                    <h2>工作流执行通知</h2>
                    <p>您好，</p>
                    <p>您的工作流已成功执行完成。</p>
                    <h3>执行摘要</h3>
                    <ul>
                        <li>工作流名称：示例工作流</li>
                        <li>执行时间：""" + new java.util.Date() + """</li>
                        <li>状态：成功</li>
                        <li>耗时：2分钟</li>
                    </ul>
                    <p>如有任何问题，请联系技术支持。</p>
                    <p>谢谢！</p>
                </body>
                </html>
                """;
    }
    
    /**
     * 创建报告邮件
     */
    private String createReportEmail() {
        return """
                <html>
                <body>
                    <h2>工作流执行报告</h2>
                    <p>您好，</p>
                    <p>以下是您的工作流执行详细报告：</p>
                    
                    <h3>执行统计</h3>
                    <table border="1" cellpadding="5">
                        <tr><th>指标</th><th>数值</th></tr>
                        <tr><td>总任务数</td><td>5</td></tr>
                        <tr><td>成功任务</td><td>5</td></tr>
                        <tr><td>失败任务</td><td>0</td></tr>
                        <tr><td>总耗时</td><td>2分钟</td></tr>
                    </table>
                    
                    <h3>任务详情</h3>
                    <ul>
                        <li>数据查询 - 成功 (30秒)</li>
                        <li>数据处理 - 成功 (45秒)</li>
                        <li>结果验证 - 成功 (15秒)</li>
                        <li>数据存储 - 成功 (20秒)</li>
                        <li>通知发送 - 成功 (10秒)</li>
                    </ul>
                    
                    <p>报告生成时间：""" + new java.util.Date() + """</p>
                    <p>谢谢！</p>
                </body>
                </html>
                """;
    }
    
    /**
     * 创建告警邮件
     */
    private String createAlertEmail() {
        return """
                <html>
                <body>
                    <h2 style="color: red;">工作流执行告警</h2>
                    <p>您好，</p>
                    <p><strong style="color: red;">注意：您的工作流执行出现异常。</strong></p>
                    
                    <h3>告警信息</h3>
                    <div style="background-color: #ffebee; padding: 10px; border-left: 4px solid #f44336;">
                        <p><strong>工作流名称：</strong>示例工作流</p>
                        <p><strong>告警时间：</strong>""" + new java.util.Date() + """</p>
                        <p><strong>异常任务：</strong>数据查询</p>
                        <p><strong>错误信息：</strong>数据库连接超时</p>
                        <p><strong>建议操作：</strong>检查数据库连接配置和网络状态</p>
                    </div>
                    
                    <h3>后续处理</h3>
                    <ol>
                        <li>检查系统日志获取详细信息</li>
                        <li>验证数据库连接配置</li>
                        <li>检查网络连接状态</li>
                        <li>联系技术支持</li>
                    </ol>
                    
                    <p>请及时处理此告警。</p>
                    <p>谢谢！</p>
                </body>
                </html>
                """;
    }
    
    /**
     * 创建默认邮件
     */
    private String createDefaultEmail() {
        return """
                <html>
                <body>
                    <h2>工作流执行通知</h2>
                    <p>您好，</p>
                    <p>您的工作流已执行完成。</p>
                    <p>执行时间：""" + new java.util.Date() + """</p>
                    <p>谢谢！</p>
                </body>
                </html>
                """;
    }
}