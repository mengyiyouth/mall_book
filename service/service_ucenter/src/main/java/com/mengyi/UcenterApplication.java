package com.mengyi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author mengyiyouth
 * @date 2021/2/5 18:59
 **/
@SpringBootApplication
@ComponentScan({"com.mengyi"})
@EnableDiscoveryClient
@MapperScan("com.mengyi.mapper")
public class UcenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UcenterApplication.class, args);
    }
}
