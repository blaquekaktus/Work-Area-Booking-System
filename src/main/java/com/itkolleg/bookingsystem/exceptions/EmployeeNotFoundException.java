package com.itkolleg.bookingsystem.exceptions;

public class EmployeeNotFoundException extends Exception{

    public EmployeeNotFoundException(){
        super("Employee not found!");
    }
}
