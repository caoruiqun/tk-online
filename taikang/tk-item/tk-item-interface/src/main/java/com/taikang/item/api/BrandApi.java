package com.taikang.item.api;

import com.taikang.item.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author CaoRuiqun on 2020/4/22
 */
public interface BrandApi {

    /**
     * 根据品牌id查询品牌信息
     * @param id
     * @return
     */
    @GetMapping("/brand/list/{id}")
    Brand queryBrandById(@PathVariable("id") Long id);


    @GetMapping("/brands")
    List<Brand> queryBrandByIds(@RequestParam("ids") List<Long> ids);

}
