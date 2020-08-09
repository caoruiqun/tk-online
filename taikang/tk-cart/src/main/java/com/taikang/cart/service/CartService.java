package com.taikang.cart.service;

import com.taikang.cart.pojo.Cart;

import java.util.List;

/**
 * @author CaoRuiqun on 2020/6/21
 */
public interface CartService {
    void addCart(Cart cart);

    List<Cart> queryCartList();

    void updateNum(Long skuId, Integer num);

    void deleteCart(String skuId);
}
