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
public class OrderWithDetailVO implements Serializable {
    //orders表
    //订单id
    private Long id;

    //订单号
    private String number;

    //下单用户id
    private Long userId;

    //地址id
    private Long addressBookId;

    //下单时间
    private LocalDateTime orderTime;

    //付款时间
    private LocalDateTime payTime;

    //支付方式 1 微信，2支付宝
    private Integer payMethod;

    //订单总金额
    private BigDecimal amount;

    //支付状态 0未支付 1已支付
    private Integer isPaid;

    //用户名
    private String username;

    //邮箱
    private String email;

    //完整详细地址
    private String addressDetail;

    //order_detail表
    //商品id
    private Long productId;

    //商品名称
    private String name;

    //商品图片
    private String picture;

    //商品数量
    private Integer quantity;

    //商品单价(amount修改为price)
    private BigDecimal price;
}
