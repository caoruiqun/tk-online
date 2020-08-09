package com.taikang.order.service.impl;

import com.taikang.auth.entity.UserInfo;
import com.taikang.common.enums.ExceptionEnum;
import com.taikang.common.exception.TkException;
import com.taikang.common.utils.IdWorker;
import com.taikang.order.client.GoodsClient;
import com.taikang.order.dto.OrderDto;
import com.taikang.order.interceptor.UserInterceptor;
import com.taikang.order.mapper.OrderDetailMapper;
import com.taikang.order.mapper.OrderMapper;
import com.taikang.order.mapper.OrderStatusMapper;
import com.taikang.order.service.OrderService;
import com.taikang.order.service.OrderStatusService;
import com.tailkang.order.pojo.Order;
import com.tailkang.order.pojo.OrderDetail;
import com.tailkang.order.pojo.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-07-28 21:00
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private OrderStatusService orderStatusService;

    @Override
    public Long creatOrder(OrderDto orderDto) {
        //新增订单
        //1.订单编号
        long orderId = idWorker.nextId();

        //2.用户信息
        UserInfo userInfo = UserInterceptor.getLoginUser();

        Order order = new Order();
        order.setOrderId(orderId);
        order.setCreateTime(new Date());
        order.setPaymentType(orderDto.getPaymentType());
        order.setUserId(userInfo.getId());
        order.setBuyerNick(userInfo.getUsername());
        order.setBuyerRate(false);

        //3.收货地址

        //4.金额

        //5.写入数据库

        //新增订单详情

        //新增订单状态
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCreateTime(order.getCreateTime());

        //减库存

        return null;
    }

    @Override
    public Order queryOrderById(Long id) {
        //查询订单
        Order order = orderMapper.selectByPrimaryKey(id);
        if (null == order) {
            throw new TkException(ExceptionEnum.ORDER_NOT_FOUND);
        }

        //查询订单详情
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(id);
        List<OrderDetail> orderDetails = orderDetailMapper.select(orderDetail);
        if (CollectionUtils.isEmpty(orderDetails)) {
            throw new TkException(ExceptionEnum.ORDER_DETAIL_NOT_FOUND);
        }
        order.setOrderDetails(orderDetails);

        //查询订单状态
        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(id);
        if (null == order) {
            throw new TkException(ExceptionEnum.ORDER_STATUS_NOT_FOUND);
        }
        order.setOrderStatus(orderStatus );

        return order;
    }

    /**
     * 更新订单状态
     * @param id
     * @param status
     * @return
     */
    @Override
    public Boolean updateOrderStatus(Long id, Integer status) {
        UserInfo userInfo = UserInterceptor.getLoginUser();
        Long spuId = this.goodsClient.querySkuById(findSkuIdByOrderId(id)).getSpuId();

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(id);
        orderStatus.setStatus(status);

        //延时消息
        OrderStatusMessage orderStatusMessage = new OrderStatusMessage(id,userInfo.getId(),userInfo.getUsername(),spuId,1);
        OrderStatusMessage orderStatusMessage2 = new OrderStatusMessage(id,userInfo.getId(),userInfo.getUsername(),spuId,2);
        //1.根据状态判断要修改的时间
        switch (status){
            case 2:
                //2.付款时间
                orderStatus.setPaymentTime(new Date());
                break;
            case 3:
                //3.发货时间
                orderStatus.setConsignTime(new Date());
                //发送消息到延迟队列，防止用户忘记确认收货
                orderStatusService.sendMessage(orderStatusMessage);
                orderStatusService.sendMessage(orderStatusMessage2);
                break;
            case 4:
                //4.确认收货，订单结束
                orderStatus.setEndTime(new Date());
                orderStatusService.sendMessage(orderStatusMessage2);
                break;
            case 5:
                //5.交易失败，订单关闭
                orderStatus.setCloseTime(new Date());
                break;
            case 6:
                //6.评价时间
                orderStatus.setCommentTime(new Date());
                break;

            default:
                return null;
        }
        int count = this.orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
        return count == 1;
    }


    /**
     * 根据订单id查询其skuId
     * @param id
     * @return
     */
    public Long findSkuIdByOrderId(Long id){
        Example example = new Example(OrderDetail.class);
        example.createCriteria().andEqualTo("orderId", id);
        List<OrderDetail> orderDetail = this.orderDetailMapper.selectByExample(example);
        return orderDetail.get(0).getSkuId();
    }
}
