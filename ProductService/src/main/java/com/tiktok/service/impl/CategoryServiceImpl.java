package com.tiktok.service.impl;

import com.tiktok.entity.Category;
import com.tiktok.mapper.CategoryMapper;
import com.tiktok.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Slf4j
//@Transactional, 加上interfaceClass事务注解才能生效
@DubboService(interfaceClass = CategoryService.class)
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 获取分类列表
     * @return
     */
    @Override
    public List<Category> list() {

        List<Category> categoryList = categoryMapper.list();

        return categoryList;
    }
}
