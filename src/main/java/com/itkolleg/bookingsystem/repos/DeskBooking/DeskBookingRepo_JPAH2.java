package com.itkolleg.bookingsystem.repos.DeskBooking;

import com.itkolleg.bookingsystem.domains.Booking.DeskBooking;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.exceptions.DeskNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import com.itkolleg.bookingsystem.repos.Desk.DeskJPARepo;
import com.itkolleg.bookingsystem.repos.Employee.EmployeeJPARepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * The JPA implementation of the DeskBookingRepo interface.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-26
 */
@Component
@ComponentScan({"com.itkolleg.repos"})
public class DeskBookingRepo_JPAH2 implements DeskBookingRepo {
    private static final Logger logger = LoggerFactory.getLogger(DeskBookingRepo_JPAH2.class);

    private final DeskBookingJPARepo deskBookingJPARepo;
    private final DeskJPARepo deskJPARepo;
    private final EmployeeJPARepo employeeJPARepo;

    public DeskBookingRepo_JPAH2(DeskBookingJPARepo deskBookingJPARepo, DeskJPARepo deskJPARepo, EmployeeJPARepo employeeJPARepo) {
        this.deskBookingJPARepo = deskBookingJPARepo;
        this.deskJPARepo = deskJPARepo;
        this.employeeJPARepo = employeeJPARepo;
    }

    /**
     * Adds a desk booking to the repository.
     *
     * @param deskBooking The desk booking to add.
     * @return The added desk booking.
     * @throws DeskNotAvailableException   If the desk is not available for the booking period.
     * @throws ResourceNotFoundException  If the desk or employee is not found.
     */
    public DeskBooking addBooking(DeskBooking deskBooking) throws DeskNotAvailableException, ResourceNotFoundException {
        // Check for null values
        if (deskBooking == null || deskBooking.getEmployee() == null || deskBooking.getDesk() == null) {
            throw new IllegalArgumentException("The DeskBooking or Employee or Desk cannot be null.");
        }

        // Load the associated Desk entity from the database
        Long deskid = deskBooking.getDesk().getId();
        Desk desk = deskJPARepo.findDeskById(deskid);
        if (desk == null) {
            throw new ResourceNotFoundException("Desk was not found");
        }

        // Check if the desk is available for the booking period
        if (!isDeskAvailable(desk, deskBooking.getDate(), deskBooking.getStart(), deskBooking.getEndTime())) {
            throw new DeskNotAvailableException("Desk not available for booking period");
        }

        // Create a new Booking entity
        DeskBooking booking = new DeskBooking();
        booking.setEmployee(deskBooking.getEmployee());
        booking.setDesk(desk);
        booking.setDate(deskBooking.getDate());
        booking.setStart(deskBooking.getStart());
        booking.setEndTime(deskBooking.getEndTime());

        // Save the booking
        try {
            return this.deskBookingJPARepo.save(booking);
        } catch (Exception e) {
            throw new RuntimeException("Error saving the booking to the database", e);
        }
    }

    /**
     * Retrieves all desk bookings from the repository.
     *
     * @return The list of all desk bookings.
     */
    public List<DeskBooking> getAllBookings() {
        return this.deskBookingJPARepo.findAll();
    }

    /**
     * Retrieves a desk booking by its booking ID.
     *
     * @param bookingId The ID of the desk booking.
     * @return The desk booking with the specified ID.
     * @throws ResourceNotFoundException If the booking is not found.
     */
    public Optional<DeskBooking> getBookingByBookingId(Long bookingId) throws ResourceNotFoundException {
        Optional<DeskBooking> bookingOptional = deskBookingJPARepo.findById(bookingId);
        if (bookingOptional.isEmpty()) {
            String message = "The Booking with the ID: " + bookingId + " was not found!";
            logger.error(message);
            throw new ResourceNotFoundException(message);
        }
        return bookingOptional;
    }

    /**
     * Retrieves all desk bookings for a specific desk.
     *
     * @param desk The desk to retrieve bookings for.
     * @return The list of desk bookings for the specified desk.
     */
    public List<DeskBooking> getBookingByDesk(Desk desk) {
        return this.deskBookingJPARepo.getBookingByDesk(Optional.ofNullable(desk));
    }

    /**
     * Retrieves all desk bookings for a specific employee.
     *
     * @param employee The employee to retrieve bookings for.
     * @return The list of desk bookings for the specified employee.
     */
    public List<DeskBooking> getBookingsByEmployee(Employee employee) {
        return this.deskBookingJPARepo.getBookingsByEmployee(employee);
    }

    /**
     * Retrieves all desk bookings for a specific employee and date.
     *
     * @param employee The employee to retrieve bookings for.
     * @param date     The date to retrieve bookings for.
     * @return The list of desk bookings for the specified employee and date.
     */
    public List<DeskBooking> getBookingsByEmployeeAndDate(Employee employee, LocalDate date) {
        return this.deskBookingJPARepo.getBookingsByEmployeeAndDate(Optional.ofNullable(employee), date);
    }

