package com.itkolleg.bookingsystem.exceptions.RessourceExceptions;

public class RessourceAlreadyExistsException extends Throwable {
    public RessourceAlreadyExistsException(String s) {
    System.out.println("Die von Ihnen angegebene Ressource existiert bereits!");
}
}
