package com.taikang.order.dto;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-07-28 20:52
 **/
public class CartDto {

    /**
     * 商品skuid
     */
    private Long skuId;

    /**
     * 购买数量
     */
    private Integer num;

    public CartDto() {
    }

    public CartDto(Long skuId, Integer num) {
        this.skuId = skuId;
        this.num = num;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
