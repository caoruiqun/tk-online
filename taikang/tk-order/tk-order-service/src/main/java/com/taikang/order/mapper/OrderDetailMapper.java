package com.taikang.order.mapper;

import com.tailkang.order.pojo.OrderDetail;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * @Author: 98050
 * @Time: 2018-10-27 16:33
 * @Feature:
 */
@org.apache.ibatis.annotations.Mapper
public interface OrderDetailMapper extends Mapper<OrderDetail>, InsertListMapper<OrderDetail> {

}
