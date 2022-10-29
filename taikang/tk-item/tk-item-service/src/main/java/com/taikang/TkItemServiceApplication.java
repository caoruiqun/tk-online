package com.taikang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Cao.Ruiqun
 * @time 2018-08-03 21:23
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
public class TkItemServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TkItemServiceApplication.class, args);
    }
}
