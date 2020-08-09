package com.taikang.search.pojo;

import com.taikang.common.vo.PageResult;
import com.taikang.item.pojo.Brand;
import com.taikang.item.pojo.Category;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-04-25 17:03
 **/
public class SearchResult extends PageResult<Goods>{

    /**
     * 分类的集合
     */
    private List<Category> categories;

    /**
     * 品牌的集合
     */
    private List<Brand> brands;

    /**
     * 规格参数的过滤条件
     */
    private List<Map<String,Object>> specs;

    public SearchResult() {
    }

//    public SearchResult(Long total, Long totalPage, List<Goods> items, List<Category> categories, List<Brand> brands) {
//        super(total, totalPage, items);
//        this.categories = categories;
//        this.brands = brands;
//    }

    public SearchResult(Long total, Long totalPage, List<Goods> items, List<Category> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public List<Map<String, Object>> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Map<String, Object>> specs) {
        this.specs = specs;
    }
}