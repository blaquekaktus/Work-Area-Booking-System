package com.itkolleg.bookingsystem.Service.TimeSlot;

import com.itkolleg.bookingsystem.domains.TimeSlot;
import com.itkolleg.bookingsystem.exceptions.TimeSlot.TimeSlotNotFoundException;
import com.itkolleg.bookingsystem.repos.Desk.DeskDBAccess;
import com.itkolleg.bookingsystem.repos.Employee.EmployeeDBAccess;
import com.itkolleg.bookingsystem.repos.TimeSlot.TimeSlotDBAccess;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class TimeSlotServiceImplementation implements TimeSlotService{
    private TimeSlotDBAccess timeSlotDBAccess;

    public TimeSlotServiceImplementation(TimeSlotDBAccess timeSlotDBAccess, DeskDBAccess deskDBAccess, EmployeeDBAccess employeeDBAccess) {
        this.timeSlotDBAccess = timeSlotDBAccess;
    }

    @Override
    public TimeSlot addTimeSlot(TimeSlot timeSlot) {
        return this.timeSlotDBAccess.addTimeSlot(timeSlot);
    }

    @Override
    public List<TimeSlot> getAllTimeSlots() {
        return this.timeSlotDBAccess.getAllTimeSlots();
    }

    @Override
    public TimeSlot getTimeSlotByName(String name) throws TimeSlotNotFoundException {
        return this.timeSlotDBAccess.getTimeSlotByName(name);
    }

    @Override
    public TimeSlot getTimeSlotByStartTime(LocalTime startTime) throws TimeSlotNotFoundException {
        return this.timeSlotDBAccess.getTimeSlotByStartTime(startTime);
    }

    @Override
    public TimeSlot getTimeSlotByEndTime(LocalTime endTime) throws TimeSlotNotFoundException {
        return this.timeSlotDBAccess.getTimeSlotByEndTime(endTime);
    }

    @Override
    public TimeSlot updateTimeSlot(TimeSlot timeSlot) throws TimeSlotNotFoundException {
        return this.timeSlotDBAccess.updateTimeSlot(timeSlot);
    }

    @Override
    public void deleteTimeSlotById(Long id) throws TimeSlotNotFoundException {
        this.timeSlotDBAccess.deleteTimeSlotById(id);
    }
    @Override
    public void deleteTimeSlotByName(String name) throws TimeSlotNotFoundException {
        this.timeSlotDBAccess.deleteTimeSlotByName(name);
    }
}