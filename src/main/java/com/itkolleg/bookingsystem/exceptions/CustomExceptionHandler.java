package com.itkolleg.bookingsystem.exceptions;

import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskNotFoundException;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskValidationFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(value = {DeskNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handleDeskNotFoundException(DeskNotFoundException ex, WebRequest request) {
        LOGGER.error("Desk not found exception: ", ex);
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {DeskDeletionFailureException.class, DeskValidationFailureException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleDeskDeletionFailureException(RuntimeException ex, WebRequest request) {
        LOGGER.error("Desk deletion/validation failure exception: ", ex);
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    // Add more exception handlers here as needed

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleUnknownException(Exception ex, WebRequest request) {
        LOGGER.error("Unknown exception: ", ex);
        return handleExceptionInternal(ex, "An unexpected error occurred.", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}