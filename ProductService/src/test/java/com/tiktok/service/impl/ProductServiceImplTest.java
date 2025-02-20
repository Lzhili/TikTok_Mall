package com.tiktok.service.impl;

import com.github.pagehelper.Page;
import com.tiktok.dto.ProductPageQueryDTO;
import com.tiktok.entity.Product;
import com.tiktok.mapper.ProductMapper;
import com.tiktok.result.PageResult;
import com.tiktok.vo.ProductVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductMapper productMapper;  // Mock ProductMapper

    @InjectMocks
    private ProductServiceImpl productService;  // Inject mocked ProductMapper into ProductServiceImpl

    private Product product;
    private ProductPageQueryDTO queryDTO;

    @BeforeEach
    void setUp() {
        // Setup mock data for product
        product = new Product(1L, "Test Product", 2L, BigDecimal.valueOf(99.99), "test_picture.jpg", "Test Description", LocalDateTime.now(), LocalDateTime.now());

        // Setup mock DTO for paging query
        queryDTO = new ProductPageQueryDTO();
        queryDTO.setPage(1);
        queryDTO.setPageSize(10);
    }

    @Test
    void testPageQuery() {
        // Mock page query result
        ProductVO productVO = new ProductVO();
        productVO.setId(1L);
        productVO.setName("Test Product");

        Page<ProductVO> page = mock(Page.class);
        when(productMapper.pageQuery(queryDTO)).thenReturn(page);
        when(page.getTotal()).thenReturn(10L);
        when(page.getResult()).thenReturn(Arrays.asList(productVO));

        // Execute method
        PageResult result = productService.pageQuery(queryDTO);

        // Assert results
        assertNotNull(result, "Result should not be null");
        assertEquals(10L, result.getTotal(), "Total count should match");
        assertEquals(1, result.getRecords().size(), "Rows should match the number of products");
    }

    @Test
    void testGetProductById() {
        // Mock getProductById
        when(productMapper.getProductById(1L)).thenReturn(product);

        // Execute method
        Product result = productService.getProductById(1L);

        // Assert results
        assertNotNull(result, "Product should not be null");
        assertEquals(1L, result.getId(), "Product ID should match");
        assertEquals("Test Product", result.getName(), "Product name should match");
    }

    @Test
    void testGetProductByIdNotFound() {
        // Mock getProductById for non-existing product
        when(productMapper.getProductById(999L)).thenReturn(null);

        // Execute method
        Product result = productService.getProductById(999L);

        // Assert results
        assertNull(result, "Product should be null");
    }
}