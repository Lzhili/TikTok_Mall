package com.tiktok.service;

import com.tiktok.context.BaseContext;
import com.tiktok.dto.OrdersSubmitDTO;
import com.tiktok.vo.OrderSubmitVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
public class OrderApiService {

    @DubboReference
    private OrderService orderService;

    /**
     * 用户提交订单
     * @param ordersSubmitDTO
     * @return
     */
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //携带当前用户id
        ordersSubmitDTO.setUserId(BaseContext.getCurrentId());

        return orderService.submitOrder(ordersSubmitDTO);
    }
}
