package com.tiktok.service;

import com.tiktok.dto.ProductPageQueryDTO;
import com.tiktok.result.PageResult;
import com.tiktok.vo.ProductVO;

public interface ProductService {
    PageResult pageQuery(ProductPageQueryDTO productPageQueryDTO);

    ProductVO getProductById(Long id);
}
