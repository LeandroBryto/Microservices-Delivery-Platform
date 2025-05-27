package com.delivery.orders.infrastructure.messaging;

import com.delivery.orders.domain.model.Order;
import com.delivery.orders.infrastructure.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final OrderRepository orderRepository;

    private static final String ORDER_EXCHANGE = "order-exchange";
    private static final String ORDER_CREATED_ROUTING_KEY = "order.created";
    private static final String ORDER_UPDATED_ROUTING_KEY = "order.updated";

    public void publishOrderCreatedEvent(Order order) {
        log.info("Publicando evento de pedido criado: {}", order.getId());
        
        Map<String, Object> message = new HashMap<>();
        message.put("orderId", order.getId());
        message.put("customerName", order.getCustomerName());
        message.put("customerAddress", order.getCustomerAddress());
        message.put("status", order.getStatus().toString());
        message.put("createdAt", order.getCreatedAt().toString());
        
        rabbitTemplate.convertAndSend(ORDER_EXCHANGE, ORDER_CREATED_ROUTING_KEY, message);
    }

    public void publishOrderUpdatedEvent(Order order) {
        log.info("Publicando evento de pedido atualizado: {}", order.getId());
        
        Map<String, Object> message = new HashMap<>();
        message.put("orderId", order.getId());
        message.put("status", order.getStatus().toString());
        message.put("updatedAt", order.getUpdatedAt().toString());
        
        rabbitTemplate.convertAndSend(ORDER_EXCHANGE, ORDER_UPDATED_ROUTING_KEY, message);
    }
}
