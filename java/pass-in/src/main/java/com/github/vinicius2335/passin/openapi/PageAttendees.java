package com.github.vinicius2335.passin.openapi;

import com.github.vinicius2335.passin.dto.attendee.AttendeeDetail;

import java.util.List;

public record PageAttendees(
        Integer currentItens,
        Integer currentPage,
        Integer totalItens,
        Integer totalPages,
        List<AttendeeDetail> attendees
) {
}
