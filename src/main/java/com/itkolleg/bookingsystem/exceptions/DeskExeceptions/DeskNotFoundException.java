package com.itkolleg.bookingsystem.exceptions.DeskExeceptions;

public class DeskNotFoundException extends Exception{
    public DeskNotFoundException() {
        super("Desk not found!");
    }
}
