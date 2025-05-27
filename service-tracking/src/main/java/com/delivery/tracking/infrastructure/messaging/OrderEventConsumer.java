package com.delivery.tracking.infrastructure.messaging;

import com.delivery.tracking.domain.model.TrackingEvent;
import com.delivery.tracking.infrastructure.repository.TrackingEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final TrackingEventRepository trackingEventRepository;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "order-created-queue", durable = "true"),
            exchange = @Exchange(value = "order-exchange", type = "topic"),
            key = "order.created"
    ))
    public void handleOrderCreatedEvent(Map<String, Object> message) {
        log.info("Recebido evento de pedido criado: {}", message);
        
        Long orderId = Long.valueOf(message.get("orderId").toString());
        
        TrackingEvent trackingEvent = TrackingEvent.builder()
                .orderId(orderId)
                .status("PEDIDO_CRIADO")
                .description("Pedido registrado no sistema")
                .timestamp(LocalDateTime.now())
                .build();
        
        trackingEventRepository.save(trackingEvent);
        log.info("Evento de tracking salvo para pedido criado: {}", orderId);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "order-updated-queue", durable = "true"),
            exchange = @Exchange(value = "order-exchange", type = "topic"),
            key = "order.updated"
    ))
    public void handleOrderUpdatedEvent(Map<String, Object> message) {
        log.info("Recebido evento de pedido atualizado: {}", message);
        
        Long orderId = Long.valueOf(message.get("orderId").toString());
        String status = message.get("status").toString();
        
        TrackingEvent trackingEvent = TrackingEvent.builder()
                .orderId(orderId)
                .status("PEDIDO_" + status)
                .description("Status do pedido atualizado para " + status)
                .timestamp(LocalDateTime.now())
                .build();
        
        trackingEventRepository.save(trackingEvent);
        log.info("Evento de tracking salvo para pedido atualizado: {}", orderId);
    }
}
