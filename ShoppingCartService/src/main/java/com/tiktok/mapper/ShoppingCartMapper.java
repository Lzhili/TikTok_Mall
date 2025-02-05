package com.tiktok.mapper;

import com.tiktok.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 动态条件查询
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 根据id修改购物车商品数量
     * @param shoppingCart
     */
    @Update("update `tiktok-mall`.shopping_cart " +
            "set quantity = #{quantity} " +
            "where id = #{id}")
    void updateById(ShoppingCart shoppingCart);

    /**
     * 插入购物车数据
     * @param shoppingCart
     */
    @Insert("insert into `tiktok-mall`.shopping_cart (name, picture, user_id, product_id, quantity, amount, status, create_time)" +
            " values (#{name}, #{picture}, #{userId}, #{productId}, #{quantity}, #{amount}, #{status}, #{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 根据用户id清空购物车数据
     * @param userId
     */
    @Delete("delete from `tiktok-mall`.shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);
}
