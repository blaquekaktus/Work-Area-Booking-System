package com.itkolleg.bookingsystem.exceptions.EmployeeExceptions;

public class EmployeeAlreadyExistsException extends Exception {
    public EmployeeAlreadyExistsException(String s) {
        System.out.println("Mitarbeiter existiert bereits!");
    }
}
