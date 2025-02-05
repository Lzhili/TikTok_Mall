package com.tiktok.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单明细
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //商品名称
    private String name;

    //商品图片
    private String picture;

    //订单id
    private Long orderId;

    //商品id
    private Long productId;

    //商品数量
    private Integer quantity;

    //商品单价
    private BigDecimal amount;
}
