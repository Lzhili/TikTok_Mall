package com.tiktok.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrdersSubmitDTO implements Serializable {
    //用户id
    private Long userId;

    //地址簿id
    private Long addressBookId;

    //订单总金额
    private BigDecimal amount;
}
