package com.tiktok.service;

import com.tiktok.dto.ShoppingCartDTO;
import com.tiktok.dto.ShoppingCartQuantityDTO;
import com.tiktok.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    Integer getQuantity(ShoppingCartQuantityDTO shoppingCartQuantityDTO);

    List<ShoppingCart> showShoppingCart(ShoppingCart shoppingCart);

    void deleteByUserId(Long userId);

    Integer getCartNum(Long userId);
}
