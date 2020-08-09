package com.taikang.order.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-07-28 20:49
 **/
public class OrderDto {

    @NotNull
    private Long addressId;

    @NotNull
    private Integer paymentType;

    @NotNull
    private List<CartDto> carts;

    public OrderDto() {
    }

    public OrderDto(Long addressId, Integer paymentType, List<CartDto> carts) {
        this.addressId = addressId;
        this.paymentType = paymentType;
        this.carts = carts;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public List<CartDto> getCarts() {
        return carts;
    }

    public void setCarts(List<CartDto> carts) {
        this.carts = carts;
    }
}
