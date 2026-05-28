package com.monji.ai_test.service;

import com.monji.ai_test.entity.BookingStatus;
import com.monji.ai_test.entity.FlightBooking;
import com.monji.ai_test.repository.FlightBookingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FlightBookingService {

    private final FlightBookingRepository flightBookingRepository;

    public FlightBooking createBooking(String userId, String destination, Instant departureTime) {
        boolean exsits = flightBookingRepository.existsByUserIdAndDestinationAndDepartureTime(userId, destination, departureTime);
        if(exsits){
            throw new IllegalArgumentException("You already have a Booking to " + destination + " at " + departureTime);
        }

        FlightBooking booking = new FlightBooking();
        booking.setDestination(destination);
        booking.setUserId(userId);
        booking.setDepartureTime(departureTime);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        return flightBookingRepository.save(booking);

    }

    public List<FlightBooking> findBookingsByUserId(String userId) {
        return flightBookingRepository.findByUserIdOrderByDepartureTimeDesc(userId);
    }

    public FlightBooking updateBookingStatus(Long bookingId, String userId, BookingStatus newStatus) {
        FlightBooking booking = flightBookingRepository.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Booking id " + bookingId + " not found"));
        if(!booking.getUserId().equals(userId)){
            throw new IllegalArgumentException("You can only modify your own bookings");
        }

        booking.setBookingStatus(newStatus);
        return flightBookingRepository.save(booking);
    }
}
