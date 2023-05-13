package com.itkolleg.bookingsystem.exceptions.RessourceExceptions;

import com.itkolleg.bookingsystem.exceptions.FormValidationExceptionDTO;

public class RessourceValidationException extends Throwable {

    private FormValidationExceptionDTO errors;

    public RessourceValidationException(String message){
        super("Invalid Ressource data entered: " + message);
    }

    public RessourceValidationException(FormValidationExceptionDTO errors){
        this.errors = errors;
    }
    public FormValidationExceptionDTO getErrorMap(){
        return errors;
    }

}
