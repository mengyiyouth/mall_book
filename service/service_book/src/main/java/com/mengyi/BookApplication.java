package com.mengyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author mengyiyouth
 * @date 2021/5/22 18:54
 **/
@SpringBootApplication
@ComponentScan(basePackages = {"com.mengyi"})
@EnableDiscoveryClient //nacos注册
@EnableFeignClients //服务调用
public class BookApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookApplication.class, args);
    }
}
