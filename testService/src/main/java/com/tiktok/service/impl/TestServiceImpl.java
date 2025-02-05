package com.tiktok.service.impl;

import com.tiktok.service.TestService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 测试dubbo服务rpc调用正常
 */
//@Transactional, 加上interfaceClass事务注解才能生效
@DubboService(interfaceClass = TestService.class)
public class TestServiceImpl implements TestService {
    @Override
    public String test() {
        return "test dubbo service !!!";
    }
}
