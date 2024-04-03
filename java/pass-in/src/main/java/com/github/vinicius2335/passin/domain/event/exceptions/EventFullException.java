package com.github.vinicius2335.passin.domain.event.exceptions;

import java.io.Serial;

public class EventFullException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -427633076663876121L;

    public EventFullException(String message) {
        super(message);
    }
}
