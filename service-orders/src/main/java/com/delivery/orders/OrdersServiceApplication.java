package com.delivery.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// Removido: import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
// Removido: @EnableDiscoveryClient
public class OrdersServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdersServiceApplication.class, args);
    }
}
