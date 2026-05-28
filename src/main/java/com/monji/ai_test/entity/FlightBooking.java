package com.monji.ai_test.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Date;

@Entity
@Data
public class FlightBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String destination;

    private Instant departureTime;

    @Enumerated(EnumType.STRING)
    protected BookingStatus bookingStatus;

    @CreationTimestamp
    private Instant bookedAt;

}
