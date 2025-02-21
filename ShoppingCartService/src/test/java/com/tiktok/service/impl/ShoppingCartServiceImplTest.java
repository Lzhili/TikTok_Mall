package com.tiktok.service.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.tiktok.dto.ShoppingCartDTO;
import com.tiktok.dto.ShoppingCartQuantityDTO;
import com.tiktok.entity.Product;
import com.tiktok.entity.ShoppingCart;
import com.tiktok.mapper.ShoppingCartMapper;
import com.tiktok.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceImplTest {

    @Mock
    private ShoppingCartMapper shoppingCartMapper;  // Mock ShoppingCartMapper

    @Mock
    private ProductService productService;  // Mock ProductService

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;  // Inject mocked ShoppingCartMapper and ProductService into ShoppingCartServiceImpl

    private ShoppingCartDTO shoppingCartDTO;
    private ShoppingCartQuantityDTO shoppingCartQuantityDTO;

    @BeforeEach
    void setUp() {
        // Setup mock data
        shoppingCartDTO = new ShoppingCartDTO();
        shoppingCartDTO.setUserId(1L);
        shoppingCartDTO.setProductId(1L);
        shoppingCartDTO.setQuantity(8);

//        // Setup mock data for ShoppingCartQuantityDTO
//        shoppingCartQuantityDTO = new ShoppingCartQuantityDTO();  // 莫名报错，只能使用 builder
//        shoppingCartQuantityDTO.setUserId(1L);
//        shoppingCartQuantityDTO.setProductId(1L);
        shoppingCartQuantityDTO = ShoppingCartQuantityDTO.builder()
                .userId(1L)
                .productId(1L)
                .build();
    }

    @Test
    void testAddShoppingCart_WhenProductExists() {
        // Mock data
        ShoppingCart existingCart = ShoppingCart.builder()
                .id(1L)
                .userId(1L)
                .productId(1L)
                .quantity(1)
                .build();

        // Mock list method
        when(shoppingCartMapper.list(any())).thenReturn(Arrays.asList(existingCart));

        // Mock getProductById method
        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .picture("test_picture.jpg")
                .price(new BigDecimal("9.99"))
                .build();

        // Execute method
        shoppingCartService.addShoppingCart(shoppingCartDTO);

        // Assert results
        verify(shoppingCartMapper).updateById(existingCart);  // 验证 updateById 方法是否被正确调用。
    }

    @Test
    void testAddShoppingCart_WhenProductNotExist() {
        // Mock data
        when(shoppingCartMapper.list(any())).thenReturn(Arrays.asList());

        // Mock getProductById method
        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .picture("test_picture.jpg")
                .price(new BigDecimal("9.99"))
                .build();
        when(productService.getProductById(1L)).thenReturn(product);

        // Execute method
        shoppingCartService.addShoppingCart(shoppingCartDTO);

        // Assert results
        verify(shoppingCartMapper, times(1)).insert(any());   // 验证 insert 方法是否被正确调用。
    }

    @Test
    void testGetQuantity_WhenProductExists() {
        // Mock data
        ShoppingCart existingCart = ShoppingCart.builder()
                .id(1L)
                .userId(1L)
                .productId(1L)
                .quantity(6)
                .build();

        // Mock list method
        when(shoppingCartMapper.list(any())).thenReturn(Arrays.asList(existingCart));

        // Execute method
        Integer quantity = shoppingCartService.getQuantity(shoppingCartQuantityDTO);

        // Assert results
        assertEquals(6, quantity, "返回的商品数量应为6");
    }

    @Test
    void testGetQuantity_WhenProductNotExists() {
        // Mock data
        when(shoppingCartMapper.list(any())).thenReturn(Arrays.asList());

        // Execute method
        Integer quantity = shoppingCartService.getQuantity(shoppingCartQuantityDTO);

        // Assert results
        assertNull(quantity, "当商品不存在时，返回空");
    }

    @Test
    void testShowShoppingCart() {
        // Mock data
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(1L).build();
        List<ShoppingCart> carts = Arrays.asList(ShoppingCart.builder().userId(1L).build(),
                ShoppingCart.builder().userId(2L).build(),
                ShoppingCart.builder().userId(3L).build());

        // Mock list method
        when(shoppingCartMapper.list(any())).thenReturn(carts);

        // Execute method
        List<ShoppingCart> result = shoppingCartService.showShoppingCart(shoppingCart);

        // Assert results
        verify(shoppingCartMapper, times(1)).list(any());
        assertEquals(3, result.size(), "返回的购物车数量应为3");
    }

    @Test
    void testDeleteByUserId() {
        // Mock data
        Long userId = 1L;

        // Execute method
        shoppingCartService.deleteByUserId(userId);

        // Assert results
        verify(shoppingCartMapper, times(1)).deleteByUserId(userId);   // 验证 delete 方法是否被正确调用。
    }

    @Test
    void testGetCartNum() {
        // Mock data
        Long userId = 1L;
        ShoppingCart cart1 = ShoppingCart.builder()
                .userId(userId)
                .quantity(2)
                .build();
        ShoppingCart cart2 = ShoppingCart.builder()
                .userId(userId)
                .quantity(3)
                .build();
        ShoppingCart cart3 = ShoppingCart.builder()
                .userId(userId)
                .quantity(8)
                .build();
        List<ShoppingCart> carts = Arrays.asList(cart1, cart2, cart3);

        // Mock list method
        when(shoppingCartMapper.list(any())).thenReturn(carts);

        // Execute method
        Integer totalQuantity = shoppingCartService.getCartNum(userId);

        // Assert results
        assertEquals(13, totalQuantity);
    }
}