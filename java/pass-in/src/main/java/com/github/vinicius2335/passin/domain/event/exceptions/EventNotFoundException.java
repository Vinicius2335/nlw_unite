package com.github.vinicius2335.passin.domain.event.exceptions;

import java.io.Serial;

public class EventNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -7838827353459451686L;

    public EventNotFoundException(String message) {
        super(message);
    }
}
