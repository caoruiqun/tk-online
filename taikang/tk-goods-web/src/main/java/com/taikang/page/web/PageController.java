package com.taikang.page.web;

import com.taikang.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-04-28 20:53
 **/
@Controller
@RequestMapping("/item")
public class PageController {

    @Autowired
    private PageService pageService;

    /**
     * 跳转到商品详情页
     * @param model
     * @param spuId
     * @return
     */
    @GetMapping("/{id}.html")
    public String toItemPage(Model model, @PathVariable("id")Long spuId){
        //查询模型数据
        Map<String, Object> modelMap = pageService.loadModel(spuId);
        //准备模型数据
        model.addAllAttributes(modelMap);
        //返回视图
        return "item";
    }

}

