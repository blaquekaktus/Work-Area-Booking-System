package com.itkolleg.bookingsystem.exceptions.DeskExeceptions;

public class DeskAlreadyExistsException extends Exception {
    public DeskAlreadyExistsException(String s) { super("This desk already exists in the database!");
    }
}
