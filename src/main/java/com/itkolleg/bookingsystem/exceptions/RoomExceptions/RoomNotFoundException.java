package com.itkolleg.bookingsystem.exceptions.RoomExceptions;

public class RoomNotFoundException extends Exception {

    public RoomNotFoundException(){
        super("Room not found!");
    }
}
