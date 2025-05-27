package com.delivery.notifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// Removido: import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
// Removido: @EnableDiscoveryClient
public class NotificationsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationsServiceApplication.class, args);
    }
}
