package com.tiktok.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 地址簿
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //用户id
    private Long userId;

    //用户名/收货人
    private String username;

    //邮箱
    private String email;

    //详细地址
    private String detail;

    //市级名称
    private String city;

    //省级名称
    private String province;

    //国家名称
    private String country;

    //标签（家/公司/学校）
    private String label;

    //邮政编码
    private String zipCode;

}