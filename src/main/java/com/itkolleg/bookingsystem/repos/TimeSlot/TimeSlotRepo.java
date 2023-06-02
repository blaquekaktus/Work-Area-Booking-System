package com.itkolleg.bookingsystem.repos.TimeSlot;

import com.itkolleg.bookingsystem.domains.TimeSlot;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;

import java.time.LocalTime;
import java.util.List;

public interface TimeSlotRepo {
    TimeSlot addTimeSlot(TimeSlot timeSlot);

    List<TimeSlot> getAllTimeSlots();

    TimeSlot getTimeSlotByName(String name) throws ResourceNotFoundException;

    TimeSlot getTimeSlotByStartTime(LocalTime startTime) throws ResourceNotFoundException;

    TimeSlot getTimeSlotByEndTime(LocalTime endTime) throws ResourceNotFoundException;

    TimeSlot updateTimeSlot(TimeSlot timeSlot) throws ResourceNotFoundException;

    void deleteTimeSlotById(Long Id) throws ResourceNotFoundException;

    void deleteTimeSlotByName(String name);
}