package com.tiktok.service;

import com.tiktok.dto.ChargeDTO;
import com.tiktok.entity.Payment;

public interface PaymentService {
    String charge(ChargeDTO chargeDTO);

    Payment queryPaymentByOrderId(Long orderId);

    void addPayment(Payment payment);
}
