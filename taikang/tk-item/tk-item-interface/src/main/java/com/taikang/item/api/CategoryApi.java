package com.taikang.item.api;

import com.taikang.item.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author CaoRuiqun on 2020/4/22
 */
public interface CategoryApi {

    /**
     * 根据分类id集合查询分类名称
     *
     * @param ids
     * @return
     */
    @GetMapping("/category/list/ids")
    List<Category> queryCategoryByIds(@RequestParam("ids") List<Long> ids);

}
