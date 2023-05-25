package com.itkolleg.bookingsystem.repos.TimeSlot;

import com.itkolleg.bookingsystem.domains.TimeSlot;
import com.itkolleg.bookingsystem.exceptions.TimeSlot.TimeSlotNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class TimeSlotDBAccess_JPAH2 implements TimeSlotDBAccess {
    TimeSlotJPARepo timeSlotJPARepo;

    public TimeSlotDBAccess_JPAH2(TimeSlotJPARepo timeSlotJPARepo) {
        this.timeSlotJPARepo = timeSlotJPARepo;
    }

    @Override
    public TimeSlot addTimeSlot(TimeSlot timeSlot) {
        return this.timeSlotJPARepo.save(timeSlot);
    }

    @Override
    public List<TimeSlot> getAllTimeSlots() {
        return this.timeSlotJPARepo.findAll();
    }

    @Override
    public TimeSlot getTimeSlotByName(String name) throws TimeSlotNotFoundException {
        return this.timeSlotJPARepo.getTimeSlotByName(name);
    }

    @Override
    public TimeSlot getTimeSlotByStartTime(LocalTime startTime) throws TimeSlotNotFoundException {
        return this.timeSlotJPARepo.getTimeSlotByStartTime(startTime);
    }

    @Override
    public TimeSlot getTimeSlotByEndTime(LocalTime endTime) throws TimeSlotNotFoundException {
        return this.timeSlotJPARepo.getTimeSlotByEndTime(endTime);
    }

    @Override
    public TimeSlot updateTimeSlot(TimeSlot timeSlot) throws TimeSlotNotFoundException {
        return this.timeSlotJPARepo.updateTimeSlot(timeSlot);
    }

    @Override
    public void deleteTimeSlotById(Long id) throws TimeSlotNotFoundException {
        this.timeSlotJPARepo.deleteTimeSlotById(id);
    }

    @Override
    public void deleteTimeSlotByName(String name) {
        this.timeSlotJPARepo.deleteTimeSlotByName(name);
    }
}