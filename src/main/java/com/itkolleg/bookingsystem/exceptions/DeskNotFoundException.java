package com.itkolleg.bookingsystem.exceptions;

public class DeskNotFoundException extends Exception{
    public DeskNotFoundException() {
        super("Desk not found!");
    }
}
