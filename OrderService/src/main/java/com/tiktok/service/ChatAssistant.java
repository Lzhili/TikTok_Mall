package com.tiktok.service;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.tiktok.context.BaseContext;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.Map;


@Service
public class ChatAssistant {

//    private static final String DEFAULT_PROMPT = "你是一个博学的智能聊天助手，请根据用户提问回答！";

    private static final String DEFAULT_PROMPT = "您是“TikTok“抖音电商的客户聊天支持代理。请以友好、乐于助人且愉快的方式来回复。\n" +
            "您正在通过在线聊天系统与客户互动。\n" +
            "您能够支持已有订单的根据订单号查询订单等操作，其余功能将在后续版本中添加，如果用户问的问题不支持请告知详情。\n" +
            "在提供有关根据订单号查询订单等操作之前，您必须始终从用户处获取以下信息：订单号。\n" +
            "在询问用户之前，请检查消息历史记录以获取订单号等信息，尽量避免重复询问给用户造成困扰。\n" +
            "如果需要，您可以调用相应函数辅助完成。\n" +
            "请讲中文。\n" +
            "和你对话的用户id是 {user_id}.\n" +
            "今天的日期是 {current_date}.";

    private final ChatClient chatClient;

    // 也可以使用如下的方式注入 ChatClient
    public ChatAssistant(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem(DEFAULT_PROMPT)
                // 实现 Chat Memory 的 Advisor
                // 在使用 Chat Memory 时，需要指定对话 ID，以便 Spring AI 处理上下文。
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(new InMemoryChatMemory())
                )
                // 实现 Logger 的 Advisor
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                // 设置 ChatClient 中 ChatModel 的 Options 参数
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .withTopP(0.7)
                                .build()
                )
                // FUNCTION CALLING
                .defaultFunctions("getOrderByOrderNo")
                .build();
    }

    /**
     * ChatClient 简单调用
     */
    public String simpleChat(String query) {
        Map<String, Object> p = Map.of("current_date", LocalDate.now().toString(), "user_id", BaseContext.getCurrentId());
        return chatClient.prompt(query)
//                .system(s -> s.param("current_date", LocalDate.now().toString()))
//                .system(s -> s.param("user_id", BaseContext.getCurrentId()))
                .system(s -> s.params(p))
                .call().content();
    }

    /**
     * ChatClient 流式调用
     */
    public Flux<String> streamChat(String query, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        return chatClient.prompt(query)
                .system(s -> s.param("current_date", LocalDate.now().toString()))
                .stream().content();
    }
}
