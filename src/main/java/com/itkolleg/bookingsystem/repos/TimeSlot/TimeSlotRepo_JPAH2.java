package com.itkolleg.bookingsystem.repos.TimeSlot;

import com.itkolleg.bookingsystem.domains.TimeSlot;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
@ComponentScan({"com.itkolleg.repos"})
public class TimeSlotRepo_JPAH2 implements TimeSlotRepo {

    Logger logger = LoggerFactory.getLogger(TimeSlotRepo_JPAH2.class);
    TimeSlotJPARepo timeSlotJPARepo;

    public TimeSlotRepo_JPAH2(TimeSlotJPARepo timeSlotJPARepo) {
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
    public TimeSlot getTimeSlotByName(String name) throws ResourceNotFoundException {
        return this.timeSlotJPARepo.getTimeSlotByName(name);
    }

    @Override
    public TimeSlot getTimeSlotByStartTime(LocalTime startTime) throws ResourceNotFoundException {
        return this.timeSlotJPARepo.getTimeSlotByStartTime(startTime);
    }

    @Override
    public TimeSlot getTimeSlotByEndTime(LocalTime endTime) throws ResourceNotFoundException {
        return this.timeSlotJPARepo.getTimeSlotByEndTime(endTime);
    }

    @Override
    public TimeSlot updateTimeSlot(TimeSlot timeSlot) throws ResourceNotFoundException {
        return this.timeSlotJPARepo.updateTimeSlot(timeSlot);
    }

    @Override
    public void deleteTimeSlotById(Long id) throws ResourceNotFoundException {
        this.timeSlotJPARepo.deleteTimeSlotById(id);
    }

    @Override
    public void deleteTimeSlotByName(String name) {
        this.timeSlotJPARepo.deleteTimeSlotByName(name);
    }
}