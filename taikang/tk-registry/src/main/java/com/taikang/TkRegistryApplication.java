package com.taikang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-04-08 21:07
 **/
@SpringBootApplication
@EnableEurekaServer
public class TkRegistryApplication {
    public static void main(String[] args){
        SpringApplication.run(TkRegistryApplication.class,args);
    }
}
