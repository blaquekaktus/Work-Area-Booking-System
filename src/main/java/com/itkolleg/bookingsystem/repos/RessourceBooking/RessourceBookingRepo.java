package com.itkolleg.bookingsystem.repos.RessourceBooking;

import com.itkolleg.bookingsystem.domains.Booking.RessourceBooking;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


public interface RessourceBookingRepo {
    RessourceBooking addBooking(RessourceBooking booking) throws ResourceNotFoundException;

    List<RessourceBooking> getAllBookings();

    Optional<RessourceBooking> getBookingByRessourceId(Long RessourceId) throws ResourceNotFoundException;

    List<RessourceBooking> getBookingsByEmployeeId(Long employeeId);

    List<RessourceBooking> getBookingByRessource(Ressource ressource);

    List<RessourceBooking> getBookingsByEmployee(Employee employee);

    List<RessourceBooking> getBookingsByEmployeeAndDate(Employee employee, LocalDate date);

    List<RessourceBooking> searchBookings(Long employeeId, Long RessourceId, LocalDate date);

    List<RessourceBooking> getByRessourceAndDate(Ressource ressource, LocalDate date);

    List<RessourceBooking> getBookingByDate(LocalDate date);

    RessourceBooking updateBookingById(Long bookingId, RessourceBooking updatedBooking) throws ResourceNotFoundException;

    RessourceBooking updateBooking(RessourceBooking updatedBooking) throws ResourceNotFoundException;

    void deleteBookingById(Long bookingId) throws ResourceDeletionFailureException;

    List<Ressource> getAvailableRessources(LocalDate date, LocalTime start, LocalTime end);

    boolean isRessourceAvailable(Ressource ressource, LocalDate date, LocalTime start, LocalTime end);

    List<RessourceBooking> getBookingsByRessourceAndDateAndBookingTimeBetween(Ressource ressource, LocalDate date, LocalTime startDateTime, LocalTime endDateTime);

    List<RessourceBooking> getBookingByDateAndByStartBetween(LocalDate date, LocalTime startOfDay, LocalTime endOfDay);

    RessourceBooking save(RessourceBooking booking);
}