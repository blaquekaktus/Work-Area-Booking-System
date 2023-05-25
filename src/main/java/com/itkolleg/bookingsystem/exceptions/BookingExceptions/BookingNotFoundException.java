package com.itkolleg.bookingsystem.exceptions.BookingExceptions;

public class BookingNotFoundException extends Exception {

    public BookingNotFoundException(String message) {
        super(message);
    }

    public BookingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}