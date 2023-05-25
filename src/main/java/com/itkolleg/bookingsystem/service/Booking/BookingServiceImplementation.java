package com.itkolleg.bookingsystem.service.Booking;

import com.itkolleg.bookingsystem.domains.Booking.Booking;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.service.Booking.BookingService;

import java.sql.Date;
import java.util.List;

// @service
public class BookingServiceImplementation implements BookingService<Booking, Long> {

    @Override
    public Booking createBooking(Employee employee, Booking bookable) {
        return null;
    }

    @Override
    public Booking createBooking(Long employeeId, Booking bookable) {
        return null;
    }

    @Override
    public List<Booking> getAllBookings() {
        return null;
    }

    @Override
    public List<Booking> getAllBookingsByEmployee(Employee employee) {
        return null;
    }

    @Override
    public List<Booking> getAllBookingsByEmployeeAndDate(Employee employee, Date searchDate) {
        return null;
    }

    @Override
    public List<Booking> getAllBookingsByDate(Date searchDate) {
        return null;
    }

    @Override
    public List<Booking> getAllBookingsByEmployeeID(Long employeeID, Date searchDate) {
        return null;
    }

    @Override
    public List<Booking> getAllBookingsById(Long id) {
        return null;
    }

    @Override
    public List<Booking> getAllBookingsByBookable(Booking bookable) {
        return null;
    }

    @Override
    public Booking updateBookingByEmployeeId(Long employeeID) {
        return null;
    }

    @Override
    public Booking updateBookingById(Long bookableID) {
        return null;
    }

    @Override
    public Booking updateBookingByEmployee(Long employee) {
        return null;
    }

    @Override
    public Booking updateBookingStartAndEnd(Date Start, Date End) {
        return null;
    }
}
