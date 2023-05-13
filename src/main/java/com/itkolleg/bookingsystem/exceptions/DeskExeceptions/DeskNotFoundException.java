package com.itkolleg.bookingsystem.exceptions.DeskExeceptions;

public class DeskNotFoundException extends Exception{

    public DeskNotFoundException(String message) {
        super("Desk Not Found!");
    }
}