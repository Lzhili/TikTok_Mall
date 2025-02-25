package com.tiktok.mapper;

import com.github.pagehelper.Page;
import com.tiktok.dto.ProductPageQueryDTO;
import com.tiktok.entity.Product;
import com.tiktok.vo.ProductVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProductMapper {

    /**
     * 商品分页查询
     * @param productPageQueryDTO
     * @return
     */
    Page<ProductVO> pageQuery(ProductPageQueryDTO productPageQueryDTO);

    /**
     * 根据id回显商品数据
     * @param id
     * @return
     */
    @Select("select * from `tiktok-mall`.product where id = #{id}")
    Product getProductById(Long id);

    /**
     * 增加一个商品
     * @param product
     */
    void insertOneProduct(Product product);
}
