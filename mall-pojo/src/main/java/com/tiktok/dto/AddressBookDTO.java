package com.tiktok.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddressBookDTO implements Serializable {
    private Long userId;    //用户id
    private String username; // 用户名/收货人
    private String email; // 邮箱
    private String detail; // 详细地址
    private String city; // 市名称
    private String province; // 省名称
    private String country; // 国家名称
    private String label; // 标签（家/公司/学校）
    private String zipCode; // 邮政编码
}
