package com.tiktok.service;

import com.tiktok.entity.Category;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryApiService {

    @DubboReference
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获得分类列表
     * @return
     */
    public List<Category> list() {

        //生成redis的缓存key
        String cacheKey = "category:list";

        //尝试从redis中获取缓存数据
        List<Category> categoryList = (List<Category>) redisTemplate.opsForValue().get(cacheKey);

        //如果redis中存在缓存数据，则直接返回
        if(categoryList != null && categoryList.size() > 0){
            log.info("Category列表redis缓存命中，直接返回数据。 cacheKey:{}", cacheKey);
            return categoryList;
        }

        //如果redis中不存在缓存数据，则调用dubbo服务去查询数据库，并缓存数据
        categoryList = categoryService.list();

        //将查询的结果缓存到redis中
        redisTemplate.opsForValue().set(cacheKey, categoryList);
        log.info("将查询的Category列表结果缓存到redis中。cacheKey:{}", cacheKey);

        return categoryList;
    }
}
