package com.itkolleg.bookingsystem.exceptions;

public class EmployeeValidationException extends Exception{

    private FormValidationExceptionDTO errors;

    public EmployeeValidationException(String message){
        super("Invalid Employee data entered: " + message);
    }

    public EmployeeValidationException(FormValidationExceptionDTO errors){
        this.errors = errors;
    }
    public FormValidationExceptionDTO getErrorMap(){
        return errors;
    }

}
