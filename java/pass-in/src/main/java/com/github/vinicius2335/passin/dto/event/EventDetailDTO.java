package com.github.vinicius2335.passin.dto.event;

public record EventDetailDTO(
        String id,
        String title,
        String details,
        String slug,
        Integer maximunAttendees,
        Integer attendeesAmount
) {
}
