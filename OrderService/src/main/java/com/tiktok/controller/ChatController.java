package com.tiktok.controller;


import com.tiktok.constant.ChatAiConstant;
import com.tiktok.service.ChatAssistant;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatAssistant agent;

    public ChatController(ChatAssistant agent) {
        this.agent = agent;
    }

    /**
     * ChatClient 简单调用
     */
    @GetMapping("/simple/chat")
    public String simpleChat(@RequestParam(value = "query", defaultValue = ChatAiConstant.DEFAULT_QUERY) String query) {
        log.info("ChatClient 简单调用 {}", query);
        return agent.simpleChat(query);
    }

    /**
     * ChatClient 流式调用
     */
    @GetMapping("/stream/chat")
    public Flux<String> streamChat(@RequestParam(value = "query", defaultValue = ChatAiConstant.DEFAULT_QUERY) String query, HttpServletResponse response) {
        log.info("ChatClient 流式调用 {}", query);
        response.setCharacterEncoding("UTF-8");
        return agent.streamChat(query, response);
    }

}
