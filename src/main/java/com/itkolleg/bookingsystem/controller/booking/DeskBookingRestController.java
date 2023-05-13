package com.itkolleg.bookingsystem.controller.booking;

import com.itkolleg.bookingsystem.Service.DeskBooking.DeskBookingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/deskbooking")
public class DeskBookingRestController {
    private final DeskBookingService deskBookingService;
    public DeskBookingsRestController(DeskBookingService deskBookingService) {
        this.deskBookingService = deskBookingService;
    }

    @PostMapping("/")
    public ResponseEntity<DeskBooking> addBooking(@RequestBody DeskBooking deskBooking) throws DeskNotAvailableException {
        return ResponseEntity.ok(this.deskBookingService.addDeskBooking(deskBooking));
    }

    @GetMapping("/")
    public ResponseEntity<List<DeskBooking>> getAllBookings() {
        return ResponseEntity.ok(this.deskBookingService.getAllBookings());
    }

    @GetMapping("/{id}/")
    public ResponseEntity<DeskBooking> getBookingById(@PathVariable Long id) throws BookingNotFoundException {
        return ResponseEntity.ok(this.deskBookingService.getBookingById(id));
    }

    @PutMapping("/{id}/")
    public ResponseEntity<DeskBooking> updateBookingById(@PathVariable Long id, @RequestBody DeskBooking deskBooking) throws BookingNotFoundException, DeskNotAvailableException {
        try {
            return ResponseEntity.ok(this.deskBookingService.updateBookingById(id, deskBooking));
        } catch (BookingNotFoundException e) {
            throw new RuntimeException(e);
        } catch (DeskNotAvailableException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> deleteBookingById(@PathVariable Long id) throws BookingNotFoundException {
        this.deskBookingService.deleteBookingById(id);
        return ResponseEntity.noContent().build();
    }
}
