package com.tiktok.service;

import com.tiktok.dto.OrderPaidDTO;
import com.tiktok.dto.OrdersSubmitDTO;
import com.tiktok.entity.Orders;
import com.tiktok.vo.OrderSubmitVO;
import com.tiktok.vo.OrderWithDetailVO;

import java.util.List;

public interface OrderService {
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    void markOrderPaid(OrderPaidDTO orderPaidDTO);

    List<OrderWithDetailVO> list(Long userId);

    Orders getOrderById(Long id);

    void cancelOrder(Long orderId);

    Orders getOrderByOrderNo(String orderNumber);
}
