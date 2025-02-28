package com.tiktok.service;


import com.tiktok.dto.OrdersSubmitDTO;
import com.tiktok.entity.AddressBook;
import com.tiktok.entity.Orders;

import java.math.BigDecimal;
import java.util.List;

public interface ChatService {

    BigDecimal getShoppingCartAmount(Long userId);

    List<AddressBook> getAddressList(Long userId);

    Orders queryOrderByOrderNo(String orderNumber);

    void autoSubmitOrder(OrdersSubmitDTO ordersSubmitDTO);
}
