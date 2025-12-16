package com.workflow.engine.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户详情服务
 * 提供用户认证信息（演示用，实际项目中应从数据库获取）
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final List<UserDetails> users = new ArrayList<>();
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostConstruct
    public void init() {
        // 演示用户数据，实际项目中应从数据库获取
        users.add(User.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin123"))
            .authorities("ROLE_ADMIN", "ROLE_USER")
            .build());
            
        users.add(User.builder()
            .username("user")
            .password(passwordEncoder.encode("user123"))
            .authorities("ROLE_USER")
            .build());
            
        users.add(User.builder()
            .username("guest")
            .password(passwordEncoder.encode("guest123"))
            .authorities("ROLE_GUEST")
            .build());
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst()
            .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
    }
}