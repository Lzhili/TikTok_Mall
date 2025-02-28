package com.tiktok.service.impl;

import com.tiktok.dto.OrdersSubmitDTO;
import com.tiktok.entity.AddressBook;
import com.tiktok.entity.Orders;
import com.tiktok.service.AddressBookService;
import com.tiktok.service.ChatService;
import com.tiktok.service.OrderService;
import com.tiktok.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Slf4j
//@Transactional, 加上interfaceClass事务注解才能生效
@DubboService(interfaceClass = ChatService.class)
@Service
public class ChatServiceImpl implements ChatService {

    @DubboReference
    private ShoppingCartService shoppingCartService;

    @DubboReference
    private AddressBookService addressBookService;

    @Autowired
    private OrderService orderService;

    /**
     * 获取购物车总金额
     * @param userId
     * @return
     */
    @Override
    @GlobalTransactional
    public BigDecimal getShoppingCartAmount(Long userId) {
        return shoppingCartService.getAmount(userId);
    }

    /**
     * 获取地址列表
     * @param userId
     * @return
     */
    @Override
    @GlobalTransactional
    public List<AddressBook> getAddressList(Long userId) {
        return addressBookService.list(userId);
    }

    /**
     * 根据订单号查询订单
     * @param orderNumber
     * @return
     */
    @Override
    public Orders queryOrderByOrderNo(String orderNumber) {
        return orderService.getOrderByOrderNo(orderNumber);
    }

    /**
     * 自动提交订单
     * @param ordersSubmitDTO
     */
    @Override
    public void autoSubmitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        orderService.submitOrder(ordersSubmitDTO);
    }
}
