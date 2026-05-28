package com.monji.ai_test.dto;

import com.monji.ai_test.entity.BookingStatus;

import java.time.Instant;

public record BookingResponse(Long id, String destination, Instant departureTime, BookingStatus bookingStatus) { }
