package com.taikang.item.mapper;

import com.taikang.common.mapper.BaseMapper;
import com.taikang.item.pojo.Stock;

/**
 * @author CaoRuiqun on 2020/4/15
 */
@org.apache.ibatis.annotations.Mapper
//public interface StockMapper extends Mapper<Stock>, InsertListMapper<Stock>, IdListMapper<Stock,Long> {
public interface StockMapper extends BaseMapper<Stock> {
}
