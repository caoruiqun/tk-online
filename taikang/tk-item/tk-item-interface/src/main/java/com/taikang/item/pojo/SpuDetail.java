package com.taikang.item.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-04-13 22:39
 **/
@Table(name="tb_spu_detail")
public class SpuDetail {

    /**
     * 对应的SPU的id
     */
    @Id
    private Long spuId;
    /**
     * 商品描述
     */
    private String description;
    /**
     * 商品特殊规格的名称及可选值模板
     */
//    private String specTemplate;
    private String specialSpec;
    /**
     * 商品的全局规格属性
     */
//    private String specifications;
    private String genericSpec;
    /**
     * 包装清单
     */
    private String packingList;
    /**
     * 售后服务
     */
    private String afterService;

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public String getSpecTemplate() {
//        return specTemplate;
//    }
//
//    public void setSpecTemplate(String specTemplate) {
//        this.specTemplate = specTemplate;
//    }
//
//    public String getSpecifications() {
//        return specifications;
//    }
//
//    public void setSpecifications(String specifications) {
//        this.specifications = specifications;
//    }


    public String getSpecialSpec() {
        return specialSpec;
    }

    public void setSpecialSpec(String specialSpec) {
        this.specialSpec = specialSpec;
    }

    public String getGenericSpec() {
        return genericSpec;
    }

    public void setGenericSpec(String genericSpec) {
        this.genericSpec = genericSpec;
    }

    public String getPackingList() {
        return packingList;
    }

    public void setPackingList(String packingList) {
        this.packingList = packingList;
    }

    public String getAfterService() {
        return afterService;
    }

    public void setAfterService(String afterService) {
        this.afterService = afterService;
    }
}
