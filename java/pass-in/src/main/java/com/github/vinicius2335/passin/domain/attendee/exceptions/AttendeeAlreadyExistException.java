package com.github.vinicius2335.passin.domain.attendee.exceptions;

import java.io.Serial;

public class AttendeeAlreadyExistException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -280773796608369516L;

    public AttendeeAlreadyExistException(String message) {
        super(message);
    }
}
