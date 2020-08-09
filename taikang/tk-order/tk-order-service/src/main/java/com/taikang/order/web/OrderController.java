package com.taikang.order.web;

import com.taikang.order.dto.OrderDto;
import com.taikang.order.service.OrderService;
import com.taikang.order.utils.PayHelper;
import com.tailkang.order.pojo.Order;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-07-28 20:53
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayHelper payHelper;

    /**
    * 创建订单
    * @Param [orderDto]
    * @return
    */
    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody OrderDto orderDto) {

        //创建订单
        return ResponseEntity.ok(orderService.creatOrder(orderDto));

    }

    /**
    * 根据id查询订单
    * @Param [id]
    * @return
    */
    @GetMapping("/{id}")
    public ResponseEntity<Order> queryOrderById(@PathVariable("id") Long id) {
        Order order = orderService.queryOrderById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * 根据订单id生成付款链接
     * @param orderId
     * @return
     */
    @GetMapping("url/{id}")
    @ApiOperation(value = "生成微信扫描支付付款链接",notes = "生成付款链接")
    @ApiImplicitParam(name = "id",value = "订单编号",type = "Long")
    @ApiResponses({
            @ApiResponse(code = 200,message = "根据订单编号生成的微信支付地址"),
            @ApiResponse(code = 404,message = "生成链接失败"),
            @ApiResponse(code = 500,message = "服务器异常")
    })
    public ResponseEntity<String> generateUrl(@PathVariable("id") Long orderId){
        String url = this.payHelper.createPayUrl(orderId);
        if (StringUtils.isNotBlank(url)){
            return ResponseEntity.ok(url);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
