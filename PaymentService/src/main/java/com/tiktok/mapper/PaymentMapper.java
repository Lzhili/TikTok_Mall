package com.tiktok.mapper;

import com.tiktok.dto.ChargeDTO;
import com.tiktok.dto.OrderPaidDTO;
import com.tiktok.entity.Orders;
import com.tiktok.entity.Payment;
import com.tiktok.vo.OrderWithDetailVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PaymentMapper {

    @Select("select * from `tiktok-mall`.payment where order_id = #{orderId}")
    Payment queryPaymentByOrderId(Long orderId);


}
