package com.itkolleg.bookingsystem.service.TimeSlot;

import com.itkolleg.bookingsystem.domains.TimeSlot;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.util.List;

public interface TimeSlotService {
    Logger logger = LoggerFactory.getLogger(TimeSlotService.class);

    TimeSlot addTimeSlot(TimeSlot timeSlot);

    List<TimeSlot> getAllTimeSlots();

    TimeSlot getTimeSlotByName(String name) throws ResourceNotFoundException;

    TimeSlot getTimeSlotByStartTime(LocalTime startTime) throws ResourceNotFoundException;

    TimeSlot getTimeSlotByEndTime(LocalTime endTime) throws ResourceNotFoundException;

    TimeSlot updateTimeSlot(TimeSlot timeSlot) throws ResourceNotFoundException;

    void deleteTimeSlotById(Long id) throws ResourceNotFoundException;

    void deleteTimeSlotByName(String name) throws ResourceNotFoundException;
}