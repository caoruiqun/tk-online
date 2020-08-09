package com.taikang.order.service;

import com.taikang.order.dto.OrderDto;
import com.tailkang.order.pojo.Order;

/**
 * @author CaoRuiqun on 2020/7/28
 */
public interface OrderService {
    Long creatOrder(OrderDto orderDto);

    Order queryOrderById(Long id);

    void updateOrderStatus(Long orderId, int i);
}
