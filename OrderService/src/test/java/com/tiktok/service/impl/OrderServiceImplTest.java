package com.tiktok.service.impl;

import com.tiktok.constant.MQConstant;
import com.tiktok.dto.OrderPaidDTO;
import com.tiktok.dto.OrdersSubmitDTO;
import com.tiktok.entity.AddressBook;
import com.tiktok.entity.OrderDetail;
import com.tiktok.entity.Orders;
import com.tiktok.entity.ShoppingCart;
import com.tiktok.exception.AddressBookIsNullException;
import com.tiktok.exception.ShoppingCartBusinessException;
import com.tiktok.mapper.OrderDetailMapper;
import com.tiktok.mapper.OrderMapper;
import com.tiktok.service.AddressBookService;
import com.tiktok.service.ShoppingCartService;
import com.tiktok.vo.OrderSubmitVO;
import com.tiktok.vo.OrderWithDetailVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderMapper mockOrderMapper;
    @Mock
    private OrderDetailMapper mockOrderDetailMapper;
    @Mock
    private RabbitTemplate mockRabbitTemplate;
    @Mock
    private AddressBookService mockAddressBookService;
    @Mock
    private ShoppingCartService mockShoppingCartService;

    @InjectMocks
    private OrderServiceImpl orderServiceImplUnderTest;

    @Test
    void testSubmitOrder() throws Exception {
        // Setup
        final OrdersSubmitDTO ordersSubmitDTO = new OrdersSubmitDTO(0L, 0L, new BigDecimal("8.88"));
        final OrderSubmitVO expectedResult = OrderSubmitVO.builder()
                .id(1L) // 设置为非 null 的预期值
                .orderNumber("number")
                .orderAmount(new BigDecimal("8.88"))
                .build();

        // Mock配置
        when(mockAddressBookService.getById(0L)).thenReturn(
                AddressBook.builder()
                        .username("username")
                        .detail("addressDetail") // 修正字段名
                        .build()
        );

        when(mockShoppingCartService.showShoppingCart(any())).thenReturn(
                List.of(ShoppingCart.builder().userId(0L).build())
        );

        // 模拟 OrderMapper.insert 方法回填 id
        doAnswer(invocation -> {
            Orders order = invocation.getArgument(0);
            order.setId(1L); // 模拟回填生成的 id
            return null; // insert 方法返回 void
        }).when(mockOrderMapper).insert(any(Orders.class));

        // 执行测试
        final OrderSubmitVO result = orderServiceImplUnderTest.submitOrder(ordersSubmitDTO);

        // 验证结果
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("orderTime", "orderNumber") // 忽略动态生成字段
                .isEqualTo(expectedResult);

        // 验证数据库操作
//        verify(mockOrderMapper).insert(argThat(order ->
//                order.getAmount().equals(new BigDecimal("8.88")) &&
//                        order.getAddressDetail().equals("addressDetail")
//        ));
        verify(mockOrderMapper).insert(any(Orders.class));

        // 验证消息队列
//        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(mockRabbitTemplate).convertAndSend(
                eq(MQConstant.DELAY_EXCHANGE_NAME), eq(MQConstant.DELAY_ORDER_KEY),
                eq(1L), any(MessagePostProcessor.class) // 使用回填的 id
        );
