package me.zhengjie.modules.forest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * 森林资源管理模块启动类
 * @author [你的名字]
 */
@SpringBootApplication
@ComponentScan(basePackages = {"me.zhengjie"})
@MapperScan(basePackages = {"me.zhengjie.modules.forest.mapper"})
@EntityScan(basePackages = {"me.zhengjie.modules.forest.domain"})
public class ForestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForestApplication.class, args);
    }

}