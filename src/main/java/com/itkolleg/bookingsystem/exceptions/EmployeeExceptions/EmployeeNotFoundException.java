package com.itkolleg.bookingsystem.exceptions.EmployeeExceptions;

public class EmployeeNotFoundException extends Exception {

    public EmployeeNotFoundException(String s){

        super("Mitarbeiter nicht gefunden!");
    }
}
