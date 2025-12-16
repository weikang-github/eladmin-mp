package com.workflow.engine.security;

import com.workflow.engine.dto.LoginRequest;
import com.workflow.engine.dto.LoginResponse;
import com.workflow.engine.security.JwtAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.stream.Collectors;

/**
 * 认证控制器
 * 处理用户登录和JWT令牌生成
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("User login attempt: {}", loginRequest.getUsername());
        
        try {
            // 验证用户凭据
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );
            
            User user = (User) authentication.getPrincipal();
            String roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
            
            // 生成JWT令牌
            String token = jwtAuthenticationFilter.generateToken(user.getUsername(), roles);
            
            logger.info("User login successful: {}", loginRequest.getUsername());
            
            return ResponseEntity.ok(new LoginResponse(
                token,
                user.getUsername(),
                roles,
                "Bearer",
                86400000 // 24小时
            ));
            
        } catch (BadCredentialsException e) {
            logger.warn("User login failed - invalid credentials: {}", loginRequest.getUsername());
            throw new BadCredentialsException("用户名或密码错误");
        } catch (Exception e) {
            logger.error("User login failed - unexpected error: {}", loginRequest.getUsername(), e);
            throw new RuntimeException("登录失败，请稍后重试");
        }
    }
    
    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        logger.info("User logout");
        // JWT是无状态的，登出只需客户端删除令牌
        return ResponseEntity.ok().build();
    }
}