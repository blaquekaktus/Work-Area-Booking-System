package com.itkolleg.bookingsystem.controller.booking.DeskBooking;

import com.itkolleg.bookingsystem.domains.Booking.BookingResponse;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.service.Desk.DeskService;
import com.itkolleg.bookingsystem.service.DeskBooking.DeskBookingService;
import com.itkolleg.bookingsystem.domains.Booking.DeskBooking;
import com.itkolleg.bookingsystem.exceptions.DeskNotAvailableException;
import com.itkolleg.bookingsystem.service.Employee.EmployeeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * REST controller for managing desk bookings.
 */
@RestController
@RequestMapping("/api/deskbooking")
public class DeskBookingRestController {

    private static final Logger logger = LoggerFactory.getLogger(DeskBookingRestController.class);

    private final DeskBookingService deskBookingService;
    private final DeskService deskService;
    private final EmployeeService employeeService;


    public DeskBookingRestController(DeskBookingService deskBookingService, DeskService deskService, EmployeeService employeeService) {
        this.deskBookingService = deskBookingService;
        this.deskService = deskService;
        this.employeeService = employeeService;
    }

    /**
     * Adds a new desk booking.
     *
     * @param deskBooking The desk booking to add.
     * @return The added desk booking.
     * @throws DeskNotAvailableException If the desk is not available.
     * @throws ResourceNotFoundException If the desk does not exist.
     */
    @PostMapping
    public ResponseEntity<DeskBooking> addBooking(@RequestBody DeskBooking deskBooking) throws DeskNotAvailableException, ResourceNotFoundException {
        return ResponseEntity.ok(this.deskBookingService.addDeskBooking(deskBooking));
    }

    /**
     * Retrieves all desk bookings.
     *
     * @return A list of all desk bookings.
     */
    @GetMapping
    public ResponseEntity<List<DeskBooking>> getAllBookings() {
        return ResponseEntity.ok(this.deskBookingService.getAllBookings());
    }

    public ResponseEntity<?> addDeskBooking(@Valid @RequestBody DeskBooking booking) {
        try {
            Desk desk = deskService.getDeskById(booking.getDesk().getId());
            Employee employee = employeeService.getEmployeeById(booking.getEmployee().getId());

            booking.setDesk(desk);
            booking.setEmployee(employee);
            booking.setCreatedOn(LocalDateTime.now());

            this.deskBookingService.addDeskBooking(booking);
            return ResponseEntity.ok().body(new BookingResponse(true, null));
        } catch (DeskNotAvailableException e) {
            logger.error("Desk is not available for booking: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BookingResponse(false, "Desk is not available for booking."));
        } catch (ExecutionException | InterruptedException | EmployeeNotFoundException | ResourceNotFoundException e) {
            logger.error("Error occurred while booking the desk: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BookingResponse(false, "Error occurred while booking the desk."));
        } catch (IllegalArgumentException e) {
            logger.error("Cannot create booking for a past date: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BookingResponse(false, "Cannot create booking for a past date."));
        }
    }
    /**
     * Retrieves a desk booking by its ID.
     *
     * @param id The ID of the desk booking.
     * @return The desk booking if it exists.
     * @throws ResourceNotFoundException If the desk booking does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DeskBooking> getBookingById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(this.deskBookingService.getBookingById(id));
    }

    /**
     * Updates a desk booking by its ID.
     *
     * @param id The ID of the desk booking.
     * @param deskBooking The updated desk booking.
     * @return The updated desk booking if it exists.
     * @throws ResourceNotFoundException If the desk booking does not exist.
     * @throws DeskNotAvailableException If the desk is not available.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DeskBooking> updateBookingById(@PathVariable Long id, @RequestBody DeskBooking deskBooking) throws ResourceNotFoundException, DeskNotAvailableException {
        return ResponseEntity.ok(this.deskBookingService.updateBookingById(id, deskBooking));
    }

    /**
     * Deletes a desk booking by its ID.
     *
     * @param id The ID of the desk booking.
     * @throws ResourceNotFoundException If the desk booking does not exist.
     * @throws ResourceDeletionFailureException If the deletion fails.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingById(@PathVariable Long id) throws ResourceNotFoundException, ResourceDeletionFailureException {
        this.deskBookingService.deleteBookingById(id);
        return ResponseEntity.noContent().build();
    }
}
