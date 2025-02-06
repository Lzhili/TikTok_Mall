package com.tiktok.controller;

import com.tiktok.dto.ChargeDTO;
import com.tiktok.result.Result;
import com.tiktok.service.PaymentApiService;
import com.tiktok.vo.ChargeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
