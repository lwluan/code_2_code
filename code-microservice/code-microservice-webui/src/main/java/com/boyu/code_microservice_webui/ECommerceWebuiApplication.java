package com.boyu.code_microservice_webui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@EnableFeignClients(basePackages = "com.boyu")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.boyu")
public class ECommerceWebuiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ECommerceWebuiApplication.class, args);
    }

    // https://www.cnblogs.com/softidea/p/11099427.html
}