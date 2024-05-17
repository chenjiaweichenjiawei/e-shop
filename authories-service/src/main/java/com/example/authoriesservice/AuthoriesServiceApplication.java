package com.example.authoriesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.example.authoriesservice.feign")
public class AuthoriesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthoriesServiceApplication.class, args);
    }

}
