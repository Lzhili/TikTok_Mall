package com.tiktok.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductPageQueryDTO implements Serializable {

    //页码
    private int page;

    //每页记录数
    private int pageSize;

    //商品名称
    private String name;

    //分类id
    private Integer categoryId;
}
