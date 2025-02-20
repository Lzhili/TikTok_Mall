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

    /**
     * 根据订单id查询支付单信息
     * @param orderId
     * @return
     */
    @Select("select * from `tiktok-mall`.payment where order_id = #{orderId}")
    Payment queryPaymentByOrderId(Long orderId);

    /**
     * 添加支付单
     * @param payment
     */
    @Insert("insert into `tiktok-mall`.payment (transaction_number, user_id, order_id, order_number, pay_time, pay_method, order_amount, is_paid) " +
            "values (#{transactionNumber}, #{userId}, #{orderId}, #{orderNumber}, #{payTime}, #{payMethod}, #{orderAmount}, #{isPaid})")
    void addPayment(Payment payment);
}
