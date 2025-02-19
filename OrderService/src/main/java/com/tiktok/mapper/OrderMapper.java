package com.tiktok.mapper;

import com.tiktok.dto.OrderPaidDTO;
import com.tiktok.entity.Orders;
import com.tiktok.vo.OrderWithDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {

    /**
     * 插入订单
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单id修改订单状态为已支付
     * @param orderPaidDTO
     */
    void markOrderPaidById(OrderPaidDTO orderPaidDTO);

    /**
     * 根据用户id查询订单列表
     * @param userId
     * @return
     */
    List<OrderWithDetailVO> list(Long userId);

    /**
     * 根据id查询单条订单
     * @param id
     * @return
     */
    @Select("select * from `tiktok-mall`.orders where id = #{id}")
    Orders getOrderById(Long id);
}
