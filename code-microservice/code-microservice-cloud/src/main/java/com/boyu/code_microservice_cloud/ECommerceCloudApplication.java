package com.boyu.code_microservice_cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.boyu")
public class ECommerceCloudApplication {
    public static void main(String[] args) {
        SpringApplication.run(ECommerceCloudApplication.class, args);
    }
}