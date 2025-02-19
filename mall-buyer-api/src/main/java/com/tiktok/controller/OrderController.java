package com.tiktok.controller;


import com.tiktok.dto.OrderPaidDTO;
import com.tiktok.dto.OrdersSubmitDTO;
import com.tiktok.result.Result;
import com.tiktok.service.OrderApiService;
import com.tiktok.vo.OrderSubmitVO;
import com.tiktok.vo.OrderVO;
import com.tiktok.vo.OrderWithDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 查询当前登录用户的所有订单
     * @return
     */
    @Operation(summary = "查询当前登录用户的所有订单")
    @GetMapping("/list")
    public Result<List<OrderWithDetailVO>> list(){
        log.info("查询当前登录用户的所有订单");
        List<OrderWithDetailVO> list = orderApiService.list();
        return Result.success(list);
    }

    /**
     * 根据id查询单条订单
     * @param id
     * @return
     */
    @Operation(summary = "根据id查询单条订单")
    @GetMapping("/{id}")
    public Result<OrderVO> getOrderById(@PathVariable Long id){
        log.info("根据订单id:{} 查询单条订单", id);
        OrderVO orderVO = orderApiService.getOrderById(id);
        return Result.success(orderVO);
    }
}
