package com.itkolleg.bookingsystem.exceptions.BookingExceptions;

public class BookingNotFoundException extends Exception{
    public BookingNotFoundException(String message) {
        super("Booking Not Found!");
    }
}
