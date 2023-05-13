package com.itkolleg.bookingsystem.repos.TimeSlot;

import com.itkolleg.bookingsystem.domains.TimeSlot;
import com.itkolleg.bookingsystem.exceptions.TimeSlot.TimeSlotNotFoundException;

import java.time.LocalTime;
import java.util.List;

public interface TimeSlotDBAccess {
    TimeSlot addTimeSlot(TimeSlot timeSlot);
    List<TimeSlot> getAllTimeSlots();
    TimeSlot getTimeSlotByName(String name)throws TimeSlotNotFoundException;
    TimeSlot getTimeSlotByStartTime(LocalTime startTime) throws TimeSlotNotFoundException;
    TimeSlot getTimeSlotByEndTime(LocalTime endTime) throws TimeSlotNotFoundException;
    TimeSlot updateTimeSlot(TimeSlot timeSlot) throws TimeSlotNotFoundException;
    void deleteTimeSlotById(Long Id) throws TimeSlotNotFoundException;
    void deleteTimeSlotByName(String name);
}