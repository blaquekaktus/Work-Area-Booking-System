package com.itkolleg.bookingsystem.repos.DeskBooking;

import com.itkolleg.bookingsystem.domains.Booking.DeskBooking;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskNotAvailableException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


public interface DeskBookingRepo {
    DeskBooking addBooking(DeskBooking booking) throws DeskNotAvailableException, ResourceNotFoundException;

    List<DeskBooking> getAllBookings();

    Optional<DeskBooking> getBookingByBookingId(Long bookingId) throws ResourceNotFoundException;


    List<DeskBooking> getBookingsByEmployeeId(Long employeeId);

    List<DeskBooking> getBookingByDesk(Desk desk);

    List<DeskBooking> getBookingsByEmployee(Employee employee);

    List<DeskBooking> getBookingsByEmployeeAndDate(Employee employee, LocalDate date);

    List<DeskBooking> searchBookings(Long employeeId, Long deskId, LocalDate date);

    List<DeskBooking> getByDeskAndDate(Desk desk, LocalDate date);

    List<DeskBooking> getBookingByDate(LocalDate date);

    DeskBooking updateBookingByBookingId(Long bookingId, DeskBooking updatedBooking) throws ResourceNotFoundException;

    DeskBooking updateBooking(DeskBooking updatedBooking) throws ResourceNotFoundException;

    void deleteBookingById(Long employeeId) throws ResourceDeletionFailureException;

    List<Desk> getAvailableDesks(LocalDate date, LocalTime start, LocalTime end);

    boolean isDeskAvailable(Desk desk, LocalDate date, LocalTime start, LocalTime end);

    List<DeskBooking> getBookingsByDeskAndDateAndBookingTimeBetween(Desk desk, LocalDate date, LocalTime startDateTime, LocalTime endDateTime);

    List<DeskBooking> getBookingByDateAndByStartBetween(LocalDate date, LocalTime startOfDay, LocalTime endOfDay);

    DeskBooking save(DeskBooking booking);
}