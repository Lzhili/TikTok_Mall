package com.tiktok.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tiktok.dto.OrdersSubmitDTO;
import com.tiktok.entity.AddressBook;
import com.tiktok.entity.Orders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.core.NestedExceptionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Configuration
public class OrdersTools {

    private static final Logger logger = LoggerFactory.getLogger(OrdersTools.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private ChatService chatService;

    //请求
    public record getOrderRequest(String orderNumber) {
    }

    public record userIdRequest(Long userId) {
    }

    public record submitOrderRequest(Long userId, Long addressBookId) {
    }

    //响应
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record OrdersResponse(Long id, String number, Long userId, Long addressBookId, LocalDateTime orderTime, LocalDateTime payTime,
                                 Integer payMethod, BigDecimal amount, Integer isPaid, String username, String email, String addressDetail)  {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record shoppingCartAmountResponse(BigDecimal amount)  {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record addressListResponse(List<AddressBook> addressBookList)  {
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

    @Bean
    @Description("根据用户id查询购物车总金额")
    public Function<userIdRequest, shoppingCartAmountResponse> getShoppingCartAmount() {
        logger.info("Function Calling: 根据用户id查询购物车总金额");
        return request -> {
            try {
                BigDecimal amount = chatService.getShoppingCartAmount(request.userId());
                return new shoppingCartAmountResponse(amount);
            }
            catch (Exception e) {
                logger.warn("shoppingCart Amount: {}", NestedExceptionUtils.getMostSpecificCause(e).getMessage());
                return new shoppingCartAmountResponse(BigDecimal.valueOf(0));
            }
        };
    }

    @Bean
    @Description("根据用户id查询用户已有的地址簿列表")
    public Function<userIdRequest, addressListResponse> getAddressList() {
        logger.info("Function Calling: 根据用户id查询用户已有的地址簿列表");
        return request -> {
            try {
                List<AddressBook> addressBookList = chatService.getAddressList(request.userId());
                return new addressListResponse(addressBookList);
            }
            catch (Exception e) {
                logger.warn("addressBook List: {}", NestedExceptionUtils.getMostSpecificCause(e).getMessage());
                return new addressListResponse(null);
            }
        };
    }

    @Bean
    @Description("用户下单")
    public Function<submitOrderRequest, String> submitOrder() {
        logger.info("Function Calling: 用户下单");
        return request -> {
            try {
                //1.检查用户的购物车是否为空
                BigDecimal amount = chatService.getShoppingCartAmount(request.userId());
                if (amount.compareTo(BigDecimal.valueOf(0)) == 0) {
                    return "用户购物车为空，请先添加商品到购物车！";
                }
                //2.检查用户输入的地址id是否存在
                List<AddressBook> addressBookList = chatService.getAddressList(request.userId());
                if (addressBookList.isEmpty() || addressBookList.stream().noneMatch(addressBook -> addressBook.getId() == request.addressBookId())){
                    return "用户输入的地址id不存在或者地址簿为空！";
                }
                //3.自动下单
                orderService.submitOrder(new OrdersSubmitDTO(request.userId(), request.addressBookId(), amount));
                return "用户下单成功，请尽快去支付！";
            }
            catch (Exception e) {
                logger.warn("submit order: {}", NestedExceptionUtils.getMostSpecificCause(e).getMessage());
                return "用户下单失败";
            }
        };
    }
}
