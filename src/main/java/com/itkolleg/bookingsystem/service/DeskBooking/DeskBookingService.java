package com.itkolleg.bookingsystem.service.DeskBooking;

import com.itkolleg.bookingsystem.domains.Booking.DeskBooking;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.exceptions.DeskNotAvailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface DeskBookingService {

    Logger logger = LoggerFactory.getLogger(DeskBookingService.class);

    DeskBooking addDeskBooking(DeskBooking deskBooking) throws DeskNotAvailableException, ResourceNotFoundException;

    List<DeskBooking> getAllBookings();

    List<DeskBooking> searchBookings(Employee employee, LocalDate date);

    List<DeskBooking> getBookingsByEmployeeId(Long employeeId);

    List<DeskBooking> getBookingByDesk(Desk desk);

    List<DeskBooking> getBookingsByEmployee(Employee employee);

    List<DeskBooking> getBookingsByDate(LocalDate date);

    DeskBooking getBookingById(Long bookingId) throws ResourceNotFoundException;

    DeskBooking updateBookingById(Long bookingId, DeskBooking updatedBooking) throws ResourceNotFoundException, DeskNotAvailableException;

    DeskBooking updateBooking(DeskBooking booking) throws ResourceNotFoundException, DeskNotAvailableException;

    void deleteBookingById(Long bookingID) throws ResourceNotFoundException, ResourceDeletionFailureException;

    boolean isDeskAvailable(Desk desk, LocalDate date, LocalTime startDateTime, LocalTime endDateTime);

    void deleteBooking(Long id) throws ResourceNotFoundException, ResourceDeletionFailureException;

    List<DeskBooking> getMyBookingHistory(Long employeeId);

    DeskBooking save(DeskBooking booking);
}