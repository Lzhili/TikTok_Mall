package com.tiktok.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductDTO implements Serializable {
    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品对应的分类id
     */
    private Long categoryId = (long)1;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 图片路径
     */
    private String picture;

    /**
     * 描述信息
     */
    private String description;
}
