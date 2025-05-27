package com.delivery.drivers.api.controller;

import com.delivery.drivers.domain.model.Driver;
import com.delivery.drivers.infrastructure.repository.DriverRepository;
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
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
@Tag(name = "Drivers", description = "API para gerenciamento de motoristas")
public class DriverController {

    private final DriverRepository driverRepository;

    @GetMapping
    @Operation(summary = "Listar todos os motoristas", description = "Retorna uma lista com todos os motoristas cadastrados")
    public ResponseEntity<List<Driver>> getAllDrivers() {
        return ResponseEntity.ok(driverRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar motorista por ID", description = "Retorna um motorista específico pelo seu ID")
    public ResponseEntity<Driver> getDriverById(@PathVariable Long id) {
        return driverRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar motoristas por status", description = "Retorna uma lista de motoristas filtrados por status")
    public ResponseEntity<List<Driver>> getDriversByStatus(@PathVariable Driver.DriverStatus status) {
        return ResponseEntity.ok(driverRepository.findByStatus(status));
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo motorista", description = "Cadastra um novo motorista no sistema")
    public ResponseEntity<Driver> createDriver(@Valid @RequestBody Driver driver) {
        driver.setCreatedAt(LocalDateTime.now());
        driver.setStatus(Driver.DriverStatus.AVAILABLE);
        return ResponseEntity.status(HttpStatus.CREATED).body(driverRepository.save(driver));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar motorista", description = "Atualiza os dados de um motorista existente")
    public ResponseEntity<Driver> updateDriver(@PathVariable Long id, @Valid @RequestBody Driver driver) {
        return driverRepository.findById(id)
                .map(existingDriver -> {
                    driver.setId(id);
                    driver.setUpdatedAt(LocalDateTime.now());
                    return ResponseEntity.ok(driverRepository.save(driver));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir motorista", description = "Remove um motorista do sistema")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        return driverRepository.findById(id)
                .map(driver -> {
                    driverRepository.delete(driver);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
