package com.github.vinicius2335.passin.dto.attendee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AttendeeRequestDTO (
        @NotBlank(message = "Attendee 'name' cannot be null or empty")
        String name,

        @NotBlank(message = "Attendee 'e-mail' cannot be null or empty")
        @Email(message = "Attendee 'e-mail' cannot be invalid")
        String email
){
}
