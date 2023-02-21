package com.itkolleg.bookingsystem.exceptions.RessourceExceptions;

public class RessourceNotFoundException extends Exception {
    public RessourceNotFoundException() {
        super("Ressource not found!");
    }
}
