package com.tiktok.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tiktok.dto.ProductDTO;
import com.tiktok.dto.ProductPageQueryDTO;
import com.tiktok.entity.Product;
import com.tiktok.mapper.ProductMapper;
import com.tiktok.result.PageResult;
import com.tiktok.service.ProductService;
import com.tiktok.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.ThreadUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@Slf4j
//@Transactional, 加上interfaceClass事务注解才能生效
@DubboService(interfaceClass = ProductService.class)
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductMapper productMapper;

    /**
     * 商品分页查询
     * @param productPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(ProductPageQueryDTO productPageQueryDTO) {
        PageHelper.startPage(productPageQueryDTO.getPage(), productPageQueryDTO.getPageSize());
        Page<ProductVO> page = productMapper.pageQuery(productPageQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());

        return pageResult;
    }

    /**
     * 根据id回显商品数据
     * @param id
     * @return
     */
    @Override
    public Product getProductById(Long id) {
        //模拟业务延迟
//        try {
//            Thread.sleep(500);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
        Product product = productMapper.getProductById(id);
        return product;
    }

    @Override
    public void addOneProduct(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);

        LocalDateTime now = LocalDateTime.now();
        product.setCreateTime(now);
        product.setUpdateTime(now);

        productMapper.insertOneProduct(product);
    }
}
