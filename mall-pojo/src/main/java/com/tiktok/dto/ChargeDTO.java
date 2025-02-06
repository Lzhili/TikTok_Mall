package com.tiktok.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ChargeDTO implements Serializable {

    //用户id
    private Long userId;

    //订单id
    private Long orderId;

    //订单号
    private String orderNumber;

    //支付方式 1 微信，2支付宝
    private Integer payMethod;

    //订单金额
    private BigDecimal orderAmount;

}
