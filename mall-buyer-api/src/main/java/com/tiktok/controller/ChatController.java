package com.tiktok.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.tiktok.constant.ChatAiConstant;
import com.tiktok.result.Result;
import com.tiktok.utils.HttpClientUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SaCheckLogin
@Slf4j
@RestController
@Tag(name = "Chat接口", description = "Chat接口")
@RequestMapping(value = "/buyer/chatAi")
public class ChatController {

    /**
     * ChatClient 简单调用
     */
    @GetMapping("/simple/chat")
    @Operation(summary = "ChatClient 简单调用")
    public Result<String> simpleChat(@RequestParam(value = "query", defaultValue = ChatAiConstant.DEFAULT_QUERY)String query) {
        log.info("ChatClient 简单调用");

        // 目标URL
        String url = ChatAiConstant.CHAT_AI_URL;
        // 创建参数映射
        Map<String, String> paramMap = new HashMap<>();
        // 添加名为query的参数
        paramMap.put("query", query);

        // 调用doGet方法发送请求
        String chatResponse = HttpClientUtil.doGet(url, paramMap);

        return Result.success(chatResponse);
    }

}
