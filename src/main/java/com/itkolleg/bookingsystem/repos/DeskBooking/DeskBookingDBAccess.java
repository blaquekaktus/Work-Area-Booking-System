package com.itkolleg.bookingsystem.repos.DeskBooking;

import com.itkolleg.bookingsystem.domains.Booking.DeskBooking;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.BookingExceptions.BookingNotFoundException;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskNotFoundException;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


public interface DeskBookingDBAccess {
    DeskBooking addBooking(DeskBooking booking) throws DeskNotAvailableException, DeskNotFoundException;

    List<DeskBooking> getAllBookings();

    Optional<DeskBooking> getBookingByBookingId(Long bookingId) throws BookingNotFoundException;


    List<DeskBooking> getBookingsByEmployeeId(Long employeeId);

    List<DeskBooking> getBookingByDesk(Desk desk);

    List<DeskBooking> getBookingsByEmployee(Employee employee);

    List<DeskBooking> getBookingsByEmployeeAndDate(Employee employee, LocalDate date);

    List<DeskBooking> searchBookings(Long employeeId, Long deskId, LocalDate date);

    List<DeskBooking> getByDeskAndDate(Desk desk, LocalDate date);
    List<DeskBooking> getBookingByDate(LocalDate date);

    DeskBooking updateBookingByBookingId(Long bookingId, DeskBooking updatedBooking) throws BookingNotFoundException;
    DeskBooking updateBooking(DeskBooking updatedBooking);

    void deleteBookingById(Long employeeId) throws BookingNotFoundException;

    List<Desk> getAvailableDesks(LocalDate date, LocalTime start, LocalTime end);

    boolean isDeskAvailable(Desk desk, LocalDate date, LocalTime start, LocalTime end);

    List<DeskBooking> getBookingsByDeskAndDateAndBookingTimeBetween(Desk desk, LocalDate date, LocalTime startDateTime, LocalTime endDateTime);

    List<DeskBooking> getBookingByDateAndByStartBetween(LocalDate date, LocalTime startOfDay, LocalTime endOfDay);
}