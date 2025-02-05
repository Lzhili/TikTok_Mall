package com.tiktok.service;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
public class TestApiService {

    @DubboReference
    private TestService testService;

    public String test() {
        // api 只是业务逻辑组合，具体的逻辑和表查询，应该交给dubbo服务
        // 访问的请求多了，一个dubbo服务扛不住了，可以部署多个，api的代码不需要做更改
        return testService.test();
    }
}
