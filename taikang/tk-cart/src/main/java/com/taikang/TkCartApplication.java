package com.taikang;

/**
 * Author: 98050
 * Time: 2018-08-03 21:23
 * Feature:
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author li
 * @time 2018-08-03 21:23
 */
@SpringBootApplication
@EnableDiscoveryClient
public class TkCartApplication {
    public static void main(String[] args) {
        SpringApplication.run(TkCartApplication.class,args);
    }
}
