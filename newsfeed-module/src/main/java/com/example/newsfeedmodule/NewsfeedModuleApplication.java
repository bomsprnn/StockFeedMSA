package com.example.newsfeedmodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
//@EnableJpaAuditing
@EnableDiscoveryClient
public class NewsfeedModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsfeedModuleApplication.class, args);
    }

}
