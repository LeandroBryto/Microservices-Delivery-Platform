package com.delivery.notifications.api.controller;

import com.delivery.notifications.domain.model.Notification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "API para gerenciamento de notificações")
public class NotificationController {

    private final List<Notification> notifications = new ArrayList<>();

    @GetMapping
    @Operation(summary = "Listar todas as notificações", description = "Retorna uma lista com todas as notificações enviadas")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Buscar notificações por pedido", description = "Retorna todas as notificações de um pedido específico")
    public ResponseEntity<List<Notification>> getNotificationsByOrderId(@PathVariable Long orderId) {
        List<Notification> orderNotifications = notifications.stream()
                .filter(n -> n.getOrderId().equals(orderId))
                .toList();
        return ResponseEntity.ok(orderNotifications);
    }

    @PostMapping
    @Operation(summary = "Enviar nova notificação", description = "Registra e envia uma nova notificação")
    public ResponseEntity<Notification> sendNotification(@Valid @RequestBody Notification notification) {
        notification.setId(UUID.randomUUID().toString());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setStatus(Notification.NotificationStatus.SENT);
        notification.setSentAt(LocalDateTime.now());
        
        notifications.add(notification);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }
}
