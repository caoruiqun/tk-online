package com.taikang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-03-15 13:08
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class TkUploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(TkUploadApplication.class, args);
    }
}
