package com.itkolleg.bookingsystem.controller.booking.DeskBooking;

import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.service.DeskBooking.DeskBookingService;
import com.itkolleg.bookingsystem.domains.Booking.DeskBooking;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskNotAvailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/deskbooking")
public class DeskBookingRestController {
    private static final Logger logger = LoggerFactory.getLogger(DeskBookingRestController.class);
    private final DeskBookingService deskBookingService;

    public DeskBookingRestController(DeskBookingService deskBookingService) {
        this.deskBookingService = deskBookingService;
    }

    @PostMapping("/")
    public ResponseEntity<DeskBooking> addBooking(@RequestBody DeskBooking deskBooking) throws DeskNotAvailableException, ResourceNotFoundException {
        return ResponseEntity.ok(this.deskBookingService.addDeskBooking(deskBooking));
    }

    @GetMapping("/")
    public ResponseEntity<List<DeskBooking>> getAllBookings() {
        return ResponseEntity.ok(this.deskBookingService.getAllBookings());
    }

    @GetMapping("/{id}/")
    public ResponseEntity<DeskBooking> getBookingById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(this.deskBookingService.getBookingById(id));
    }

    @PutMapping("/{id}/")
    public ResponseEntity<DeskBooking> updateBookingById(@PathVariable Long id, @RequestBody DeskBooking deskBooking) throws ResourceNotFoundException, DeskNotAvailableException {
        try {
            return ResponseEntity.ok(this.deskBookingService.updateBookingById(id, deskBooking));
        } catch (ResourceNotFoundException | DeskNotAvailableException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> deleteBookingById(@PathVariable Long id) throws ResourceNotFoundException, ResourceDeletionFailureException {
        this.deskBookingService.deleteBookingById(id);
        return ResponseEntity.noContent().build();
    }
}
