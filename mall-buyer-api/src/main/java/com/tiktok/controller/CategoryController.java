package com.tiktok.controller;


import com.tiktok.entity.Category;
import com.tiktok.result.Result;
import com.tiktok.service.CategoryApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "分类管理接口", description = "分类相关接口")
@RestController
@Slf4j
@RequestMapping(value = "/buyer/category")
public class CategoryController {

    @Autowired
    private CategoryApiService categoryApiService;

    /**
     * 获得分类列表
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "获得分类列表")
    public Result<List<Category>> list(){
        log.info("获取分类列表");
        List<Category> list =  categoryApiService.list();
        return Result.success(list);
    }
}
