package com.itkolleg.bookingsystem.repos.TimeSlot;

import com.itkolleg.bookingsystem.domains.TimeSlot;
import com.itkolleg.bookingsystem.exceptions.DatabaseOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Component
@ComponentScan({"com.itkolleg.repos"})
public class TimeSlotRepo_JPAH2 implements TimeSlotRepo {

    private static final Logger logger = LoggerFactory.getLogger(TimeSlotRepo_JPAH2.class);
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
    public Optional<TimeSlot> getTimeSlotByName(String name) {
        return this.timeSlotJPARepo.findByName(name);
    }

    @Override
    public Optional<TimeSlot> getTimeSlotByStartTime(LocalTime startTime) {
        return this.timeSlotJPARepo.findByStartTime(startTime);
    }

    @Override
    public Optional<TimeSlot> getTimeSlotByEndTime(LocalTime endTime) {
        return this.timeSlotJPARepo.findByEndTime(endTime);
    }

    @Override
    public Optional<TimeSlot> updateTimeSlot(TimeSlot timeSlot) {
        return Optional.of(this.timeSlotJPARepo.saveAndFlush(timeSlot));
    }

    @Override
    public void deleteTimeSlotById(Long id) {
        this.timeSlotJPARepo.deleteById(id);
    }

    @Override
    public void deleteTimeSlotByName(String name) {
        this.timeSlotJPARepo.deleteTimeSlotByName(name);
    }

    @Override
    public void delete(TimeSlot toDelete) throws DatabaseOperationException {
        this.timeSlotJPARepo.delete(toDelete);
    }
}