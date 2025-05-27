package com.delivery.drivers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// Removido: import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
// Removido: @EnableDiscoveryClient
public class DriversServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriversServiceApplication.class, args);
    }
}
