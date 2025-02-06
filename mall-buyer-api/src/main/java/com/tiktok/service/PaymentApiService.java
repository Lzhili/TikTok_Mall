package com.tiktok.service;

import com.tiktok.context.BaseContext;
import com.tiktok.dto.ChargeDTO;
import com.tiktok.vo.ChargeVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentApiService {

    @DubboReference
    private PaymentService paymentService;

    /**
     * 用户支付订单
     * @param chargeDTO
     * @return
     */
    public ChargeVO charge(ChargeDTO chargeDTO) {
        //获取当前用户id
        Long userId = BaseContext.getCurrentId();
        chargeDTO.setUserId(userId);

        //完成支付，获得交易号
        String transactionNumber = paymentService.charge(chargeDTO);

        //封装为VO返回
        ChargeVO chargeVO = ChargeVO.builder()
                .transactionNumber(transactionNumber)
                .build();

        return chargeVO;
    }
}
