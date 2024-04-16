package com.github.vinicius2335.passin.dto.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record EventRequestDTO (
        @NotBlank(message = "Event 'title' cannot be null or empty")
        String title,

        @NotBlank(message = "Event 'details' cannot be null or empty")
        String details,

        @Positive(message = "Event 'maximunAttendees' cannot be equals zero or negative")
        Integer maximunAttendees
) {
}
