package com.tiktok.listener;

import com.tiktok.dto.OrderPaidDTO;
import com.tiktok.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PayStatusListener {

    private final OrderService orderService; //依赖注入orderService

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "order.pay.success.queue", durable = "true"),
            exchange = @Exchange(name = "pay.direct", type = "direct"), //默认是direct交换机
            key = "pay.success"  //BindKey
    ))
    public void listenPaySuccess(OrderPaidDTO orderPaidDTO){
        log.info("监听到支付成功的异步通知消息");
        orderService.markOrderPaid(orderPaidDTO);
    }
}
