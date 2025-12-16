package com.workflow.engine.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证入口点
 * 处理未认证用户的访问请求
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
        
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        String errorMessage = "未认证访问 - " + authException.getMessage();
        String jsonResponse = String.format(
            "{\"error\": \"%s\", \"message\": \"%s\", \"path\": \"%s\", \"timestamp\": \"%s\"}",
            "UNAUTHORIZED",
            errorMessage,
            request.getRequestURI(),
            java.time.Instant.now().toString()
        );
        
        response.getWriter().write(jsonResponse);
    }
}