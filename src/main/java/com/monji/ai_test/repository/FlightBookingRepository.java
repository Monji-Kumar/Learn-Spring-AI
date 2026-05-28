package com.monji.ai_test.repository;

import com.monji.ai_test.entity.FlightBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface FlightBookingRepository extends JpaRepository<FlightBooking, Long> {

    List<FlightBooking> findByUserIdOrderByDepartureTimeDesc(String userId);

    boolean existsByUserIdAndDestinationAndDepartureTime(String userId, String destination, Instant departureTime);
}
