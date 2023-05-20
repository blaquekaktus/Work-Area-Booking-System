package com.itkolleg.bookingsystem.exceptions.DeskExceptions;

public class DeskNotFoundException extends Exception{

    public DeskNotFoundException(String message) {
        super("Desk Not Found!");
    }
}