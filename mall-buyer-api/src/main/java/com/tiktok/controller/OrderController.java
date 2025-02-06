package com.tiktok.controller;


import com.tiktok.dto.OrderPaidDTO;
import com.tiktok.dto.OrdersSubmitDTO;
import com.tiktok.result.Result;
import com.tiktok.service.OrderApiService;
import com.tiktok.vo.OrderSubmitVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "订单接口", description = "订单簿相关接口")
@RestController
@Slf4j
@RequestMapping(value = "/buyer/order")
public class OrderController {

    @Autowired
    private OrderApiService orderApiService;

    /**
     * 用户提交订单
     * @param ordersSubmitDTO
     * @return
     */
    @Operation(summary = "用户提交订单")
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户提交订单：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderApiService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 用户标记订单为已支付
     * @param orderPaidDTO
     * @return
     */
    @Operation(summary = "用户标记订单为已支付")
    @PutMapping("/paid")
    public Result markOrderPaid(@RequestBody OrderPaidDTO orderPaidDTO){
        log.info("用户标记订单为已支付");
        orderApiService.markOrderPaid(orderPaidDTO);
        return Result.success();
    }
}
