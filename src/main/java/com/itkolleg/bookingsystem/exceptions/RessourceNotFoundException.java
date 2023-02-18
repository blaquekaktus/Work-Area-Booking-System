package com.itkolleg.bookingsystem.exceptions;

public class RessourceNotFoundException extends Exception {
    public RessourceNotFoundException() {
        super("Ressource not found!");
    }
}
