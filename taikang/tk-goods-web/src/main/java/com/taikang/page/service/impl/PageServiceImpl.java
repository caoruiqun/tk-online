package com.taikang.page.service.impl;

import com.taikang.item.pojo.*;
import com.taikang.page.client.BrandClient;
import com.taikang.page.client.CategoryClient;
import com.taikang.page.client.GoodsClient;
import com.taikang.page.client.SpecificationClient;
import com.taikang.page.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-05-01 10:16
 **/
@Service
public class PageServiceImpl implements PageService {

    private static final Logger log = LoggerFactory.getLogger(PageServiceImpl.class);

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public Map<String, Object> loadModel(Long spuId) {
        Map<String,Object> modelMap = new HashMap<>();
        //查询spu
        Spu spu = goodsClient.querySpuById(spuId);
        //查询sku
        List<Sku> skuList = spu.getSkus();
        //查询详情
        SpuDetail spuDetail = spu.getSpuDetail();
        //查询brand
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        //查询商品分类
        List<Category> categoryList = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //查询规格参数
        List<SpecGroup> specGroupList = specificationClient.getSpecGroupsByCid(spu.getCid3());
        List<SpecParam> specParam = specificationClient.getSpecParam(null, spu.getCid3(), null);

        modelMap.put("spu",spu);
        modelMap.put("spuDetail",spuDetail);
        modelMap.put("skus",skuList);
        modelMap.put("brand",brand);
        modelMap.put("categories",categoryList);
//        modelMap.put("specName",specName);
//        modelMap.put("specValue",specValue);
//        modelMap.put("groups",groups);
//        modelMap.put("specialParamName",specialParamName);
//        modelMap.put("specialParamValue",specialParamValue);

        return modelMap;
    }


    public void createHtml(Long spuId)  {
        //上下文
        Context context = new Context();
        context.setVariables(loadModel(spuId));
        //输出流
        File dest = new File("D:\\zzzz", spuId + ".html");
        if (dest.exists()) {
            dest.delete();
        }

//        PrintWriter writer = null;
        try {
            PrintWriter writer = new PrintWriter(dest,"UTF-8");
            //生成 HTML
            templateEngine.process("item", context, writer);
        } catch (Exception e) {
            log.error("[静态页服务]生成静态页异常！",e);
        }

    }

    @Override
    public void deleteHtml(Long spuId) {
        File dest = new File("D:\\zzzz", spuId + ".html");
        if (dest.exists()) {
            dest.delete();
        }
    }

}
