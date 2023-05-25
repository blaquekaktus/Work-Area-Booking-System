package com.itkolleg.bookingsystem.service.TimeSlot;

import com.itkolleg.bookingsystem.domains.TimeSlot;
import com.itkolleg.bookingsystem.exceptions.TimeSlot.TimeSlotNotFoundException;
import com.itkolleg.bookingsystem.repos.Desk.DeskRepo;
import com.itkolleg.bookingsystem.repos.Employee.EmployeeDBAccess;
import com.itkolleg.bookingsystem.repos.TimeSlot.TimeSlotRepo;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class TimeSlotServiceImplementation implements TimeSlotService {
    private final TimeSlotRepo timeSlotRepo;

    public TimeSlotServiceImplementation(TimeSlotRepo timeSlotRepo, DeskRepo deskRepo, EmployeeDBAccess employeeDBAccess) {
        this.timeSlotRepo = timeSlotRepo;
    }

    @Override
    public TimeSlot addTimeSlot(TimeSlot timeSlot) {
        return this.timeSlotRepo.addTimeSlot(timeSlot);
    }

    @Override
    public List<TimeSlot> getAllTimeSlots() {
        return this.timeSlotRepo.getAllTimeSlots();
    }

    @Override
    public TimeSlot getTimeSlotByName(String name) throws TimeSlotNotFoundException {
        return this.timeSlotRepo.getTimeSlotByName(name);
    }

    @Override
    public TimeSlot getTimeSlotByStartTime(LocalTime startTime) throws TimeSlotNotFoundException {
        return this.timeSlotRepo.getTimeSlotByStartTime(startTime);
    }

    @Override
    public TimeSlot getTimeSlotByEndTime(LocalTime endTime) throws TimeSlotNotFoundException {
        return this.timeSlotRepo.getTimeSlotByEndTime(endTime);
    }

    @Override
    public TimeSlot updateTimeSlot(TimeSlot timeSlot) throws TimeSlotNotFoundException {
        return this.timeSlotRepo.updateTimeSlot(timeSlot);
    }

    @Override
    public void deleteTimeSlotById(Long id) throws TimeSlotNotFoundException {
        this.timeSlotRepo.deleteTimeSlotById(id);
    }

    @Override
    public void deleteTimeSlotByName(String name) throws TimeSlotNotFoundException {
        this.timeSlotRepo.deleteTimeSlotByName(name);
    }
}