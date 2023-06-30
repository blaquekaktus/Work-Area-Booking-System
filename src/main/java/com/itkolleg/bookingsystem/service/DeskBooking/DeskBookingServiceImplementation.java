package com.itkolleg.bookingsystem.service.DeskBooking;

import com.itkolleg.bookingsystem.domains.Booking.DeskBooking;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Role;
import com.itkolleg.bookingsystem.exceptions.DeskNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.repos.DeskBooking.DeskBookingRepo;
import com.itkolleg.bookingsystem.repos.Holiday.HolidayRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for handling desk booking operations.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-26
 */
@Service
public class DeskBookingServiceImplementation implements DeskBookingService {

    static final Logger logger = LoggerFactory.getLogger(DeskBookingServiceImplementation.class);

    private final DeskBookingRepo deskBookingRepo;
    private final HolidayRepo holidayRepo;

    public DeskBookingServiceImplementation(DeskBookingRepo deskBookingRepo, HolidayRepo holidayRepo) {
        this.deskBookingRepo = deskBookingRepo;
        this.holidayRepo = holidayRepo;
    }

    /**
     * Adds a new desk booking.
     *
     * @param booking The desk booking to add.
     * @return The added desk booking.
     * @throws DeskNotAvailableException    If the desk is not available for the booking period.
     * @throws ResourceNotFoundException   If a required resource is not found.
     * @throws IllegalArgumentException    If the booking date is in the past, on a weekend, or on a non-booking allowed day.
     */
    @Override
    public DeskBooking addDeskBooking(DeskBooking booking) throws DeskNotAvailableException, ResourceNotFoundException {
        // Check if desk is available for the date and time chosen
        List<DeskBooking> bookings = deskBookingRepo.getBookingsByDeskAndDateAndBookingTimeBetween(booking.getDesk(), booking.getDate(), booking.getStart(), booking.getEndTime());
        LocalDate currentDate = LocalDate.now();

        // Logging dates
        logger.info("Booking Date: " + booking.getDate());
        logger.info("Current Date: " + currentDate);

        if (!bookings.isEmpty()) {
            throw new DeskNotAvailableException("Desk not available for booking period");
        }

        // Check if booking is for a past date
        if (booking.getDate().isBefore(currentDate)) {
            throw new IllegalArgumentException("Cannot create booking for a past date");
        }

        // Check if booking is for a weekday
        DayOfWeek dayOfWeek = booking.getDate().getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Cannot create booking for a weekend");
        }

        // Check if booking is allowed on this day
        if (!holidayRepo.isBookingAllowedOnHoliday(booking.getDate())) {
            throw new IllegalArgumentException("Cannot create booking on this day");
        }

        // Determine maximum advance booking date based on user role
        Role role = booking.getEmployee().getRole();

        // Log the role
        logger.info("Employee Role: " + role);

        LocalDate maxAdvanceBookingDate;
        if (role.name().equals(Role.ROLE_N_EMPLOYEE.name())) {
            maxAdvanceBookingDate = currentDate.plusWeeks(1);
        } else {
            maxAdvanceBookingDate = currentDate.plusWeeks(12);
        }

        // Log the max advance booking date
        logger.info("Max Advance Booking Date: " + maxAdvanceBookingDate);

        // Check if booking is too far in advance
        if (booking.getDate().isAfter(maxAdvanceBookingDate)) {
            logger.info("Is booking date after max advance booking date? " + booking.getDate().isAfter(maxAdvanceBookingDate));
            throw new IllegalArgumentException("Cannot book more than " +
                    (role.name().equals(Role.ROLE_N_EMPLOYEE.name()) ? "1 week" : "12 weeks") + " in advance");
        }

