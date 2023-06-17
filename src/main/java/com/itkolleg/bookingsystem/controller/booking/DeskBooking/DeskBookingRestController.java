package com.itkolleg.bookingsystem.controller.booking.DeskBooking;

import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.service.DeskBooking.DeskBookingService;
import com.itkolleg.bookingsystem.domains.Booking.DeskBooking;
import com.itkolleg.bookingsystem.exceptions.DeskNotAvailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing desk bookings.
 */
@RestController
@RequestMapping("/api/v1/deskbooking")
public class DeskBookingRestController {

    private static final Logger logger = LoggerFactory.getLogger(DeskBookingRestController.class);

    private final DeskBookingService deskBookingService;

    public DeskBookingRestController(DeskBookingService deskBookingService) {
        this.deskBookingService = deskBookingService;
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
