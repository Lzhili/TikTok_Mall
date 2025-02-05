package com.tiktok.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ShoppingCartQuantityDTO implements Serializable {

    //用户id
    private Long userId;

    // 商品id
    private Long productId;
}
