package com.tiktok.service.impl;

import com.tiktok.constant.PayMethodConstant;
import com.tiktok.dto.ChargeDTO;
import com.tiktok.entity.Orders;
import com.tiktok.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.UUID;

@Slf4j
//@Transactional, 加上interfaceClass事务注解才能生效
@DubboService(interfaceClass = PaymentService.class)
public class PaymentServiceImpl implements PaymentService {

    /**
     * 模拟支付
     * @param chargeDTO
     * @return
     */
    @Override
    public String charge(ChargeDTO chargeDTO) {
        // 模拟支付过程（可以使用第三方库进行验证）
        // 这里只是简单示例，没有实际验证逻辑

        log.info("用户id：{}，支付方式：{}，订单号：{}，支付金额为：{}",
                chargeDTO.getUserId(),
                chargeDTO.getPayMethod().equals(Orders.WECHAT) ? PayMethodConstant.WECHAT : PayMethodConstant.ALIPAY,
                chargeDTO.getOrderNumber(),
                chargeDTO.getOrderAmount());

        //支付成功，生成交易号返回
        String transactionNumber = UUID.randomUUID().toString();

        return transactionNumber;
    }
}
