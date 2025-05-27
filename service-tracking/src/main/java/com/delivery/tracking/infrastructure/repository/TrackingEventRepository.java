package com.delivery.tracking.infrastructure.repository;

import com.delivery.tracking.domain.model.TrackingEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackingEventRepository extends MongoRepository<TrackingEvent, String> {
    List<TrackingEvent> findByOrderId(Long orderId);
    List<TrackingEvent> findByDriverId(Long driverId);
    List<TrackingEvent> findByStatus(String status);
}
