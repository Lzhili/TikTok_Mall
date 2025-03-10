package com.tiktok;


import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Slf4j
@SpringBootApplication
@EnableScheduling
@EnableDubbo
@EnableTransactionManagement //开启注解方式的事务管理
public class AuthDubboApp {
    public static void main(String[] args) {
        SpringApplication.run(AuthDubboApp.class, args);
        log.info("Auth dubbo application started");
    }
}
