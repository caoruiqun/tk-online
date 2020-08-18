package com.taikang.common.mapper;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;
//import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-04-16 00:58
 **/
@RegisterMapper
public interface BaseMapper<T> extends Mapper<T>, IdListMapper<T,Long>, InsertListMapper<T> {
}
