package com.tiktok;


import com.alibaba.csp.sentinel.adapter.dubbo3.config.DubboAdapterGlobalConfig;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.tiktok.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.dubbo.rpc.AsyncRpcResult;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestClient;

import java.util.Collections;


@Slf4j
@SpringBootApplication
@EnableDubbo
@EnableTransactionManagement //开启注解方式的事务管理
public class OrderDubboApp {
    public static void main(String[] args) {
        SpringApplication.run(OrderDubboApp.class, args);
        log.info("Order dubbo application started");
    }


    //provider请求限流
    @Component
    static class SentinelMethodConfig implements CommandLineRunner {
        @Override
        public void run(String... args) {
            // Limit method to 6 QPS.
            FlowRule flowRule = new FlowRule();
            // Note: the resource name here includes the method signature.
            flowRule.setResource(OrderService.class.getName() + ":list(java.lang.Long)");
            flowRule.setCount(6);
            flowRule.setLimitApp("default");
            flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
            FlowRuleManager.loadRules(Collections.singletonList(flowRule));
            log.info("provider请求限流 set up successfully.");
        }
    }


    //provider fall back
    @Component
    static class ProviderSentinelCallbackConfig implements CommandLineRunner {
        @Override
        public void run(String... args) {
            DubboAdapterGlobalConfig.setProviderFallback((invoker, invocation, ex) -> {
                System.out.println("Fallback:provider请求限流 --> Blocked by Sentinel: " + ex.getClass().getSimpleName() + ", " + invocation);
                return AsyncRpcResult.newDefaultAsyncResult(ex.toRuntimeException(), invocation);
            });
        }
    }

    //Spring ai alibaba 配置
    @Bean
    @ConditionalOnMissingBean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}
