package com.github.vinicius2335.passin.dto.event;

public record EventRequestDTO (
        String title,
        String details,
        Integer maximunAttendees
) {
}
