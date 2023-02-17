package com.itkolleg.bookingsystem.Service;

import com.itkolleg.bookingsystem.domains.Booking;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;

import java.sql.Date;
import java.util.List;

public interface BookingService {
    Booking createBooking(Employee employee, Desk desk);
    Booking createBooking(Long employeeID, Long deskID);
    List<Booking> getAllBookings();
    List<Booking> getAllBookingsByEmployee(Employee employee);
    List<Booking> getAllBookingsByEmployeeAndDate(Employee employee, Date searchDate);
    List<Booking> getAllBookingsByDate(Date searchDate);
    List<Booking> getAllBookingsByEmployeeID(Long employeeID, Date searchDate);
    List<Booking> getAllBookingsByDeskId(Long deskID);
    List<Booking> getAllBookingsByDeskNr(String DeskNr);
    Booking updateBookingByEmployeeId(Long employeeID);
    Booking updateBookingByDeskId(Long DeskID);
    Booking updateBookingByEmployee(Long employee);
    Booking updateBookingStartAndEnd(Date Start, Date End);
}
