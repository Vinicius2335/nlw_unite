package com.github.vinicius2335.passin.dto.attendee;

import java.util.List;

public record PageAttendeesResponse(
        Integer currentItens,
        Integer currentPage,
        Long totalItens,
        Integer totalPages,
        List<AttendeeDetail> attendees
) {
}
