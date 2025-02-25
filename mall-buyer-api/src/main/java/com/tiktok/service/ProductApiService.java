package com.tiktok.service;

import com.tiktok.dto.ProductDTO;
import com.tiktok.dto.ProductPageQueryDTO;
import com.tiktok.entity.Product;
import com.tiktok.result.PageResult;
import com.tiktok.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class ProductApiService {

    @DubboReference
    private ProductService productService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 商品分页查询
     * @param productPageQueryDTO
     * @return
     */
    public PageResult pageQuery(ProductPageQueryDTO productPageQueryDTO) {
        //如果搜索框中没有搜索内容，则使用redis缓存
        if (productPageQueryDTO.getName() == null || productPageQueryDTO.getName() == ""){
            //生成redis的缓存key
            String cacheKey = generateCacheKey(productPageQueryDTO);

            //尝试从redis中获取缓存数据
            PageResult cacheResult = (PageResult) redisTemplate.opsForValue().get(cacheKey);

            //如果redis中存在缓存数据，则直接返回
            if(cacheResult != null){
                log.info("redis缓存命中，直接返回数据。 cacheKey:{}", cacheKey);
                return cacheResult;
            }

            PageResult pageResult = productService.pageQuery(productPageQueryDTO);

            //将查询的结果缓存到redis中
            redisTemplate.opsForValue().set(cacheKey, pageResult);
            log.info("将查询的结果缓存到redis中。cacheKey:{}", cacheKey);

            return pageResult;

        }else { //若搜索框中有搜索内容，不使用redis缓存
            return productService.pageQuery(productPageQueryDTO);
        }
    }

    /**
     * 根据id回显商品数据
     * @param id
     * @return
     */
    public ProductVO getProductById(Long id) {
        Product product = productService.getProductById(id);
        //封装成VO返回
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(product, productVO);
        return productVO;
    }

    /**
     * 根据查询条件生成redis的缓存key
     * @param productPageQueryDTo
     * @return
     */
    private String generateCacheKey(ProductPageQueryDTO productPageQueryDTo){
        StringBuilder keyBuilder = new StringBuilder("product");
        keyBuilder.append(":page:").append(productPageQueryDTo.getPage());
        keyBuilder.append(":pageSize:").append(productPageQueryDTo.getPageSize());
        if (productPageQueryDTo.getCategoryId() != null){
            keyBuilder.append(":categoryId:").append(productPageQueryDTo.getCategoryId());
        }
        return keyBuilder.toString();
    }

    /**
     * 增加一个商品
     * @param productDTO
     * @return
     */
    public void addOneProduct(ProductDTO productDTO) {
        productService.addOneProduct(productDTO);

        // 清理与商品分页查询相关的Redis缓存
        clearProductPageCache();
        log.info("新增商品后，清理Redis缓存。");
    }

    /**
     * 清理与商品分页查询相关的Redis缓存
     */
    private void clearProductPageCache() {
        // 生成缓存键的前缀
        String cacheKeyPrefix = "product:page:*";

        // 查找所有匹配的缓存键
        Set<String> keys = redisTemplate.keys(cacheKeyPrefix);

        // 删除所有匹配的缓存键
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("清理Redis缓存，删除的缓存键：{}", keys);
        }
    }
}
