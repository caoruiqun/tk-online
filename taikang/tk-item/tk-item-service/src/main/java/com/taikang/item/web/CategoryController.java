package com.taikang.item.web;

import com.taikang.item.pojo.Category;
import com.taikang.item.service.CategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-04-09 00:12
 **/
@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据父节点id查询商品分类
     *
     * @Param: [pid]
     */
    @GetMapping("/list")
    public ResponseEntity<List<Category>> queryCategoryListByPid(@RequestParam("pid") Long pid) {
        List<Category> categoryList = categoryService.queryCategoryListByPid(pid);
        return ResponseEntity.ok(categoryList);
    }


    /**
     * 根据分类id集合查询分类名称
     *
     * @param ids
     * @return
     */
    @GetMapping("/list/ids")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam("ids") List<Long> ids) {
        List<Category> list = categoryService.queryCategoryByIds(ids);
        return ResponseEntity.ok(list);
    }
}
