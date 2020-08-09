package com.taikang.item.service;

import com.taikang.item.pojo.Brand;
import com.taikang.common.vo.PageResult;

import java.util.List;

/**
 * @author CaoRuiqun on 2020/3/14
 */
public interface BrandService {
//    Page<Brand> getBrandListByPage(Integer pageNum, Integer pageSize, String orderBy, Boolean desc, String key);

    void addBrand(Brand brand, List<Long> cid);

    PageResult<Brand> getBrandListByPage(Integer pageNum, Integer pageSize, String orderBy, Boolean desc, String key);

    List<Brand> getBrandbyCid(Long cid);

    Brand queryBrandById(Long id);

    List<Brand> queryBrandByIds(List<Long> ids);
}
