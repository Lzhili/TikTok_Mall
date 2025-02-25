package com.tiktok.service.impl;

import com.tiktok.constant.MQConstant;
import com.tiktok.constant.MessageConstant;
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
import com.tiktok.service.OrderService;
import com.tiktok.service.ShoppingCartService;
import com.tiktok.vo.OrderSubmitVO;
import com.tiktok.vo.OrderWithDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
//@Transactional, 加上interfaceClass事务注解才能生效
@DubboService(interfaceClass = OrderService.class)
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @DubboReference
    private AddressBookService addressBookService;

    @DubboReference
    private ShoppingCartService shoppingCartService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     *  提交订单
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    @GlobalTransactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //1.处理业务异常（地址簿为空、购物车数据为空）
        AddressBook addressBook = addressBookService.getById(ordersSubmitDTO.getAddressBookId());
        if(addressBook == null){
            //当前用户的地址簿为空，抛出异常
            throw new AddressBookIsNullException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        //查询当前用户购物车数据
        Long userId = ordersSubmitDTO.getUserId();
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(userId)
                .build();
        List<ShoppingCart> shoppingCartList = shoppingCartService.showShoppingCart(shoppingCart);

        if(shoppingCartList == null || shoppingCartList.size() == 0){
            //当前用户的购物车数据为空，抛出异常
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //2.向订单表插入一条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders); //复制 地址簿id 和 订单总金额
        orders.setNumber(String.valueOf(System.currentTimeMillis())); //订单号
        orders.setUserId(userId); //用户id
        orders.setOrderTime(LocalDateTime.now()); //下单时间
        orders.setPayMethod(Orders.WECHAT); //支付方式，默认：微信
        orders.setIsPaid(Orders.UN_PAID); //支付状态
        orders.setUsername(addressBook.getUsername()); //用户名
        orders.setEmail(addressBook.getEmail()); //邮箱

        String addressDetail = addressBook.getCountry() + addressBook.getProvince() +
                addressBook.getCity() + addressBook.getDetail();
        orders.setAddressDetail(addressDetail); //完整详细地址

        orderMapper.insert(orders);

        //3.向订单明细表插入n条数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList){
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId()); //设置当前订单明细关联的订单id
            orderDetailList.add(orderDetail);
        }

        orderDetailMapper.insertBatch(orderDetailList);

        //4.清空购物车数据
        shoppingCartService.deleteByUserId(userId);

        //模拟错误，测试分布式事务是否回滚，
//        try {
//            int i = 1/0; //可在此打断点，便于查看各个表和undo_log表的变化
//        } catch (Exception e) {
//            throw new RuntimeException("测试分布式事务是否回滚！");
//        }

        //5.封装返回的VO数据
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())  //订单id
                .orderNumber(orders.getNumber()) //订单号
                .orderAmount(orders.getAmount()) //订单金额
                .orderTime(orders.getOrderTime()) //下单时间
                .build();

        //6.发送延迟消息，检测订单支付状态
        try {
            rabbitTemplate.convertAndSend(
                    MQConstant.DELAY_EXCHANGE_NAME,//exchange
                    MQConstant.DELAY_ORDER_KEY, // routingKey
                    orders.getId(), //消息体为订单id
                    new MessagePostProcessor() { //利用消息后置处理器添加消息头
                        @Override
                        public Message postProcessMessage(Message message) throws AmqpException {
                            //添加延迟消息属性
                            message.getMessageProperties().setDelay(30000);//实际为15分钟，这里为了方便测试可改为30秒
                            return message;
                        }
                    });
            log.info("发送延迟消息成功，检测订单支付状态");
        }catch (Exception e){
            log.error("发送延迟消息失败");
        }

        return orderSubmitVO;
    }

    /**
     * 用户标记订单为已支付
     * @param orderPaidDTO
     */
    @Override
    public void markOrderPaid(OrderPaidDTO orderPaidDTO) {
//        orderPaidDTO.setPayTime(LocalDateTime.now());
        orderMapper.markOrderPaidById(orderPaidDTO);
    }

    /**
     * 查询当前用户所有订单
     * @param userId
     * @return
     */
    @Override
    public List<OrderWithDetailVO> list(Long userId) {
        return orderMapper.list(userId);
    }


    /**
     * 根据id查询单条订单数据
     * @param id
     * @return
     */
    @Override
    public Orders getOrderById(Long id) {
        Orders order = orderMapper.getOrderById(id);
        return order;
    }

    /**
     * 用户取消订单
     * @param orderId
     */
    @Override
    public void cancelOrder(Long orderId) {
        orderMapper.cancelOrderById(orderId);
    }

    /**
     * 根据订单号查询订单
     * @param orderNumber
     * @return
     */
    @Override
    public Orders getOrderByOrderNo(String orderNumber) {
        return orderMapper.getOrderByOrderNo(orderNumber);
    }

}
