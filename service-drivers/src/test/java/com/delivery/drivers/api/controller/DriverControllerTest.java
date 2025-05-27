package com.delivery.drivers.api.controller;

import com.delivery.drivers.domain.model.Driver;
import com.delivery.drivers.infrastructure.repository.DriverRepository;
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

@WebMvcTest(DriverController.class)
public class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DriverRepository driverRepository;

    @Test
    public void testGetAllDrivers() throws Exception {
        Driver driver1 = new Driver();
        driver1.setId(1L);
        driver1.setName("Motorista 1");
        driver1.setStatus(Driver.DriverStatus.AVAILABLE);
        
        Driver driver2 = new Driver();
        driver2.setId(2L);
        driver2.setName("Motorista 2");
        driver2.setStatus(Driver.DriverStatus.BUSY);
        
        when(driverRepository.findAll()).thenReturn(Arrays.asList(driver1, driver2));
        
        mockMvc.perform(get("/api/drivers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    public void testGetDriverById() throws Exception {
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setName("Motorista 1");
        driver.setEmail("motorista1@email.com");
        driver.setPhone("11999999999");
        driver.setStatus(Driver.DriverStatus.AVAILABLE);
        driver.setCreatedAt(LocalDateTime.now());
        
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        
        mockMvc.perform(get("/api/drivers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Motorista 1"));
    }

    @Test
    public void testCreateDriver() throws Exception {
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setName("Motorista 1");
        driver.setEmail("motorista1@email.com");
        driver.setPhone("11999999999");
        driver.setStatus(Driver.DriverStatus.AVAILABLE);
        driver.setCreatedAt(LocalDateTime.now());
        
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);
        
        mockMvc.perform(post("/api/drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Motorista 1\",\"email\":\"motorista1@email.com\",\"phone\":\"11999999999\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Motorista 1"));
    }
}
