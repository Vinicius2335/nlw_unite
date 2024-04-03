package com.github.vinicius2335.passin.domain.attendee.exceptions;

import java.io.Serial;

public class AttendeeNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 3200984078596999416L;

    public AttendeeNotFoundException(String message) {
        super(message);
    }
}
