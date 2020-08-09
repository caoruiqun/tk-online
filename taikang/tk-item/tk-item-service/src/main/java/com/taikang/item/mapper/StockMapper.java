package com.taikang.item.mapper;

import com.taikang.item.pojo.Stock;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author CaoRuiqun on 2020/4/15
 */
@org.apache.ibatis.annotations.Mapper
public interface StockMapper extends Mapper<Stock>, InsertListMapper<Stock>, IdListMapper<Stock,Long> {
}
