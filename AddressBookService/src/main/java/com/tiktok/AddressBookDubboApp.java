package com.tiktok;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Slf4j
@SpringBootApplication
//@EnableDubbo
@EnableTransactionManagement //开启注解方式的事务管理
public class AddressBookDubboApp {
    public static void main(String[] args) {
        SpringApplication.run(AddressBookDubboApp.class, args);
        log.info("AddressBook dubbo application started");
    }
}
