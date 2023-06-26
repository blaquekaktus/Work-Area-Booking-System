package com.itkolleg.bookingsystem.exceptions.RessourceExceptions;

public class RessourceIsOccupied extends Throwable {

    public RessourceIsOccupied(String message){
        super(message);
    }
}
