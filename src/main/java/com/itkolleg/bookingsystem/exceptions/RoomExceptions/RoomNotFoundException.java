package com.itkolleg.bookingsystem.exceptions.RoomExceptions;

public class RoomNotFoundException extends Exception {

    public RoomNotFoundException() {
        super("Raum wurde nicht gefunden!");
    }
}
