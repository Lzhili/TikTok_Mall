package com.tiktok.mapper;

import com.tiktok.dto.OrderPaidDTO;
import com.tiktok.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    /**
     * 插入订单
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单id修改订单状态为已支付
     * @param orderPaidDTO
     */
    void markOrderPaidById(OrderPaidDTO orderPaidDTO);
}
