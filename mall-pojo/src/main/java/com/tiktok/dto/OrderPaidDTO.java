package com.tiktok.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class OrderPaidDTO  implements Serializable {

    //用户id
    private Long userId;

    //订单id
    private Long orderId;

    //支付方式 1 微信，2支付宝
    private Integer payMethod;

    //付款时间
    private LocalDateTime payTime;
}
