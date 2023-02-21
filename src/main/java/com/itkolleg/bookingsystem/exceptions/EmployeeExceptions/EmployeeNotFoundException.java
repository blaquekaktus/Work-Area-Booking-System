package com.itkolleg.bookingsystem.exceptions.EmployeeExceptions;

public class EmployeeNotFoundException extends Exception{

    public EmployeeNotFoundException(){
        super("Employee not found!");
    }
}
