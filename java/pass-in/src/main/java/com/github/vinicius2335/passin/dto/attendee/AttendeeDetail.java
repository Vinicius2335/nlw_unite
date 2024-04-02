package com.github.vinicius2335.passin.dto.attendee;

import java.time.OffsetDateTime;

public record AttendeeDetail(
        String id,
        String name,
        String email,
        OffsetDateTime createdAt,
        OffsetDateTime checkInAt
) {
}
