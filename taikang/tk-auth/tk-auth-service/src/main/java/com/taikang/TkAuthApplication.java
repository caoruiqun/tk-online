package com.taikang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @description: 授权服务启动器
 * @author: CaoRuiqun
 * @create: 2020-06-19 22:12
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class TkAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(TkAuthApplication.class,args);
    }

}
