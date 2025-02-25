package com.tiktok.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.tiktok.dto.ChargeDTO;
import com.tiktok.entity.Payment;
import com.tiktok.result.Result;
import com.tiktok.service.PaymentApiService;
import com.tiktok.vo.ChargeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "支付接口", description = "支付接口")
@RequestMapping("/buyer/payment")
public class PaymentController {

    @Autowired
    private PaymentApiService paymentApiService;

    /**
     * 用户支付订单
     * @param chargeDTO
     * @return
     */
    @Operation(summary = "用户支付订单")
    @PostMapping("/charge")
    public Result<ChargeVO> charge(@RequestBody ChargeDTO chargeDTO){
        log.info("用户支付订单：{}", chargeDTO);
        ChargeVO chargeVO = paymentApiService.charge(chargeDTO);
        return Result.success(chargeVO);
    }

    /**
     * 根据订单id查询支付单信息
     * @param orderId
     * @return
     */
    @Operation(summary = "根据订单id查询支付单信息")
    @GetMapping("/payOrder/{id}")
    public Result<Payment> queryPaymentByOrderId(@PathVariable("id") Long orderId){
        log.info("根据订单id查询支付单信息：{}", orderId);
        Payment payment = paymentApiService.queryPaymentByOrderId(orderId);
        return Result.success(payment);
    }

}
