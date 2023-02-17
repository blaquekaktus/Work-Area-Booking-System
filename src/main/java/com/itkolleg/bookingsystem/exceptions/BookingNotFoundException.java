package com.itkolleg.bookingsystem.exceptions;

public class BookingNotFoundException extends Exception{
    public BookingNotFoundException(){
        super("Booking not found!");
    }
}
