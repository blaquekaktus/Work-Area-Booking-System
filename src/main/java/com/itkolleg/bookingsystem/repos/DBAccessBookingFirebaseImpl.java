package com.itkolleg.bookingsystem.repos;

import com.itkolleg.bookingsystem.domains.Booking.Booking;
import com.itkolleg.bookingsystem.domains.Employee;

import java.util.List;

public class DBAccessBookingFirebaseImpl implements DBAccessBooking {
    @Override
    public List<Booking> getByEmployee(Employee employee) {
        return null;
    }

    @Override
    public List<Booking> getByBookable(Object bookable) {
        return null;
    }

    @Override
    public List<Booking> getDeskBookingsByEmployeeID(Long employeeID) {
        return null;
    }

    @Override
    public List<Booking> getBookingsByID(Long id) {
        return null;
    }

    @Override
    public Booking updateDeskBookingByEmployeeId(Long employeeID) {
        return null;
    }

    @Override
    public Booking updateBookingById(Long deskID) {
        return null;
    }

    @Override
    public void deleteBookingByEmployeeId(Long employeeID) {

    }

    @Override
    public void deleteBookingById(Long id) {

    }
}
