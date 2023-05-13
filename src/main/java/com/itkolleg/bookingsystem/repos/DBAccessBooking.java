package com.itkolleg.bookingsystem.repos;

import com.itkolleg.bookingsystem.domains.Booking.Booking;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;

import java.util.List;

public interface DBAccessBooking <T,I> {
    List<Booking>getByEmployee(Employee employee);
    List<Booking> getByBookable(T bookable);

    List<Booking>getDeskBookingsByEmployeeID(Long employeeID);

    List<Booking>getBookingsByID(Long id);

    Booking updateDeskBookingByEmployeeId(Long employeeID);

    Booking updateBookingById(Long deskID);

    void deleteBookingByEmployeeId(Long employeeID);

    void deleteBookingById(Long id);
}
