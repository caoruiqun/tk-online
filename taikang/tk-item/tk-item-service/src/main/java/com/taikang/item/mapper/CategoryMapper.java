package com.taikang.item.mapper;

import com.taikang.item.pojo.Category;
import com.taikang.item.pojo.Spu;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author CaoRuiqun on 2020/4/9
 */
@org.apache.ibatis.annotations.Mapper
public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category,Long> {
}
