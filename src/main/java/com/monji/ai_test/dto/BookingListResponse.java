package com.monji.ai_test.dto;

import java.util.List;

public record BookingListResponse(List<BookingResponse> responses, String message) {
}
