package com.tiktok.listener;

import com.tiktok.constant.MQConstant;
import com.tiktok.dto.OrderPaidDTO;
import com.tiktok.entity.Orders;
import com.tiktok.entity.Payment;
import com.tiktok.service.OrderService;
import com.tiktok.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderDelayMessageListener {

    private final OrderService orderService;

    @DubboReference(lazy = true)
    private PaymentService paymentService;

    @GlobalTransactional
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MQConstant.DELAY_ORDER_QUEUE_NAME),
            exchange = @Exchange(name = MQConstant.DELAY_EXCHANGE_NAME, delayed = "true"),
            key = MQConstant.DELAY_ORDER_KEY
    ))
    public void listenOrderDelayMessage(Long orderId){
        // 1.查询订单
        log.info("监听到异步延迟消息消息，订单id:{}", orderId);
        Orders order = orderService.getOrderById(orderId);

        //2.检查订单状态，判断是否已经支付
        if(order == null || order.getIsPaid() == Orders.PAID){
            return; //订单不存在或者已经支付，直接返回
        }

        //3.未支付，需要查询payment数据库中订单id对应的支付单记录的支付状态（rpc调用）
        Payment payment = paymentService.queryPaymentByOrderId(orderId);

        //4.判断payment中是否支付
        if(payment != null && payment.getIsPaid() == Payment.PAID){
            //4.1 payment中已经支付，标记订单状态为已经支付，同时更新支付方式
            OrderPaidDTO orderPaidDTO = new OrderPaidDTO();
            orderPaidDTO.setOrderId(orderId); //设置订单id
            orderPaidDTO.setPayMethod(payment.getPayMethod()); //设置订单支付方式
            orderPaidDTO.setPayTime(payment.getPayTime()); //设置付款时间

            orderService.markOrderPaid(orderPaidDTO);
        }else{
            // 4.2 取消订单，标记订单为订单超时状态
            orderService.cancelOrder(orderId);
        }
    }
}
