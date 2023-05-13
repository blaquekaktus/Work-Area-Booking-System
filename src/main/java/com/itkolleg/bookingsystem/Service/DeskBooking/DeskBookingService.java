package com.itkolleg.bookingsystem.Service.DeskBooking;

import com.itkolleg.bookingsystem.domains.Booking.DeskBooking;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.BookingExceptions.BookingNotFoundException;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskNotAvailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface DeskBookingService {

    Logger logger = LoggerFactory.getLogger(DeskBookingService.class);
    DeskBooking addDeskBooking(DeskBooking deskBooking) throws DeskNotAvailableException;
    List<DeskBooking>getAllBookings();
    List<DeskBooking> searchBookings(Employee employee, LocalDate date);
    List<DeskBooking> getBookingsByEmployeeId(Long employeeId);
    List<DeskBooking> getBookingByDesk(Desk desk);
    List<DeskBooking> getBookingsByEmployee(Employee employee);
    List<DeskBooking> getBookingsByDate(LocalDate date);
    DeskBooking getBookingById(Long bookingId) throws BookingNotFoundException;
    DeskBooking updateBookingById(Long bookingId, DeskBooking updatedBooking) throws BookingNotFoundException, DeskNotAvailableException;
    DeskBooking updateBooking(DeskBooking booking) throws BookingNotFoundException, DeskNotAvailableException;
    List<DeskBooking> findByDeskAndBookingEndAfterAndBookingStartBefore(Desk desk, LocalDateTime startDateTime, LocalDateTime endDateTime);
    void deleteBookingById(Long bookingID) throws BookingNotFoundException;
    List<Desk> getAvailableDesks(LocalDateTime startDateTime, LocalDateTime endDateTime);
    boolean isDeskAvailable(Desk desk, LocalDateTime startDateTime, LocalDateTime endDateTime);

    abstract List<Desk> getAvailableDesks(LocalDate date, LocalTime bookingStart, LocalTime bookingEnd);

    boolean isDeskAvailable(Desk desk, LocalDate date, LocalTime startDateTime, LocalTime endDateTime);

    void deleteBooking(Long id) throws BookingNotFoundException;
    List<DeskBooking>getMyBookingHistory(Long employeeId);
}