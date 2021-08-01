package com.mengyi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author mengyiyouth
 * @date 2021/2/8 8:07
 **/
@SpringBootApplication
@ComponentScan(basePackages = {"com.mengyi"})
@EnableDiscoveryClient
@EnableFeignClients //服务调用
@MapperScan("com.mengyi.mapper")
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
