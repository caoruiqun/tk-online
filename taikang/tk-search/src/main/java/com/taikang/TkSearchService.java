package com.taikang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-04-22 20:31
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class TkSearchService {

    public static void main(String[] args) {
        SpringApplication.run(TkSearchService.class, args);
    }
}