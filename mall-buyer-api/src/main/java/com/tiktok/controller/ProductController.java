package com.tiktok.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.tiktok.dto.ProductPageQueryDTO;
import com.tiktok.result.PageResult;
import com.tiktok.result.Result;
import com.tiktok.service.ProductApiService;
import com.tiktok.vo.ProductVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SaCheckLogin
@Slf4j
@RestController
@Tag(name = "商品接口", description = "商品接口")
@RequestMapping("/buyer/product")
public class ProductController {

    @Autowired
    private ProductApiService productApiService;

    /**
     * 商品分页查询
     * @param productPageQueryDTO
     * @return
     */
    @Operation(summary = "商品分页查询")
    @GetMapping("/page")
    public Result<PageResult> listProducts(@ParameterObject ProductPageQueryDTO productPageQueryDTO){
        log.info("商品分页查询 {}", productPageQueryDTO);
        PageResult pageResult = productApiService.pageQuery(productPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据id回显商品数据
     * @param id
     * @return
     */
    @Operation(summary = "根据id回显商品数据")
    @GetMapping("/{id}")
    public Result<ProductVO> getProductById(@PathVariable Long id){
        log.info("根据id:{}回显商品数据", id);
        ProductVO productVO = productApiService.getProductById(id);
        return Result.success(productVO);
    }
}
