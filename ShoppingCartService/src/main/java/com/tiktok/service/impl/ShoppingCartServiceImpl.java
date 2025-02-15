package com.tiktok.service.impl;

import com.tiktok.dto.ShoppingCartDTO;
import com.tiktok.dto.ShoppingCartQuantityDTO;
import com.tiktok.entity.Product;
import com.tiktok.entity.ShoppingCart;
import com.tiktok.mapper.ShoppingCartMapper;
import com.tiktok.service.ProductService;
import com.tiktok.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
//@Transactional, 加上interfaceClass事务注解才能生效
@DubboService(interfaceClass = ShoppingCartService.class)
public class ShoppingCartServiceImpl implements ShoppingCartService{
    
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @DubboReference
    private ProductService productService;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Override
    @GlobalTransactional
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //判断当前用户购物车中是否有该商品
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        //如果购物车存在，则更新该条购物车记录
        if(list != null && list.size() > 0){
            ShoppingCart cart = list.get(0); //只能查到一条购物车记录
            cart.setQuantity(shoppingCart.getQuantity());
            shoppingCartMapper.updateById(cart);
        }
        else{  //如果购物车不存在，则插入该条购物车记录
            Product product = productService.getProductById(shoppingCart.getProductId());
            shoppingCart.setName(product.getName());
            shoppingCart.setPicture(product.getPicture());
            shoppingCart.setAmount(product.getPrice());
            shoppingCart.setStatus(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);  //当用于测试线程隔离和熔断降级时，注释掉该行。
        }

    }

    /**
     * 根据productId查询用户该商品的数量
     * @param shoppingCartQuantityDTO
     * @return
     */
    @Override
    public Integer getQuantity(ShoppingCartQuantityDTO shoppingCartQuantityDTO) {
        //判断当前用户购物车中是否有该商品
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartQuantityDTO, shoppingCart);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        //如果存在该商品，则返回该商品数量
        if(list != null && list.size() > 0){
            return list.get(0).getQuantity();
        }
        else {//否则返回null
            return null;
        }
    }

    /**
     * 根据userId展示购物车列表
     * @return
     */
    @Override
    public List<ShoppingCart> showShoppingCart(ShoppingCart shoppingCart) {
        return shoppingCartMapper.list(shoppingCart);
    }

    /**
     * 根据userId删除购物车
     * @param userId
     */
    @Override
    public void deleteByUserId(Long userId) {
        shoppingCartMapper.deleteByUserId(userId);
    }

    /**
     * 根据userId获取购物车所有商品总数量
     * @param userId
     * @return
     */
    @Override
    public Integer getCartNum(Long userId) {
        //根据用户id查询购物车
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(userId)
                .build();
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        //计算购物车中所有商品总数量
        Integer cartNum = 0;
        for(ShoppingCart cart : list){
            cartNum += cart.getQuantity();
        }
        return cartNum;
    }
}