    /**
     * Searches for desk bookings based on the given parameters.
     *
     * @param employeeId The ID of the employee.
     * @param deskId     The ID of the desk.
     * @param date       The date.
     * @return The list of desk bookings that match the search criteria.
     */
    public List<DeskBooking> searchBookings(Long employeeId, Long deskId, LocalDate date) {
        List<DeskBooking> bookings = new ArrayList<>();
        if (employeeId == null && deskId == null && date == null) {
            // Handle scenario where all parameters are null
            bookings = deskBookingJPARepo.findAll();
        }
        if (employeeId != null && deskId != null && date != null) {
            // Search for bookings by employee ID, desk ID, and date
            Optional<Employee> employee = employeeJPARepo.findById(employeeId);
            Desk desk;
            bookings = deskBookingJPARepo.getBookingsByEmployeeIdAndDateAndDeskId(employeeId, date, deskId);
        } else if (employeeId != null && date != null) {
            // Search for bookings by employee ID and date
            Optional<Employee> employee = employeeJPARepo.findById(employeeId);
            bookings = deskBookingJPARepo.getBookingsByEmployeeAndDate(employee, date);
        } else if (deskId != null && date != null) {
            // Search for bookings by desk ID and date
            Optional<Desk> desk = deskJPARepo.findById(deskId);
            bookings = deskBookingJPARepo.getBookingsByDeskAndDate(desk, date);
        } else if (employeeId != null) {
            // Search for bookings by employee ID
            Optional<Employee> employee = employeeJPARepo.findById(employeeId);
            bookings = deskBookingJPARepo.getBookingsByEmployeeId(employeeId);
        } else if (deskId != null) {
            // Search for bookings by desk ID
            Optional<Desk> desk = deskJPARepo.findById(deskId);
            bookings = deskBookingJPARepo.getBookingByDesk(desk);
        } else if (date != null) {
            // Search for bookings by date
            bookings = deskBookingJPARepo.getBookingsByDate(date);
        }
        return bookings;
    }

    /**
     * Retrieves desk bookings for a specific desk and date.
     *
     * @param desk The desk to retrieve bookings for.
     * @param date The date to retrieve bookings for.
     * @return The list of desk bookings for the specified desk and date.
     */
    public List<DeskBooking> getByDeskAndDate(Desk desk, LocalDate date) {
        return this.deskBookingJPARepo.getBookingsByDeskAndDate(Optional.ofNullable(desk), date);
    }

    /**
     * Retrieves desk bookings for a specific date.
     *
     * @param date The date to retrieve bookings for.
     * @return The list of desk bookings for the specified date.
     */
    public List<DeskBooking> getBookingByDate(LocalDate date) {
        return this.deskBookingJPARepo.getBookingsByDate(date);
    }

    /**
     * Retrieves desk bookings for a specific employee ID.
     *
     * @param employeeId The ID of the employee.
     * @return The list of desk bookings for the specified employee ID.
     */
    public List<DeskBooking> getBookingsByEmployeeId(Long employeeId) {
        return this.deskBookingJPARepo.getBookingsByEmployeeId(employeeId);
    }

    /**
     * Retrieves desk bookings for a specific date and booking time range.
     *
     * @param date        The date to retrieve bookings for.
     * @param bookingStart The start time of the booking time range.
     * @param bookingEnd   The end time of the booking time range.
     * @return The list of desk bookings for the specified date and booking time range.
     */
    public List<DeskBooking> getBookingsByDateAndBookingStartBetween(LocalDate date, LocalTime bookingStart, LocalTime bookingEnd) {
        return this.deskBookingJPARepo.getBookingsByDateAndStartBetween(date, bookingStart, bookingEnd);
    }

    /**
     * Updates a desk booking by its ID.
     *
     * @param deskBookingId   The ID of the desk booking.
     * @param updatedDeskBooking The updated desk booking.
     * @return The updated desk booking.
     * @throws ResourceNotFoundException If the booking or associated entities are not found.
     */
    public DeskBooking updateBookingById(Long deskBookingId, DeskBooking updatedDeskBooking) throws ResourceNotFoundException {
        // Checking for mandatory fields on the updated booking
        if (updatedDeskBooking.getDesk() == null || updatedDeskBooking.getEmployee() == null || updatedDeskBooking.getDate() == null
                || updatedDeskBooking.getStart() == null || updatedDeskBooking.getEndTime() == null || updatedDeskBooking.getCreatedOn() == null) {
            throw new IllegalArgumentException("Updated Booking must have valid Desk, Employee, Date, StartTime, EndTime and Creation Date.");
        }

        return deskBookingJPARepo.findById(deskBookingId).map(existingBooking -> {
            Desk fetchedDesk;
            try {
                fetchedDesk = deskJPARepo.findById(updatedDeskBooking.getDesk().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("The Desk with the ID: " + updatedDeskBooking.getDesk().getId() + " was not found!"));
            } catch (ResourceNotFoundException e) {
                logger.error(e.getMessage());
                throw new RuntimeException("Failed to update booking due to missing desk. Original error: " + e.getMessage());
            }

            Employee fetchedEmployee;
            try {
                fetchedEmployee = employeeJPARepo.findById(updatedDeskBooking.getEmployee().getId())
                        .orElseThrow(() -> new EmployeeNotFoundException("The Employee with the ID: " + updatedDeskBooking.getEmployee().getId() + " was not found!"));
            } catch (EmployeeNotFoundException e) {
                logger.error(e.getMessage());
                throw new RuntimeException("Failed to update booking due to missing employee. Original error: " + e.getMessage());
            }
            existingBooking.setDesk(fetchedDesk);
            existingBooking.setEmployee(fetchedEmployee);
            existingBooking.setDate(updatedDeskBooking.getDate());
            existingBooking.setStart(updatedDeskBooking.getStart());
            existingBooking.setEndTime(updatedDeskBooking.getEndTime());
            existingBooking.setUpdatedOn(LocalDateTime.now());
            return existingBooking;
        }).orElseThrow(() -> new ResourceNotFoundException("The Desk Booking with the ID: " + deskBookingId + " was not found!"));
    }

