package com.itkolleg.bookingsystem.exceptions.DeskExceptions;

public class DeskAlreadyExistsException extends Exception {
    public DeskAlreadyExistsException(String s) { super("This desk already exists in the database!");
    }
}
