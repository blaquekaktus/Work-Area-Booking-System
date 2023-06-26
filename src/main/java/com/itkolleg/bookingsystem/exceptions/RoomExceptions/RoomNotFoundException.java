package com.itkolleg.bookingsystem.exceptions.RoomExceptions;

public class RoomNotFoundException extends Exception {

    public RoomNotFoundException(String s){
        super("Raum wurde nicht gefunden!");
    }
}