        return this.deskBookingRepo.addBooking(booking);
    }

    /**
     * Retrieves all desk bookings.
     *
     * @return The list of all desk bookings.
     */
    @Override
    public List<DeskBooking> getAllBookings() {
        return deskBookingRepo.getAllBookings();
    }

    /**
     * Searches for desk bookings based on employee and date criteria.
     *
     * @param employee The employee to search for (can be null).
     * @param date     The date to search for (can be null).
     * @return The list of desk bookings matching the search criteria.
     */
    @Override
    public List<DeskBooking> searchBookings(Employee employee, LocalDate date) {
        List<DeskBooking> bookings = new ArrayList<>();
        if (employee != null && date != null) {
            // Search for bookings by employee and date range
            bookings = deskBookingRepo.getBookingsByEmployeeAndDate(employee, date);
        } else if (employee != null) {
            // Search for bookings by employee
            bookings = deskBookingRepo.getBookingsByEmployee(employee);
        } else if (date != null) {
            // Search for bookings by date range
            bookings = deskBookingRepo.getBookingByDate(date);
        }
        return bookings;
    }

    /**
     * Retrieves desk bookings for a specific employee.
     *
     * @param employeeId The ID of the employee.
     * @return The list of desk bookings for the employee.
     */
    @Override
    public List<DeskBooking> getBookingsByEmployeeId(Long employeeId) {
        return deskBookingRepo.getBookingsByEmployeeId(employeeId);
    }

    /**
     * Retrieves desk bookings for a specific employee.
     *
     * @param employee The employee object.
     * @return The list of desk bookings for the employee.
     */
    @Override
    public List<DeskBooking> getBookingsByEmployee(Employee employee) {
        return deskBookingRepo.getBookingsByEmployee(employee);
    }

    /**
     * Retrieves desk bookings for a specific desk.
     *
     * @param desk The desk object.
     * @return The list of desk bookings for the desk.
     */
    @Override
    public List<DeskBooking> getBookingByDesk(Desk desk) {
        return deskBookingRepo.getBookingByDesk(desk);
    }

    /**
     * Retrieves desk bookings for a specific date.
     *
     * @param date The date to search for bookings.
     * @return The list of desk bookings for the date.
     */
    @Override
    public List<DeskBooking> getBookingsByDate(LocalDate date) {
        LocalTime startOfDay = LocalTime.from(date.atStartOfDay());
        LocalTime endOfDay = startOfDay.plusHours(24).minusSeconds(1);
        return deskBookingRepo.getBookingByDateAndByStartBetween(date, startOfDay, endOfDay);
    }

    /**
     * Retrieves a desk booking by its ID.
     *
     * @param bookingId The ID of the desk booking.
     * @return The desk booking with the specified ID.
     * @throws ResourceNotFoundException If the booking with the specified ID is not found.
     */
    @Override
    public DeskBooking getBookingById(Long bookingId) throws ResourceNotFoundException {
        Optional<DeskBooking> optionalBooking = deskBookingRepo.getBookingByBookingId(bookingId);
        if (optionalBooking.isPresent()) {
            return optionalBooking.get();
        } else {
            throw new ResourceNotFoundException("Booking with ID " + bookingId + " not found.");
        }
    }

    /**
     * Updates a desk booking by its ID.
     *
     * @param bookingId      The ID of the desk booking to update.
     * @param updatedBooking The updated desk booking object.
     * @return The updated desk booking.
     * @throws ResourceNotFoundException   If the booking with the specified ID is not found.
     * @throws DeskNotAvailableException    If the desk is not available for the updated booking period.
     */
    public DeskBooking updateBookingById(Long bookingId, DeskBooking updatedBooking) throws ResourceNotFoundException, DeskNotAvailableException {
        Optional<DeskBooking> booking = deskBookingRepo.getBookingByBookingId(bookingId);
        if (booking.isEmpty()) {
            throw new ResourceNotFoundException("Booking not found for id: " + bookingId);
        }

        Desk desk = booking.get().getDesk();
        LocalDate date = booking.get().getDate();
        LocalTime start = booking.get().getStart();
        LocalTime endTime = booking.get().getEndTime();
        if (isDeskAvailable(desk, date, start, endTime)) {
            updatedBooking.setId(bookingId);
        }
        return deskBookingRepo.updateBooking(updatedBooking);
    }

    /**
     * Updates a desk booking.
     *
     * @param booking The updated desk booking.
     * @return The updated desk booking.
     * @throws DeskNotAvailableException    If the desk is not available for the updated booking period.
     * @throws ResourceNotFoundException   If the booking with the specified ID is not found.
     */
    @Override
    public DeskBooking updateBooking(DeskBooking booking) throws DeskNotAvailableException, ResourceNotFoundException {
        DeskBooking result;
        try {
            DeskBooking existingBooking = deskBookingRepo.getBookingByBookingId(booking.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found for id: " + booking.getId()));
            // Check if the desk is available for the updated booking period
            List<DeskBooking> bookings = deskBookingRepo.getBookingsByDeskAndDateAndBookingTimeBetween(booking.getDesk(), booking.getDate(), booking.getStart(), booking.getEndTime());
            bookings.remove(existingBooking);
            if (!bookings.isEmpty()) {
                throw new DeskNotAvailableException("Desk not available for booking period");
            }
            existingBooking.setEmployee(booking.getEmployee());
            existingBooking.setDesk(booking.getDesk());
            existingBooking.setDate(booking.getDate());
            existingBooking.setStart(booking.getStart());
            existingBooking.setEndTime(booking.getEndTime());
            existingBooking.setCreatedOn(LocalDateTime.now());
            result = deskBookingRepo.addBooking(existingBooking);
        } catch (DataAccessException e) {
            throw new ResourceNotFoundException("Database access error occurred for id: " + booking.getId(), e);
        }

        return result;
    }

    /**
     * Deletes a desk booking by its ID.
     *
     * @param bookingId The ID of the desk booking to delete.
     * @throws ResourceDeletionFailureException If the deletion of the booking fails.
     * @throws ResourceNotFoundException       If the booking with the specified ID is not found.
     */
    @Override
    public void deleteBookingById(Long bookingId) throws ResourceDeletionFailureException, ResourceNotFoundException {
        Optional<DeskBooking> booking = deskBookingRepo.getBookingByBookingId(bookingId);
        if (booking.isEmpty()) {
            throw new ResourceDeletionFailureException("Booking not Found!");
        }
        deskBookingRepo.deleteBookingById(bookingId);
    }

    /**
     * Checks if a desk is available for a specified date and time range.
     *
     * @param desk         The desk to check availability for.
     * @param date         The date to check availability for.
     * @param startDateTime The start time of the booking.
     * @param endDateTime   The end time of the booking.
     * @return True if the desk is available, false otherwise.
     */
    @Override
    public boolean isDeskAvailable(Desk desk, LocalDate date, LocalTime startDateTime, LocalTime endDateTime) {
        List<DeskBooking> bookings = deskBookingRepo.getBookingsByDeskAndDateAndBookingTimeBetween(desk, date, startDateTime, endDateTime);
        return bookings.isEmpty();
    }

    /**
     * Deletes a desk booking by its ID.
     *
     * @param id The ID of the desk booking to delete.
     * @throws ResourceDeletionFailureException If the deletion of the booking fails.
     */
    @Override
    public void deleteBooking(Long id) throws ResourceDeletionFailureException {
        deskBookingRepo.deleteBookingById(id);
    }

    /**
     * Retrieves the booking history for a specific employee.
     *
     * @param employeeId The ID of the employee.
     * @return The list of desk bookings for the employee.
     */
    @Override
    public List<DeskBooking> getMyBookingHistory(Long employeeId) {
        return deskBookingRepo.getBookingsByEmployeeId(employeeId);
    }

    /**
     * Saves a desk booking.
     *
     * @param booking The desk booking to save.
     * @return The saved desk booking.
     */
    @Override
    public DeskBooking save(DeskBooking booking) {
        return this.deskBookingRepo.save(booking);
    }

}
