package com.delivery.orders.infrastructure.repository;

import com.delivery.orders.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByDriverId(Long driverId);
    List<Order> findByStatus(Order.OrderStatus status);
}
