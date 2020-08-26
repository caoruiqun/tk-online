package com.taikang.item.mapper;

import com.taikang.common.mapper.BaseMapper;
import com.taikang.item.pojo.Sku;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * @author CaoRuiqun on 2020/4/15
 */
@org.apache.ibatis.annotations.Mapper
//public interface SkuMapper extends Mapper<Sku>, IdListMapper<Sku,Long>, InsertListMapper<Sku> {
public interface SkuMapper extends BaseMapper<Sku> {
}
