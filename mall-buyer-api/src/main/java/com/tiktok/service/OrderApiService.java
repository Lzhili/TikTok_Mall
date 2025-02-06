package com.tiktok.service;

import com.tiktok.context.BaseContext;
import com.tiktok.dto.OrderPaidDTO;
import com.tiktok.dto.OrdersSubmitDTO;
import com.tiktok.vo.OrderSubmitVO;
import com.tiktok.vo.OrderWithDetailVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 用户标记订单为已支付
     * @param orderPaidDTO
     */
    public void markOrderPaid(OrderPaidDTO orderPaidDTO) {
        //携带当前用户id
        orderPaidDTO.setUserId(BaseContext.getCurrentId());

        orderService.markOrderPaid(orderPaidDTO);
    }

    /**
     * 查询当前用户所有订单
     * @return
     */
    public List<OrderWithDetailVO> list() {
        return orderService.list(BaseContext.getCurrentId());
    }
}
