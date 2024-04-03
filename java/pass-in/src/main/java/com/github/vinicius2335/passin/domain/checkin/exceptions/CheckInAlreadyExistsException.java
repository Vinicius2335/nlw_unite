package com.github.vinicius2335.passin.domain.checkin.exceptions;

import java.io.Serial;

public class CheckInAlreadyExistsException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -4382221256710555206L;

    public CheckInAlreadyExistsException(String message) {
        super(message);
    }
}
