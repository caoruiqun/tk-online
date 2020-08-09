package com.taikang.item.service.impl;

import com.taikang.item.pojo.Category;
import com.taikang.common.enums.ExceptionEnum;
import com.taikang.common.exception.TkException;
import com.taikang.item.mapper.CategoryMapper;
import com.taikang.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-03-12 23:09
 **/
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
    * 根据父节点id查询分类
    * @Param: [pid]
    */
    @Override
    public List<Category> queryCategoryListByPid(Long pid) {
        //查询条件，mapper会将对象中的非空属性作为查询条件
        Category t = new Category();
        t.setParentId(pid);
        List<Category> categoryList = categoryMapper.select(t);
        //判断结果
        if (CollectionUtils.isEmpty(categoryList)) {    //list == null || list.isEmpty()
            throw new TkException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return categoryList;
    }

    @Override
    public List<Category> queryCategoryByIds(List<Long> ids) {
        List<Category> categoryList = categoryMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(categoryList)) {
            throw new TkException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return categoryList;
    }
}
