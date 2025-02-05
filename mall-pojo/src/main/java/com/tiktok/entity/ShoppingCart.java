package com.tiktok.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //商品名称
    private String name;

    //图片
    private String picture;

    //用户id
    private Long userId;

    //商品id
    private Long productId;

    //数量
    private Integer quantity;

    //金额(单价)
    private BigDecimal amount;

    //状态 1:有效 0:无效
    private Integer status;

    private LocalDateTime createTime;
}
