package com.delivery.tracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// Removido: import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
// Removido: @EnableDiscoveryClient
public class TrackingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrackingServiceApplication.class, args);
    }
}
