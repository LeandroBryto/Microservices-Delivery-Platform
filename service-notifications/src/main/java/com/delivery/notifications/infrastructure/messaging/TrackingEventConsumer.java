package com.delivery.notifications.infrastructure.messaging;

import com.delivery.notifications.domain.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class TrackingEventConsumer {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "tracking-status-queue", durable = "true"),
            exchange = @Exchange(value = "tracking-exchange", type = "topic"),
            key = "tracking.status.updated"
    ))
    public void handleTrackingStatusUpdatedEvent(Map<String, Object> message) {
        log.info("Recebido evento de atualização de status de tracking: {}", message);
        
        Long orderId = Long.valueOf(message.get("orderId").toString());
        String status = message.get("status").toString();
        
        // Simulação de envio de notificação
        Notification notification = Notification.builder()
                .id(UUID.randomUUID().toString())
                .orderId(orderId)
                .recipientEmail("cliente@email.com")
                .subject("Atualização do seu pedido")
                .content("O status do seu pedido foi atualizado para: " + status)
                .type(determineNotificationType(status))
                .status(Notification.NotificationStatus.SENT)
                .createdAt(LocalDateTime.now())
                .sentAt(LocalDateTime.now())
                .build();
        
        log.info("Notificação enviada: {}", notification);
    }
    
    private Notification.NotificationType determineNotificationType(String status) {
        if (status.contains("CREATED")) {
            return Notification.NotificationType.ORDER_CREATED;
        } else if (status.contains("ASSIGNED")) {
            return Notification.NotificationType.ORDER_ASSIGNED;
        } else if (status.contains("IN_TRANSIT")) {
            return Notification.NotificationType.ORDER_IN_TRANSIT;
        } else if (status.contains("DELIVERED")) {
            return Notification.NotificationType.ORDER_DELIVERED;
        } else if (status.contains("DELAYED")) {
            return Notification.NotificationType.ORDER_DELAYED;
        } else {
            return Notification.NotificationType.ORDER_CANCELLED;
        }
    }
}
