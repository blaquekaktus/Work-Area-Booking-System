package com.itkolleg.bookingsystem.exceptions.EmployeeExceptions;

public class EmployeeAlreadyExistsException extends Throwable {
    public EmployeeAlreadyExistsException(String s) {
        System.out.println("Mitarbeiter existiert bereits!");
    }
}
