package com.tiktok.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersSubmitDTO implements Serializable {
    //用户id
    private Long userId;

    //地址簿id
    private Long addressBookId;

    //订单总金额
    private BigDecimal amount;
}