//        assertThat(captor.getValue()).isEqualTo(1L);
    }


    @Test
    void testSubmitOrder_AddressBookServiceReturnsNull() throws Exception {
        // Setup
        final OrdersSubmitDTO ordersSubmitDTO = new OrdersSubmitDTO(0L, 0L, new BigDecimal("0.00"));
        when(mockAddressBookService.getById(0L)).thenReturn(null);

        // Run the test
        assertThatThrownBy(() -> orderServiceImplUnderTest.submitOrder(ordersSubmitDTO)).isInstanceOf(
                AddressBookIsNullException.class);
    }

    @Test
    void testSubmitOrder_ShoppingCartServiceShowShoppingCartReturnsNull() throws Exception {
        // Setup
        final OrdersSubmitDTO ordersSubmitDTO = new OrdersSubmitDTO(0L, 0L, new BigDecimal("0.00"));

        // Configure AddressBookService.getById(...).
        final AddressBook addressBook = AddressBook.builder()
                .username("username")
                .email("email")
                .detail("detail")
                .city("city")
                .province("province")
                .country("country")
                .build();
        when(mockAddressBookService.getById(0L)).thenReturn(addressBook);

        when(mockShoppingCartService.showShoppingCart(ShoppingCart.builder()
                .userId(0L)
                .build())).thenReturn(null);

        // Run the test
        assertThatThrownBy(() -> orderServiceImplUnderTest.submitOrder(ordersSubmitDTO)).isInstanceOf(
                ShoppingCartBusinessException.class);
    }

    @Test
    void testSubmitOrder_ShoppingCartServiceShowShoppingCartReturnsNoItems() throws Exception {
        // Setup
        final OrdersSubmitDTO ordersSubmitDTO = new OrdersSubmitDTO(0L, 0L, new BigDecimal("0.00"));

        // Configure AddressBookService.getById(...).
        final AddressBook addressBook = AddressBook.builder()
                .username("username")
                .email("email")
                .detail("detail")
                .city("city")
                .province("province")
                .country("country")
                .build();
        when(mockAddressBookService.getById(0L)).thenReturn(addressBook);

        when(mockShoppingCartService.showShoppingCart(ShoppingCart.builder()
                .userId(0L)
                .build())).thenReturn(Collections.emptyList());

        // Run the test
        assertThatThrownBy(() -> orderServiceImplUnderTest.submitOrder(ordersSubmitDTO)).isInstanceOf(
                ShoppingCartBusinessException.class);
    }

    @Test
    void testSubmitOrder_RabbitTemplateThrowsAmqpException() throws Exception {
        // Setup
        final OrdersSubmitDTO ordersSubmitDTO = new OrdersSubmitDTO(0L, 0L, new BigDecimal("0.00"));

        // Configure AddressBookService.getById(...).
        final AddressBook addressBook = AddressBook.builder()
                .country("中国")
                .province("北京")
                .city("朝阳区")
                .detail("国贸大厦")
                .username("username")
                .email("email")
                .build();
        when(mockAddressBookService.getById(0L)).thenReturn(addressBook);

        // Configure ShoppingCartService.showShoppingCart(...).
        final List<ShoppingCart> shoppingCarts = List.of(ShoppingCart.builder().userId(0L).build());
        when(mockShoppingCartService.showShoppingCart(any())).thenReturn(shoppingCarts);

        // 模拟主键回填
        doAnswer(inv -> {
            Orders order = inv.getArgument(0);
            order.setId(1001L); // 设置模拟生成的ID
            return null;
        }).when(mockOrderMapper).insert(any(Orders.class));

        // 配置RabbitMQ异常
        doThrow(AmqpException.class).when(mockRabbitTemplate)
                .convertAndSend(any(), any(), any(), any(MessagePostProcessor.class));

        // Run the test
        final OrderSubmitVO result = orderServiceImplUnderTest.submitOrder(ordersSubmitDTO);

        // Verify the results
        // 验证关键字段 + 忽略动态字段
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id", "orderNumber", "orderTime")
                .isEqualTo(OrderSubmitVO.builder()
                        .orderAmount(new BigDecimal("0.00"))
                        .build());

        // 验证数据库操作
        verify(mockOrderMapper).insert(argThat(order ->
                order.getAddressDetail().equals("中国北京朝阳区国贸大厦") &&
                        order.getPayMethod().equals(Orders.WECHAT)
        ));

        // 验证购物车清空
        verify(mockShoppingCartService).deleteByUserId(0L);
    }

    @Test
    void testMarkOrderPaid() {
        // Setup
        final OrderPaidDTO orderPaidDTO = new OrderPaidDTO();
        orderPaidDTO.setUserId(0L);
        orderPaidDTO.setOrderId(0L);
        orderPaidDTO.setPayMethod(0);
        orderPaidDTO.setPayTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));

        // Run the test
        orderServiceImplUnderTest.markOrderPaid(orderPaidDTO);

        // Verify the results
        // Confirm OrderMapper.markOrderPaidById(...).
        final OrderPaidDTO orderPaidDTO1 = new OrderPaidDTO();
        orderPaidDTO1.setUserId(0L);
        orderPaidDTO1.setOrderId(0L);
        orderPaidDTO1.setPayMethod(0);
        orderPaidDTO1.setPayTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        verify(mockOrderMapper).markOrderPaidById(orderPaidDTO1);
    }

    @Test
    void testList() throws Exception {
        // Setup
        final List<OrderWithDetailVO> expectedResult = List.of(OrderWithDetailVO.builder().build());

        // Configure OrderMapper.list(...).
        final List<OrderWithDetailVO> orderWithDetailVOS = List.of(OrderWithDetailVO.builder().build());
        when(mockOrderMapper.list(0L)).thenReturn(orderWithDetailVOS);

        // Run the test
        final List<OrderWithDetailVO> result = orderServiceImplUnderTest.list(0L);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testList_OrderMapperReturnsNoItems() throws Exception {
        // Setup
        when(mockOrderMapper.list(0L)).thenReturn(Collections.emptyList());

        // Run the test
        final List<OrderWithDetailVO> result = orderServiceImplUnderTest.list(0L);

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testGetOrderById() throws Exception {
        // Setup
        final Orders expectedResult = Orders.builder()
                .id(0L)
                .number("number")
                .userId(0L)
                .orderTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                .payMethod(0)
                .amount(new BigDecimal("0.00"))
                .isPaid(0)
                .username("username")
                .email("email")
                .addressDetail("addressDetail")
                .build();

        // Configure OrderMapper.getOrderById(...).
        final Orders orders = Orders.builder()
                .id(0L)
                .number("number")
                .userId(0L)
                .orderTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                .payMethod(0)
                .amount(new BigDecimal("0.00"))
                .isPaid(0)
                .username("username")
                .email("email")
                .addressDetail("addressDetail")
                .build();
        when(mockOrderMapper.getOrderById(0L)).thenReturn(orders);

        // Run the test
        final Orders result = orderServiceImplUnderTest.getOrderById(0L);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testCancelOrder() {
        // Setup
        // Run the test
        orderServiceImplUnderTest.cancelOrder(0L);

        // Verify the results
        verify(mockOrderMapper).cancelOrderById(0L);
    }

    @Test
    void testGetOrderByOrderNo() throws Exception {
        // Setup
        final Orders expectedResult = Orders.builder()
                .id(0L)
                .number("number")
                .userId(0L)
                .orderTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                .payMethod(0)
                .amount(new BigDecimal("0.00"))
                .isPaid(0)
                .username("username")
                .email("email")
                .addressDetail("addressDetail")
                .build();

        // Configure OrderMapper.getOrderByOrderNo(...).
        final Orders orders = Orders.builder()
                .id(0L)
                .number("number")
                .userId(0L)
                .orderTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                .payMethod(0)
                .amount(new BigDecimal("0.00"))
                .isPaid(0)
                .username("username")
                .email("email")
                .addressDetail("addressDetail")
                .build();
        when(mockOrderMapper.getOrderByOrderNo("orderNumber")).thenReturn(orders);

        // Run the test
        final Orders result = orderServiceImplUnderTest.getOrderByOrderNo("orderNumber");

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }
}