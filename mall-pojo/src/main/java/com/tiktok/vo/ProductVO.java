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
public class ProductVO implements Serializable {

    private Long id;

    //商品名称
    private String name;

    //对应的商品分类id
    private Long categoryId;

    //分类名称
    private String categoryName;

    //商品价格
    private BigDecimal price;

    //图片
    private String picture;

    //描述信息
    private String description;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
