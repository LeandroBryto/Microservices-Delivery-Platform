package com.delivery.tracking.api.controller;

import com.delivery.tracking.domain.model.TrackingEvent;
import com.delivery.tracking.infrastructure.repository.TrackingEventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrackingController.class)
public class TrackingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrackingEventRepository trackingEventRepository;

    @Test
    public void testGetAllEvents() throws Exception {
        TrackingEvent event1 = new TrackingEvent();
        event1.setId("1");
        event1.setOrderId(1L);
        event1.setStatus("EM_TRANSITO");
        
        TrackingEvent event2 = new TrackingEvent();
        event2.setId("2");
        event2.setOrderId(2L);
        event2.setStatus("ENTREGUE");
        
        List<TrackingEvent> events = Arrays.asList(event1, event2);
        
        when(trackingEventRepository.findAll()).thenReturn(events);
        
        mockMvc.perform(get("/api/tracking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"));
    }

    @Test
    public void testGetEventsByOrderId() throws Exception {
        TrackingEvent event1 = new TrackingEvent();
        event1.setId("1");
        event1.setOrderId(1L);
        event1.setStatus("EM_TRANSITO");
        
        TrackingEvent event2 = new TrackingEvent();
        event2.setId("2");
        event2.setOrderId(1L);
        event2.setStatus("ENTREGUE");
        
        List<TrackingEvent> events = Arrays.asList(event1, event2);
        
        when(trackingEventRepository.findByOrderId(1L)).thenReturn(events);
        
        mockMvc.perform(get("/api/tracking/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(1))
                .andExpect(jsonPath("$[1].orderId").value(1));
    }

    @Test
    public void testCreateEvent() throws Exception {
        TrackingEvent event = new TrackingEvent();
        event.setId("1");
        event.setOrderId(1L);
        event.setDriverId(1L);
        event.setStatus("EM_TRANSITO");
        event.setTimestamp(LocalDateTime.now());
        
        when(trackingEventRepository.save(any(TrackingEvent.class))).thenReturn(event);
        
        mockMvc.perform(post("/api/tracking")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"orderId\":1,\"driverId\":1,\"status\":\"EM_TRANSITO\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.orderId").value(1));
    }
}
