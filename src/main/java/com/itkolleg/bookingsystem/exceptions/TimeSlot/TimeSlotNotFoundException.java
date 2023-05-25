package com.itkolleg.bookingsystem.exceptions.TimeSlot;

public class TimeSlotNotFoundException extends Exception {
    public TimeSlotNotFoundException(String message) {
        super("Time Slot Not Found!" + message);
    }
}
