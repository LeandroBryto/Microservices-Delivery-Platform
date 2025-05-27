package com.delivery.notifications.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    
    private String id;
    private Long orderId;
    private String recipientEmail;
    private String recipientPhone;
    private String subject;
    private String content;
    private NotificationType type;
    private NotificationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    
    public enum NotificationType {
        ORDER_CREATED,
        ORDER_ASSIGNED,
        ORDER_IN_TRANSIT,
        ORDER_DELIVERED,
        ORDER_DELAYED,
        ORDER_CANCELLED
    }
    
    public enum NotificationStatus {
        PENDING,
        SENT,
        FAILED
    }
}
