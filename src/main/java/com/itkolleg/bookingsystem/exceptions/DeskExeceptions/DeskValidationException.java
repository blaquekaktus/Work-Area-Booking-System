package com.itkolleg.bookingsystem.exceptions.DeskExeceptions;

public class DeskValidationException extends Exception{

    public DeskValidationException(String message){
        super("Invalid Desk data entered: " + message);
    }
}