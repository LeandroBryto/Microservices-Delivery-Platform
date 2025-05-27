package com.delivery.notifications.api.controller;

import com.delivery.notifications.domain.model.Notification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllNotifications() throws Exception {
        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testSendNotification() throws Exception {
        mockMvc.perform(post("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"orderId\":1,\"recipientEmail\":\"cliente@email.com\",\"subject\":\"Pedido em trânsito\",\"content\":\"Seu pedido está a caminho\",\"type\":\"ORDER_IN_TRANSIT\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.status").value("SENT"));
    }

    @Test
    public void testGetNotificationsByOrderId() throws Exception {
        // Primeiro cria uma notificação
        mockMvc.perform(post("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"orderId\":2,\"recipientEmail\":\"cliente@email.com\",\"subject\":\"Pedido entregue\",\"content\":\"Seu pedido foi entregue\",\"type\":\"ORDER_DELIVERED\"}"))
                .andExpect(status().isCreated());
        
        // Depois busca por orderId
        mockMvc.perform(get("/api/notifications/order/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
