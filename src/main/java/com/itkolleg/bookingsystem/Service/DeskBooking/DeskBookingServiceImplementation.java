package com.itkolleg.bookingsystem.Service.DeskBooking;

import java.util.List;
import java.util.Optional;

@Service
public class DeskBookingServiceImplementation implements DeskBookingService {

    private final DeskBookingDBAccess deskBookingDBAccess;
    private DeskDBAccess deskDBAccess;
    private EmployeeDBAccess employeeDBAccess;

    public DeskBookingServiceImplementation(DeskBookingDBAccess deskBookingDBAccess, DeskDBAccess deskDBAccess, EmployeeDBAccess employeeDBAccess) {
        this.deskBookingDBAccess = deskBookingDBAccess;
        this.deskDBAccess = deskDBAccess;
        this.employeeDBAccess = employeeDBAccess;
    }


    @Override
    public DeskBooking addDeskBooking(DeskBooking booking) throws DeskNotAvailableException {
        List<DeskBooking> bookings = deskBookingDBAccess.getByDeskAndDate(booking.getDesk(), booking.getDate());
        if (!bookings.isEmpty()) {
            throw new DeskNotAvailableException("Desk not available for booking period");
        }
        return deskBookingDBAccess.addBooking(booking);
    }

    @Override
    public List<DeskBooking> getAllBookings() {
        return deskBookingDBAccess.getAllBookings();
    }

    /*@Override
    public List<DeskBooking> searchBookings(Employee employee, Desk desk, LocalDate date) {
        return null;
    }*/

    @Override
    public List<DeskBooking> searchBookings(Employee employee, LocalDate date) {
        List<DeskBooking> bookings = new ArrayList<>();
        if (employee != null  && date != null) {
            // Search for bookings by employee and date range
            bookings = deskBookingDBAccess.getBookingsByEmployeeAndDate(employee, date);
        } else if (employee != null) {
            // Search for bookings by employee
            bookings = deskBookingDBAccess.getBookingsByEmployee(employee);
        } else if (date != null) {
            // Search for bookings by date range
            bookings = deskBookingDBAccess.getBookingByDate(date);
        }
        return bookings;
    }

    @Override
    public List<DeskBooking> getBookingsByEmployeeId(Long employeeId) {
        return deskBookingDBAccess.getBookingsByEmployeeId(employeeId);
    }

    @Override
    public List<DeskBooking> getBookingsByEmployee(Employee employee) {
        return deskBookingDBAccess.getBookingsByEmployee(employee);
    }

    @Override
    public List<DeskBooking> getBookingByDesk(Desk desk) {
        return deskBookingDBAccess.getBookingByDesk(desk);
    }

    @Override
    public List<DeskBooking> getBookingsByDate(LocalDate date) {
        LocalTime startOfDay = LocalTime.from(date.atStartOfDay());
        LocalTime endOfDay = startOfDay.plusHours(24).minusSeconds(1);
        return deskBookingDBAccess.getBookingByDateAndByStartBetween(date, startOfDay, endOfDay);
    }

    @Override
    public DeskBooking getBookingById(Long bookingId) throws BookingNotFoundException {
        Optional<DeskBooking> optionalBooking = deskBookingDBAccess.getBookingByBookingId(bookingId);
        if (optionalBooking.isPresent()) {
            return optionalBooking.get();
        } else {
            throw new BookingNotFoundException("Booking with ID " + bookingId + " not found.");
        }
    }

    public DeskBooking updateBookingById(Long bookingId, DeskBooking updatedBooking) throws BookingNotFoundException, DeskNotAvailableException {
        Optional<DeskBooking> booking = deskBookingDBAccess.getBookingByBookingId(bookingId);
        if (!booking.isPresent()) {
            throw new BookingNotFoundException("Booking not found for id: " + bookingId);
        }
        updatedBooking.setId(bookingId);
        return deskBookingDBAccess.updateBooking(updatedBooking);
    }

    @Override
    public DeskBooking updateBooking(DeskBooking booking) throws BookingNotFoundException, DeskNotAvailableException {
        DeskBooking existingBooking = deskBookingDBAccess.getBookingByBookingId(booking.getId())
                .orElseThrow(() -> new BookingNotFoundException("Booking not found for id: " + booking.getId()));
        // Check if the desk is available for the updated booking period
        List<DeskBooking> bookings = deskBookingDBAccess.getByDeskAndDate(booking.getDesk(), booking.getDate());
        bookings.remove(existingBooking);
        if (!bookings.isEmpty()) {
            throw new DeskNotAvailableException("Desk not available for booking period");
        }
        try {
            existingBooking.setEmployee(booking.getEmployee());
            existingBooking.setDesk(booking.getDesk());
            existingBooking.setDate(booking.getDate());
            existingBooking.setEndTime(booking.getEndTime());
            return deskBookingDBAccess.addBooking(existingBooking);
        } catch (DeskNotAvailableException e) {
            deskBookingDBAccess.addBooking(existingBooking);
            throw e;
        }
    }

    @Override
    public List<DeskBooking> findByDeskAndBookingEndAfterAndBookingStartBefore(Desk desk, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return null;
    }

    @Override
    public void deleteBookingById(Long bookingId) throws BookingNotFoundException {
        Optional<DeskBooking> booking = deskBookingDBAccess.getBookingByBookingId(bookingId);
        if (!booking.isPresent()) {
            throw new BookingNotFoundException("Booking not Found!");
        }
        deskBookingDBAccess.deleteBookingById(bookingId);
    }

    @Override
    public List<Desk> getAvailableDesks(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return null;
    }

    @Override
    public boolean isDeskAvailable(Desk desk, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return false;
    }

    @Override
    public List<Desk> getAvailableDesks(LocalDate date, LocalTime bookingStart, LocalTime bookingEnd) {
        List<Desk> allDesks = deskDBAccess.getAllDesks();
        List<Desk> availableDesks = new ArrayList<>();
        for (Desk desk : allDesks) {
            List<DeskBooking> bookings = getBookingsByDeskAndDateAndBookingStartBetween (desk, date, bookingStart, bookingEnd);
            if (bookings.isEmpty()) {
                availableDesks.add(desk);
            }
        }
        return availableDesks;
    }

    private List<DeskBooking> getBookingsByDeskAndDateAndBookingStartBetween(Desk desk, LocalDate date, LocalTime bookingStart, LocalTime bookingEnd) {
        return null;
    }

    @Override
    public boolean isDeskAvailable(Desk desk, LocalDate date, LocalTime startDateTime, LocalTime endDateTime) {
        List<DeskBooking> bookings = deskBookingDBAccess.getBookingsByDeskAndDateAndBookingTimeBetween(desk, date, startDateTime, endDateTime);
        return bookings.isEmpty();
    }

    @Override
    public void deleteBooking(Long id) throws BookingNotFoundException {
        deskBookingDBAccess.deleteBookingById(id);
    }

    @Override
    public List<DeskBooking> getMyBookingHistory(Long employeeId) {
        return null;
    }
}