    /**
     * Updates a desk booking.
     *
     * @param updatedBooking The updated desk booking.
     * @return The updated desk booking.
     * @throws ResourceNotFoundException If the booking is not found.
     */
    public DeskBooking updateBooking(DeskBooking updatedBooking) throws ResourceNotFoundException {
        if (updatedBooking.getId() == null) {
            throw new IllegalArgumentException("Id cannot be null when updating");
        }
        return deskBookingJPARepo.saveAndFlush(updatedBooking);
    }

    /**
     * Deletes a desk booking by its ID.
     *
     * @param bookingId The ID of the desk booking.
     * @throws ResourceDeletionFailureException If the booking is not found.
     */
    public void deleteBookingById(Long bookingId) throws ResourceDeletionFailureException {
        Optional<DeskBooking> bookingOptional = this.deskBookingJPARepo.findById(bookingId);
        if (bookingOptional.isPresent()) {
            this.deskBookingJPARepo.deleteById(bookingId);
        } else {
            throw new ResourceDeletionFailureException("The Desk Booking with the ID: " + bookingId + " was not found!");
        }
    }

    /**
     * Retrieves available desks for a specific date and time range.
     *
     * @param date The date to check for availability.
     * @param start The start time of the time range.
     * @param end The end time of the time range.
     * @return The list of available desks.
     */
    public List<Desk> getAvailableDesks(LocalDate date, LocalTime start, LocalTime end) {
        List<Desk> allDesks = deskJPARepo.findAll();
        List<Desk> availableDesks = new ArrayList<>();

        for (Desk desk : allDesks) {
            if (isDeskAvailable(desk, date, start, end)) {
                availableDesks.add(desk);
            }
        }
        return availableDesks;
    }

    /**
     * Checks if a desk is available for a specific date and time range.
     *
     * @param desk The desk to check availability for.
     * @param date The date to check for availability.
     * @param start The start time of the time range.
     * @param end The end time of the time range.
     * @return True if the desk is available, false otherwise.
     */
    public boolean isDeskAvailable(Desk desk, LocalDate date, LocalTime start, LocalTime end) {
        List<DeskBooking> overlappingBookings = deskBookingJPARepo.getBookingsByDeskAndDateAndStartBetween(desk, date, start, end);
        return overlappingBookings.isEmpty();
    }

    /**
     * Retrieves desk bookings for a specific date and booking time range.
     *
     * @param date The date to retrieve bookings for.
     * @param startOfDay The start time of the booking time range.
     * @param endOfDay The end time of the booking time range.
     * @return The list of desk bookings for the specified date and booking time range.
     */
    public List<DeskBooking> getBookingByDateAndByStartBetween(LocalDate date, LocalTime startOfDay, LocalTime endOfDay) {
        return deskBookingJPARepo.getBookingsByDateAndStartBetween(date, startOfDay, endOfDay);
    }

    /**
     * Saves a desk booking to the repository.
     *
     * @param booking The desk booking to save.
     * @return The saved desk booking.
     */
    public DeskBooking save(DeskBooking booking) {
        return this.deskBookingJPARepo.save(booking);
    }

    /**
     * Retrieves desk bookings for a specific desk, date, and booking time range.
     *
     * @param desk The desk to retrieve bookings for.
     * @param date The date to retrieve bookings for.
     * @param start The start time of the booking time range.
     * @param endTime The end time of the booking time range.
     * @return The list of desk bookings for the specified desk, date, and booking time range.
     */
    public List<DeskBooking> getBookingsByDeskAndDateAndBookingTimeBetween(Desk desk, LocalDate date, LocalTime start, LocalTime endTime) {
        return deskBookingJPARepo.getBookingsByDeskAndDateAndStartBetween(desk, date, start, endTime);
    }
}
