package com.taikang.order;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;


/**
 * @Author: 98050
 * @Time: 2018-10-27 11:36
 * @Feature: 订单服务启动器
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
//@EnableSwagger2
@MapperScan(value = "com.taikang.order.mapper")
public class TkOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(TkOrderApplication.class,args);
    }
}
