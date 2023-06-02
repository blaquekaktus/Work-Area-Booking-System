package com.itkolleg.bookingsystem.repos.TimeSlot;

import com.itkolleg.bookingsystem.domains.TimeSlot;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface TimeSlotJPARepo extends JpaRepository<TimeSlot, Long> {
    TimeSlot getTimeSlotByStartTime(LocalTime startTime) throws ResourceNotFoundException;

    TimeSlot getTimeSlotByEndTime(LocalTime endTime) throws ResourceNotFoundException;

    TimeSlot getTimeSlotByName(String name) throws ResourceNotFoundException;

    default TimeSlot updateTimeSlot(TimeSlot timeslot) throws ResourceNotFoundException{
        return null;
    }

    void deleteTimeSlotById(Long id) throws ResourceNotFoundException;

    void deleteTimeSlotByName(String name);
}