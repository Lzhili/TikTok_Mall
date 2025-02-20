package com.tiktok.service.impl;

import com.tiktok.entity.Category;
import com.tiktok.mapper.CategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryMapper categoryMapper; // 模拟 CategoryMapper

    @InjectMocks
    private CategoryServiceImpl categoryService; // 注入模拟的 CategoryMapper 到 CategoryServiceImpl

    // 设置模拟数据
    private List<Category> mockCategoryList;

    @BeforeEach
    void setUp() {
        // 初始化模拟数据，模拟 LocalDateTime 时间
        mockCategoryList = Arrays.asList(
                new Category(1L, "Electronics", 1, LocalDateTime.of(2023, 1, 1, 10, 0, 0, 0), LocalDateTime.now()),
                new Category(2L, "Clothing", 2, LocalDateTime.of(2023, 2, 1, 12, 0, 0, 0), LocalDateTime.now()),
                new Category(3L, "Food", 3, LocalDateTime.of(2023, 3, 1, 14, 0, 0, 0), LocalDateTime.now())
        );
    }

    @Test
    void testList() {
        // 定义模拟的行为（执行调用 categoryMapper.list() 时的插桩逻辑）
        when(categoryMapper.list()).thenReturn(mockCategoryList);

        // 执行服务方法
        List<Category> result = categoryService.list();

        // 验证结果
        assertNotNull(result, "The result should not be null.");
        assertEquals(3, result.size(), "The result size should be 3.");
        assertEquals("Electronics", result.get(0).getName(), "First category name should be 'Electronics'");
        assertEquals("Clothing", result.get(1).getName(), "Second category name should be 'Clothing'");
        assertEquals("Food", result.get(2).getName(), "Third category name should be 'Food'");
    }

    @Test
    void testListEmpty() {
        // 定义模拟的行为：返回一个空的列表
        when(categoryMapper.list()).thenReturn(Arrays.asList());

        // 执行服务方法
        List<Category> result = categoryService.list();

        // 验证结果
        assertNotNull(result, "The result should not be null.");
        assertTrue(result.isEmpty(), "The result should be an empty list.");
    }
}