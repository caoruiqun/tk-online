package com.taikang.cart.service.impl;

import com.taikang.auth.entity.UserInfo;
import com.taikang.cart.client.GoodsClient;
import com.taikang.cart.interceptor.UserInterceptor;
import com.taikang.cart.pojo.Cart;
import com.taikang.cart.service.CartService;
import com.taikang.common.enums.ExceptionEnum;
import com.taikang.common.exception.TkException;
import com.taikang.common.utils.JsonUtils;
import com.taikang.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-06-21 17:32
 **/
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

//    @Autowired
//    private RedisTemplate redisTemplate;

//    @Autowired
//    private GoodsClient goodsClient;

    private static String KEY_PREFIX = "cart:uid:";

    private final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    /**
     * 添加购物车
     * @param cart
     */
    @Override
    public void addCart(Cart cart) {
        //1.获取登录用户
        UserInfo userInfo = UserInterceptor.getLoginUser();
        //2.Redis的key
        String key = KEY_PREFIX + userInfo.getId();
        //3.获取hash操作对象
        BoundHashOperations<String,Object,Object> hashOperations = redisTemplate.boundHashOps(key);
        //4.查询是否存在 判断当前购物车商品是否存在
        Long hashKey = cart.getSkuId();
        Integer num = cart.getNum();
        Boolean result = hashOperations.hasKey(hashKey.toString());
        if (result){
            //5.存在，获取购物车数据
            String json = hashOperations.get(hashKey.toString()).toString();
            cart = JsonUtils.toBean(json,Cart.class);
            //6.修改购物车数量
            cart.setNum(cart.getNum() + num);
        }
//        else{
//            //7.不存在，新增购物车数据
//            cart.setUserId(userInfo.getId());
//            //8.其他商品信息，需要查询商品微服务
//            Sku sku = this.goodsClient.querySkuById(hashKey);
//            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(),",")[0]);
//            cart.setPrice(sku.getPrice());
//            cart.setTitle(sku.getTitle());
//            cart.setOwnSpec(sku.getOwnSpec());
//        }
        //9.将购物车数据写入redis
        hashOperations.put(cart.getSkuId().toString(),JsonUtils.toString(cart));
    }

    /**
     * 查询购物车
     * @return
     */
    @Override
    public List<Cart> queryCartList() {
        //1.获取登录的用户信息
        UserInfo userInfo = UserInterceptor.getLoginUser();
        //2.判断是否存在购物车
        String key = KEY_PREFIX + userInfo.getId();
        if (!redisTemplate.hasKey(key)) {
            //3.不存在直接返回
//            return null;
            throw new TkException(ExceptionEnum.CART_NOT_FUND);
        }
        //获取用户所有购物车数据
        BoundHashOperations<String,Object,Object> hashOperations = redisTemplate.boundHashOps(key);
        List<Object> values = hashOperations.values();
        //4.判断是否有数据
//        if (CollectionUtils.isEmpty(carts)){
//            return null;
//        }
        List<Cart> carts = values.stream().map(o -> JsonUtils.toBean(o.toString(), Cart.class)).collect(Collectors.toList());
        return carts;
    }

    /**
     * 更新购物车中商品数量
     * @param skuId
     * @param num
     */
    @Override
    public void updateNum(Long skuId, Integer num) {
        //1.获取登录用户
        UserInfo userInfo = UserInterceptor.getLoginUser();
        String key = KEY_PREFIX + userInfo.getId();
        BoundHashOperations<String,Object,Object> hashOperations = redisTemplate.boundHashOps(key);
        //2.获取购物车
        String json = hashOperations.get(skuId.toString()).toString();
        Cart cart = JsonUtils.toBean(json,Cart.class);
        cart.setNum(num);
        //3.写入购物车
        hashOperations.put(skuId.toString(),JsonUtils.toString(cart));
    }

    /**
     * 删除购物车中的商品
     * @param skuId
     */
    @Override
    public void deleteCart(String skuId) {
        //1.获取登录用户
        UserInfo userInfo = UserInterceptor.getLoginUser();
        String key = KEY_PREFIX + userInfo.getId();
        BoundHashOperations<String,Object,Object> hashOperations = redisTemplate.boundHashOps(key);
        //2.删除商品
        hashOperations.delete(skuId);
    }

}
