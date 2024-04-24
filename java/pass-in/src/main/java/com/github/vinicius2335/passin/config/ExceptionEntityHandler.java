package com.github.vinicius2335.passin.config;

import com.github.vinicius2335.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import com.github.vinicius2335.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import com.github.vinicius2335.passin.domain.checkin.exceptions.CheckInAlreadyExistsException;
import com.github.vinicius2335.passin.domain.event.exceptions.EventFullException;
import com.github.vinicius2335.passin.domain.event.exceptions.EventNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionEntityHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EventNotFoundException.class)
    public ProblemDetail handleEventNotFound(EventNotFoundException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle(ex.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(EventFullException.class)
    public ProblemDetail handleEventFull(EventFullException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle(ex.getMessage());

        return problemDetail;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle("One or more fields are invalid");

        Map<String, String> fields = ex.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage
                ));
        problemDetail.setProperty("fields", fields);

        return super.handleExceptionInternal(ex, problemDetail, headers, status, request);
    }

    @ExceptionHandler(AttendeeNotFoundException.class)
    public ProblemDetail handleAttendeeNotFound(AttendeeNotFoundException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle(ex.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(AttendeeAlreadyExistException.class)
    public ProblemDetail handleAttendeeAlreadyExist(AttendeeAlreadyExistException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle(ex.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(CheckInAlreadyExistsException.class)
    public ProblemDetail handleCheckInAlreadyExists(CheckInAlreadyExistsException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle(ex.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Some field of the request has a resource in use");

        return problemDetail;
    }
}
