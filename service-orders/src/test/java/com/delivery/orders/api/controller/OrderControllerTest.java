package com.delivery.orders.api.controller;

import com.delivery.orders.domain.model.Order;
import com.delivery.orders.infrastructure.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    public void testGetAllOrders() throws Exception {
        Order order1 = new Order();
        order1.setId(1L);
        order1.setCustomerName("Cliente 1");
        order1.setStatus(Order.OrderStatus.CREATED);
        
        Order order2 = new Order();
        order2.setId(2L);
        order2.setCustomerName("Cliente 2");
        order2.setStatus(Order.OrderStatus.IN_TRANSIT);
        
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));
        
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    public void testGetOrderById() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setCustomerName("Cliente 1");
        order.setCustomerAddress("Endereço 1");
        order.setStatus(Order.OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());
        
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerName").value("Cliente 1"));
    }

    @Test
    public void testCreateOrder() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setCustomerName("Cliente 1");
        order.setCustomerAddress("Endereço 1");
        order.setStatus(Order.OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());
        
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"customerName\":\"Cliente 1\",\"customerAddress\":\"Endereço 1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerName").value("Cliente 1"));
    }
}
