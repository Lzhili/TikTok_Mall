package com.tiktok.service.impl;

import com.tiktok.constant.PayMethodConstant;
import com.tiktok.dto.ChargeDTO;
import com.tiktok.dto.OrderPaidDTO;
import com.tiktok.entity.Orders;
import com.tiktok.service.OrderService;
import com.tiktok.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Slf4j
//@Transactional, 加上interfaceClass事务注解才能生效
@GlobalTransactional
@DubboService(interfaceClass = PaymentService.class)
public class PaymentServiceImpl implements PaymentService {

    @DubboReference
    private OrderService orderService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 模拟支付
     * @param chargeDTO
     * @return
     */
    @Override
    public String charge(ChargeDTO chargeDTO) {
        // 模拟支付过程（可以使用第三方库进行验证）
        // 这里只是简单示例，没有实际验证逻辑
        // 实际情况下，应该会插入一条交易数据到交易表
        log.info("用户id：{}，支付方式：{}，订单号：{}，支付金额为：{}",
                chargeDTO.getUserId(),
                chargeDTO.getPayMethod().equals(Orders.WECHAT) ? PayMethodConstant.WECHAT : PayMethodConstant.ALIPAY,
                chargeDTO.getOrderNumber(),
                chargeDTO.getOrderAmount());

        //支付成功，生成交易号返回
        String transactionNumber = UUID.randomUUID().toString();

//        //用户标记订单为已支付（同步调用）
//        OrderPaidDTO orderPaidDTO = new OrderPaidDTO();
//        BeanUtils.copyProperties(chargeDTO, orderPaidDTO);
//        orderService.markOrderPaid(orderPaidDTO);

        //用户标记订单为已支付（异步通知）
        //发送消息这个分支事务作为Seata分布式事务的参与者RM（原先是整个同步调用流程），
        //当全局事务的一阶段完成，这个MQ消息会根据二阶段要求commit/rollback进行消息的提交或撤回，在此之前消息不会被消费。
        //举个异常例子，如果不加@GlobalTransactional注解，如果后续的发送短信等其他业务出错，发送到MQ的消息无法撤回。
        OrderPaidDTO orderPaidDTO = new OrderPaidDTO();
        BeanUtils.copyProperties(chargeDTO, orderPaidDTO);
        try {
            rabbitTemplate.convertAndSend("pay.direct", "pay.success", orderPaidDTO);
            log.info("支付成功的异步通知消息发送成功，订单id：{}， 订单号：{}", chargeDTO.getOrderId(), chargeDTO.getOrderNumber());
        }catch (Exception e){
            log.error("支付成功的消息发送失败，订单id：{}， 订单号：{}", chargeDTO.getOrderId(), chargeDTO.getOrderNumber(), e);
        }

        return transactionNumber;
    }
}
