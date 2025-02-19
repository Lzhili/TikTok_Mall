package com.tiktok.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment implements Serializable {

    /**
     * 支付状态 0未支付 1已支付 2订单超时
     */
    public static final Integer UN_PAID = 0;
    public static final Integer PAID = 1;
    public static final Integer TIMEOUT = 2;

    /**
     * 支付方式 1 微信（默认方式） 2 支付宝
     */
    public static final Integer WECHAT = 1;
    public static final Integer ALIPAY = 2;

    private static final long serialVersionUID = 1L;

    private Long id;

    //交易号
    private String transactionNumber;

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

    //支付状态 0未支付 1已支付 2订单超时
    private Integer isPaid;

}
