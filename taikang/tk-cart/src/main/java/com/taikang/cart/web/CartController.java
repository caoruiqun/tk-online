package com.taikang.cart.web;

import com.taikang.cart.pojo.Cart;
import com.taikang.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-06-21 17:28
 **/
@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 新增购物车
     *
     * @return
     * @Param
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart) {
        cartService.addCart(cart);
//        return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 查询购物车
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<Cart>> queryCartList(){
        List<Cart> carts = this.cartService.queryCartList();
//        if(carts == null){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
        return ResponseEntity.ok(carts);
    }

    /**
     * 修改购物车中商品数量
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num){
        this.cartService.updateNum(skuId,num);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除购物车中的商品
     * @param skuId
     * @return
     */
    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") String skuId){
        this.cartService.deleteCart(skuId);
        return ResponseEntity.ok().build();
    }
}
