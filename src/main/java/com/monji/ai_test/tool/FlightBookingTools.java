package com.monji.ai_test.tool;

import com.monji.ai_test.dto.BookingListResponse;
import com.monji.ai_test.dto.BookingResponse;
import com.monji.ai_test.entity.BookingStatus;
import com.monji.ai_test.entity.FlightBooking;
import com.monji.ai_test.service.FlightBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FlightBookingTools {

    private final FlightBookingService flightBookingService;

    @Tool(name = "Flight_booking_tool", description = "This tool is used to book flights")
    public BookingResponse bookFlight(
            @ToolParam(description = "Unique user id (e.g. userId can be user123)") String userId,
            @ToolParam(description = "The destination for the flight booking (e.g. it can be a city like Delhi, London, New York, etc.)") String destination,
            @ToolParam(description = "Departure date and time in ISO-8601 format (e.g. 2025-12-25T14:30:00Z)") Instant departureTime)
    {
        var flightBooking = flightBookingService.createBooking(userId, destination, departureTime);

        return new BookingResponse(flightBooking.getId(), flightBooking.getDestination(), flightBooking.getDepartureTime(), flightBooking.getBookingStatus());
    }

    @Tool(name = "get_user_booking",
    description = "Retrieve all flight bookings for the current user, sorted by departure time (most recent first) and it Returns an empty list message if none exists")
    public BookingListResponse getUserBookings(@ToolParam(description = "the Unique userID", required = true) String userId) {
        List<FlightBooking> bookings = flightBookingService.findBookingsByUserId(userId);

        List<BookingResponse> bookingResponses = bookings.stream().map(b-> new BookingResponse(b.getId(),
                b.getDestination(),b.getDepartureTime(),b.getBookingStatus())).toList();

        String message = bookings.isEmpty() ? "You have no upcoming flight bookings." : "Here are your current flight bookings.";

        return new BookingListResponse(bookingResponses, message);
    }

    @Tool(name = "update_booking_status",
            description = "Update the status of an existing flight booking(e.g. cancel it or confirm it. Commong use: set status of CANCELLED or CONFIRMED")
    public BookingResponse updateBookingStatus(@ToolParam(description = "The bookingId returned from create or get bookings", required = true) Long bookingId,
                                                   @ToolParam(description = "The Unique UserId of the user who owns the booking") String userId,
                                                   @ToolParam(description = "new Status : CONFIRMED, CANCELLED OR PENDING", required = true) BookingStatus newStatus) {
        FlightBooking updated = flightBookingService.updateBookingStatus(bookingId, userId, newStatus);
        return new BookingResponse(updated.getId(), updated.getDestination(), updated.getDepartureTime(), updated.getBookingStatus());
    }

}
