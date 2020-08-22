package com.taikang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author: 98050
 * @Time: 2018-10-21 17:29
 * @Feature: 用户中心启动器
 */
@SpringBootApplication
@EnableDiscoveryClient
//@MapperScan("com.taikang.user.mapper")
public class TkUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(TkUserApplication.class,args);
    }
}
