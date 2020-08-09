package com.taikang.item.service;

import com.taikang.item.pojo.Category;

import java.util.List;

/**
 * @author CaoRuiqun on 2020/3/12
 */
public interface CategoryService {
    List<Category> queryCategoryListByPid(Long pid);

    List<Category> queryCategoryByIds(List<Long> ids);
}
