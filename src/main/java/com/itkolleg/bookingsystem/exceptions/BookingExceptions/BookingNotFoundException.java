package com.itkolleg.bookingsystem.exceptions.BookingExceptions;

public class BookingNotFoundException extends Exception{
    public BookingNotFoundException(){
        super("Booking not found!");
    }
}
