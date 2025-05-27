package com.delivery.tracking.api.controller;

import com.delivery.tracking.domain.model.TrackingEvent;
import com.delivery.tracking.infrastructure.repository.TrackingEventRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tracking")
@RequiredArgsConstructor
@Tag(name = "Tracking", description = "API para rastreamento de entregas")
public class TrackingController {

    private final TrackingEventRepository trackingEventRepository;

    @GetMapping
    @Operation(summary = "Listar todos os eventos de rastreamento", description = "Retorna uma lista com todos os eventos de rastreamento")
    public ResponseEntity<List<TrackingEvent>> getAllEvents() {
        return ResponseEntity.ok(trackingEventRepository.findAll());
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Buscar eventos por pedido", description = "Retorna todos os eventos de rastreamento de um pedido específico")
    public ResponseEntity<List<TrackingEvent>> getEventsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(trackingEventRepository.findByOrderId(orderId));
    }

    @GetMapping("/driver/{driverId}")
    @Operation(summary = "Buscar eventos por motorista", description = "Retorna todos os eventos de rastreamento de um motorista específico")
    public ResponseEntity<List<TrackingEvent>> getEventsByDriverId(@PathVariable Long driverId) {
        return ResponseEntity.ok(trackingEventRepository.findByDriverId(driverId));
    }

    @PostMapping
    @Operation(summary = "Registrar novo evento", description = "Registra um novo evento de rastreamento no sistema")
    public ResponseEntity<TrackingEvent> createEvent(@Valid @RequestBody TrackingEvent event) {
        event.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(trackingEventRepository.save(event));
    }
}
