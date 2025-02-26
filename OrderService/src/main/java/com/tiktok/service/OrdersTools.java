package com.tiktok.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tiktok.entity.Orders;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.core.NestedExceptionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.Function;

@Configuration
public class OrdersTools {

    private static final Logger logger = LoggerFactory.getLogger(OrdersTools.class);

    @Autowired
    private OrderService orderService;

    //请求
    public record getOrderRequest(String orderNumber) {
    }

    //响应
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record OrdersResponse(Long id, String number, Long userId, Long addressBookId, LocalDateTime orderTime, LocalDateTime payTime,
                                 Integer payMethod, BigDecimal amount, Integer isPaid, String username, String email, String addressDetail)  {
    }

    @Bean
    @Description("根据订单号查询订单")
    public Function<getOrderRequest, OrdersResponse> getOrderByOrderNo() {
        logger.info("Function Calling: 根据订单号查询订单");
        return request -> {
            try {
                Orders entityOrders = orderService.getOrderByOrderNo(request.orderNumber());
                return new OrdersResponse(
                        entityOrders.getId(),
                        entityOrders.getNumber(),
                        entityOrders.getUserId(),
                        entityOrders.getAddressBookId(),
                        entityOrders.getOrderTime(),
                        entityOrders.getPayTime(),
                        entityOrders.getPayMethod(),
                        entityOrders.getAmount(),
                        entityOrders.getIsPaid(),
                        entityOrders.getUsername(),
                        entityOrders.getEmail(),
                        entityOrders.getAddressDetail()
                );
            }
            catch (Exception e) {
                logger.warn("Orders details: {}", NestedExceptionUtils.getMostSpecificCause(e).getMessage());
                return new OrdersResponse(null, request.orderNumber(),null, null, null, null,
                        null, null, null, null, null,null);
            }
        };
    }
}
