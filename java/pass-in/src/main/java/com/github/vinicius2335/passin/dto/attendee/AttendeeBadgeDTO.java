package com.github.vinicius2335.passin.dto.attendee;

public record AttendeeBadgeDTO(
        String name,
        String email,
        String checkInUrl,
        String eventId,
        String eventTitle
) {
}
