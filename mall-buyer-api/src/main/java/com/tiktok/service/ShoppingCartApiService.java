package com.tiktok.service;

import com.tiktok.context.BaseContext;
import com.tiktok.dto.ShoppingCartDTO;
import com.tiktok.dto.ShoppingCartQuantityDTO;
import com.tiktok.entity.ShoppingCart;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ShoppingCartApiService {

    @DubboReference
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //获取当前用户id
        Long userId = BaseContext.getCurrentId();
        shoppingCartDTO.setUserId(userId);

        log.info("添加购物车:{} ", shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);
    }

    /**
     * 根据productId查询用户该商品的数量
     * @param productId
     * @return
     */
    public Integer getQuantity(Long productId) {
        //获取当前用户id
        Long userId = BaseContext.getCurrentId();
        ShoppingCartQuantityDTO shoppingCartQuantityDTO = ShoppingCartQuantityDTO.builder()
                .userId(userId)
                .productId(productId)
                .build();
        log.info("查询用户该商品的数量:{}", shoppingCartQuantityDTO);
        return shoppingCartService.getQuantity(shoppingCartQuantityDTO);
    }

    /**
     * 查看购物车列表
     * @return
     */
    public List<ShoppingCart> showShoppingCart() {
        //获取当前用户id
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(userId)
                .build();

        List<ShoppingCart> list = shoppingCartService.showShoppingCart(shoppingCart);
        return list;
    }

    /**
     * 清空购物车
     */
    public void cleanShoppingCart() {
        //获取当前用户id
        Long userId = BaseContext.getCurrentId();
        //清除当前用户的购物车数据
        shoppingCartService.deleteByUserId(userId);
    }

    /**
     * 获取购物车数量
     * @return
     */
    public Integer getCartNum() {
        //获取当前用户id
        Long userId = BaseContext.getCurrentId();

        Integer cartNum = shoppingCartService.getCartNum(userId);
        return cartNum;
    }
}
