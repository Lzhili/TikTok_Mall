package com.tiktok.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.tiktok.dto.ShoppingCartDTO;
import com.tiktok.entity.ShoppingCart;
import com.tiktok.result.Result;
import com.tiktok.service.ShoppingCartApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "购物车管理接口", description = "购物车相关接口")
@SaCheckLogin
@RestController
@Slf4j
@RequestMapping(value = "/buyer/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartApiService shoppingCartApiService;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    @Operation(summary = "添加购物车")
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加购物车");
        shoppingCartApiService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 根据productId查询用户该商品的数量
     * @param id
     * @return
     */
    @Operation(summary = "根据productId查询用户该商品的数量")
    @GetMapping("/{id}")
    public Result<Integer> getQuantity(@PathVariable Long id){
        log.info("商品id: {}", id);
        Integer quantity =shoppingCartApiService.getQuantity(id);
        return Result.success(quantity);
    }

    /**
     * 查看购物车列表
     * @return
     */
    @Operation(summary = "查看购物车列表")
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        log.info("查看购物车列表");
        List<ShoppingCart> list = shoppingCartApiService.showShoppingCart();
        return Result.success(list);
    }

    /**
     * 清空购物车列表
     * @return
     */
    @Operation(summary = "清空购物车列表")
    @DeleteMapping("/clean")
    public Result clean(){
        log.info("清空购物车列表");
        shoppingCartApiService.cleanShoppingCart();
        return Result.success();
    }

    /**
     * 获取购物车数量
     * @return
     */
    @Operation(summary = "获取购物车中所有商品总数量")
    @GetMapping("/getCartNum")
    public Result<Integer> getCartNum(){
        log.info("获取购物车中所有商品总数量");
        Integer cartNum = shoppingCartApiService.getCartNum();
        return Result.success(cartNum);
    }
}
