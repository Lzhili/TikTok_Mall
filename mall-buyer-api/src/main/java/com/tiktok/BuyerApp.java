package com.tiktok;


import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
//@EnableDubbo
@SpringBootApplication
public class BuyerApp {
    public static void main(String[] args) {
        SpringApplication.run(BuyerApp.class, args);
        log.info("buyer application started");
    }
}
