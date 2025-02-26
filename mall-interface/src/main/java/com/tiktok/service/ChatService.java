package com.tiktok.service;


import com.tiktok.entity.AddressBook;

import java.math.BigDecimal;
import java.util.List;

public interface ChatService {

    BigDecimal getShoppingCartAmount(Long userId);

    List<AddressBook> getAddressList(Long userId);
}
