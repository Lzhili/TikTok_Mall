package com.tiktok.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSubmitVO implements Serializable {
    //订单id
    private Long id;

    //订单号（对应orders表number字段）
    private String orderNumber;

    //订单金额（对应orders表amount字段）
    private BigDecimal orderAmount;

    //下单时间
    private LocalDateTime orderTime;
}
