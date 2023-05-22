package com.itkolleg.bookingsystem.exceptions.DeskExeceptions;

public class DeskValidationFailureException  extends Exception{

    public DeskValidationFailureException(String message) {
        super("Invalid Desk data entered: " + message);
    }
}