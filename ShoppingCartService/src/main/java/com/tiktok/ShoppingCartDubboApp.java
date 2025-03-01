package com.tiktok;


import com.alibaba.csp.sentinel.adapter.dubbo3.config.DubboAdapterGlobalConfig;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.tiktok.entity.Product;
import com.tiktok.service.ProductService;
import com.tiktok.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.dubbo.rpc.AsyncRpcResult;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;


@Slf4j
@SpringBootApplication
@EnableDubbo
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

    //consumer熔断降级
    @Component
    static class SentinelDowngradeConfig implements CommandLineRunner {
        @Override
        public void run(String... args) {
            List<DegradeRule> rules = new ArrayList<>();
            DegradeRule rule = new DegradeRule();
            rule.setResource(ProductService.class.getName() + ":getProductById(java.lang.Long)");
            rule.setGrade(CircuitBreakerStrategy.SLOW_REQUEST_RATIO.getType()); //熔断策略:慢调用比例
            rule.setCount(200); // 200ms，慢调用临界 RT（超出该值计为慢调用）
            rule.setMinRequestAmount(5); //熔断触发的最小请求数
            rule.setStatIntervalMs(1000); // 1s, 统计时长（单位为 ms）
            rule.setTimeWindow(20); //熔断时长，单位为 s
            rule.setSlowRatioThreshold(0.5); //慢调用比例阈值
            rules.add(rule);

            //这种是按照慢调用比例来做熔断，上述配置的含义是：
            //- RT超过200毫秒的请求调用就是慢调用
            //- 统计最近1000ms内的最少5次请求，如果慢调用比例不低于0.5，则触发熔断
            //- 熔断持续时长20s

            DegradeRuleManager.loadRules(rules);
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
