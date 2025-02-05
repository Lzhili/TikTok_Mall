package com.tiktok.controller;


import com.tiktok.result.Result;
import com.tiktok.service.TestApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试dubbo服务rpc调用正常
 */
@Slf4j
@RestController
@Tag(name = "测试接口", description = "测试接口")
@RequestMapping
public class TestController {

    @Autowired
    private TestApiService testApiService;

    @GetMapping("/buyer/test")
    @Operation(summary = "测试")
    public Result<String> test() {
        log.info("测试接口");
        String res = testApiService.test();
        return Result.success(res);
    }

}
