package com.example.thirdservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ThirdServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThirdServiceApplication.class, args);
    }

}
