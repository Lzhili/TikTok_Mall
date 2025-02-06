package com.tiktok.service;

import com.tiktok.dto.ChargeDTO;

public interface PaymentService {
    String charge(ChargeDTO chargeDTO);
}
