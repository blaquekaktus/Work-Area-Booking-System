package com.itkolleg.bookingsystem.exceptions.DeskExeceptions;

import com.itkolleg.bookingsystem.exceptions.FormValidationExceptionDTO;

public class DeskValidationException extends Exception{

    private FormValidationExceptionDTO errors;
    public DeskValidationException(String message){
        super("Eingegebene Tischdaten sind inkorrekt: " + message);
    }

    public DeskValidationException(FormValidationExceptionDTO errors){
        this.errors = errors;
    }
    public FormValidationExceptionDTO getErrorMap(){
        return errors;
    }
}