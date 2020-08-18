package com.taikang.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taikang.item.pojo.Brand;
import com.taikang.common.enums.ExceptionEnum;
import com.taikang.common.exception.TkException;
import com.taikang.common.vo.PageResult;
import com.taikang.item.mapper.BrandMapper;
import com.taikang.item.service.BrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-03-14 15:06
 **/
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 分页查询品牌
     * @Param: [pageNum, pageSize, orderBy, desc, key]
     * @return:
     */
    @Override
//    public Page<Brand> queryBrandByPage(Integer pageNum, Integer pageSize, String orderBy, Boolean desc, String key) {
    public PageResult<Brand> queryBrandByPage(Integer pageNum, Integer pageSize, String orderBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(pageNum, pageSize);
        //过滤
        /**
         * where name like "%x%" or letter=="x"
         * order by id desc
         */
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {
            //过滤条件
            example.createCriteria().orLike("name", "%" + key + "%")
                    .orEqualTo("letter", key.toUpperCase());
        }
        //排序
        if (StringUtils.isNotBlank(orderBy)) {
            String orderByClause = orderBy + (desc ? " DESC" : " ASC");;
            example.setOrderByClause(orderByClause);
        }
        //查询
        List<Brand> brandList = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(brandList)) {
            throw new TkException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        //解析分页结果
        PageInfo<Brand> pageInfo = new PageInfo<>(brandList);

        return new PageResult<>(pageInfo.getTotal(),brandList);
    }

    /**
     * 根据category id查询brand,左连接
     * @param cid
     * @return
     */
    @Override
    public List<Brand> getBrandbyCid(Long cid) {
        List<Brand> brandList = brandMapper.selectBrandByCategoryId(cid);
        if (CollectionUtils.isEmpty(brandList)) {
            throw new TkException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brandList;
    }

    /**
    * 根据品牌id查询品牌
    * @Param [id]
    * @return
    */
    @Override
    public Brand queryBrandById(Long id) {
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if (null == brand) {
            throw new TkException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brand;
    }

    @Override
    public List<Brand> queryBrandByIds(List<Long> ids) {
        List<Brand> brandList = brandMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(brandList)) {
            throw new TkException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brandList;
    }


    /**
    * 新增品牌
    * @Param: [brand, cidList]
    * @return:
    */
    @Override
    @Transactional
    public void addBrand(Brand brand, List<Long> cidList) {
        //新增品牌
        int insertCount = brandMapper.insert(brand);
        if (insertCount != 1) {
            throw new TkException(ExceptionEnum.BRAND_ADD_ERROR);
        }
        //新增品牌-分类中间表
        for (Long cid : cidList) {
            int insertCount2 = brandMapper.insertCategoryBrand(cid,brand.getId());
            if (insertCount2 != 1) {
                throw new TkException(ExceptionEnum.BRAND_ADD_ERROR);
            }
        }
    }

}
