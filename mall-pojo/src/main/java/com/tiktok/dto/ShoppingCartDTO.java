package com.tiktok.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShoppingCartDTO implements Serializable {
    //用户id
    private Long userId;

    // 商品id
    private Long productId;

    //商品数量
    private Integer quantity;
}
