package com.itkolleg.bookingsystem.Service.TimeSlot;

import com.itkolleg.bookingsystem.domains.TimeSlot;
import com.itkolleg.bookingsystem.exceptions.TimeSlot.TimeSlotNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.util.List;

public interface TimeSlotService {
    Logger logger = LoggerFactory.getLogger(TimeSlotService.class);
    TimeSlot addTimeSlot(TimeSlot timeSlot);
    List<TimeSlot> getAllTimeSlots();
    TimeSlot getTimeSlotByName(String name)throws TimeSlotNotFoundException;
    TimeSlot getTimeSlotByStartTime(LocalTime startTime)throws TimeSlotNotFoundException;
    TimeSlot getTimeSlotByEndTime(LocalTime endTime)throws TimeSlotNotFoundException;
    TimeSlot updateTimeSlot(TimeSlot timeSlot) throws TimeSlotNotFoundException;
    void deleteTimeSlotById(Long id) throws TimeSlotNotFoundException;

    void deleteTimeSlotByName(String name) throws TimeSlotNotFoundException;
}