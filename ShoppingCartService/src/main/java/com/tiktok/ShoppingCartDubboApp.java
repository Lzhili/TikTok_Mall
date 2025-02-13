package com.tiktok;


import com.alibaba.csp.sentinel.adapter.dubbo3.config.DubboAdapterGlobalConfig;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.tiktok.entity.Product;
import com.tiktok.service.ProductService;
import com.tiktok.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.AsyncRpcResult;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Collections;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;


@Slf4j
@SpringBootApplication
//@EnableDubbo
@EnableTransactionManagement //开启注解方式的事务管理
public class ShoppingCartDubboApp {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartDubboApp.class, args);
        log.info("ShoppingCart dubbo application started");
    }

    //consumer线程隔离
    @Component
    static class SentinelConfig implements CommandLineRunner {
        @Override
        public void run(String... args) {
            FlowRule flowRule = new FlowRule();
            //购物车的添加购物车业务设置线程隔离为5，并发调用在超过 5 个线程时就会发生限流
            flowRule.setResource(ProductService.class.getName() + ":getProductById(java.lang.Long)");
            flowRule.setCount(5);
            flowRule.setGrade(RuleConstant.FLOW_GRADE_THREAD);
            FlowRuleManager.loadRules(Collections.singletonList(flowRule));
            log.info("consumer线程隔离 set up successfully.");
        }
    }

    //Consumer fall back
    @Component
    static class ConsumerSentinelCallbackConfig implements CommandLineRunner {
        @Override
        public void run(String... args) {
            // Register fallback handler for consumer.
            // If you only want to handle degrading, you need to
            // check the type of BlockException.
            DubboAdapterGlobalConfig.setConsumerFallback((invoker, invocation, ex) -> {
                System.out.println("Fallback:consumer线程隔离和熔断降级 --> Blocked by Sentinel: " + ex.getClass().getSimpleName() + ", " + invocation);
                return AsyncRpcResult.newDefaultAsyncResult(ex.toRuntimeException(), invocation);
            });
        }
    }
}
