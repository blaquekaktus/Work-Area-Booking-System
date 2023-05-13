package com.itkolleg.bookingsystem.repos.TimeSlot;

import java.time.LocalTime;

@Repository
public interface TimeSlotJPARepo extends JpaRepository<TimeSlot, Long> {
    TimeSlot getTimeSlotByStartTime(LocalTime startTime) throws TimeSlotNotFoundException;

    TimeSlot getTimeSlotByEndTime(LocalTime endTime) throws TimeSlotNotFoundException;
    TimeSlot getTimeSlotByName(String name) throws TimeSlotNotFoundException;

    default TimeSlot updateTimeSlot(TimeSlot timeslot) throws TimeSlotNotFoundException {
        return null;
    }

    void deleteTimeSlotById(Long id) throws TimeSlotNotFoundException;
    void deleteTimeSlotByName(String name);
}