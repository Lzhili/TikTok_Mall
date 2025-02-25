package com.tiktok.service;

import com.tiktok.dto.ProductPageQueryDTO;
import com.tiktok.dto.ProductDTO;
import com.tiktok.entity.Product;
import com.tiktok.result.PageResult;
import com.tiktok.vo.ProductVO;

public interface ProductService {
    PageResult pageQuery(ProductPageQueryDTO productPageQueryDTO);

    Product getProductById(Long id);

    void addOneProduct(ProductDTO productDTO);
}
