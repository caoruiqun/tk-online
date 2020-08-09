package com.taikang.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-03-09 23:35
 **/
//@SpringCloudApplication 加了给注解就不用加下面的@SpringBootApplication、@EnableDiscoveryClient
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class TkGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(TkGatewayApplication.class,args);
    }
}
