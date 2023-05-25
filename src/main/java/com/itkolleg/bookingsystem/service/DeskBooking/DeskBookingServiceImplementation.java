package com.itkolleg.bookingsystem.service.DeskBooking;

import com.itkolleg.bookingsystem.domains.Booking.DeskBooking;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.BookingExceptions.BookingNotFoundException;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskNotFoundException;
import com.itkolleg.bookingsystem.repos.Desk.DeskDBAccess;
import com.itkolleg.bookingsystem.repos.DeskBooking.DeskBookingDBAccess;
import com.itkolleg.bookingsystem.repos.Employee.EmployeeDBAccess;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeskBookingServiceImplementation implements DeskBookingService {

    private final DeskBookingDBAccess deskBookingDBAccess;
    private final DeskDBAccess deskDBAccess;
    private final EmployeeDBAccess employeeDBAccess;


    public DeskBookingServiceImplementation(DeskBookingDBAccess deskBookingDBAccess, DeskDBAccess deskDBAccess, EmployeeDBAccess employeeDBAccess) {
        this.deskBookingDBAccess = deskBookingDBAccess;
        this.deskDBAccess = deskDBAccess;
        this.employeeDBAccess = employeeDBAccess;
    }

    @Override
    public DeskBooking addDeskBooking(DeskBooking booking) throws DeskNotAvailableException, DeskNotFoundException {
        List<DeskBooking> bookings = deskBookingDBAccess.getBookingsByDeskAndDateAndBookingTimeBetween(booking.getDesk(), booking.getDate(),booking.getStart(), booking.getEndTime());
        System.out.println(bookings);
        if (!bookings.isEmpty()) {
            throw new DeskNotAvailableException("Desk not available for booking period");
        }
        return this.deskBookingDBAccess.addBooking(booking);
    }

    @Override
    public List<DeskBooking> getAllBookings() {
        return deskBookingDBAccess.getAllBookings();
    }


    @Override
    public List<DeskBooking> searchBookings(Employee employee, LocalDate date) {
        List<DeskBooking> bookings = new ArrayList<>();
        if (employee != null && date != null) {
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

        Desk desk = booking.get().getDesk();
        LocalDate date = booking.get().getDate();
        LocalTime start = booking.get().getStart();
        LocalTime endTime = booking.get().getEndTime();
        if(isDeskAvailable(desk, date, start, endTime)) {
            updatedBooking.setId(bookingId);
        }
        return deskBookingDBAccess.updateBooking(updatedBooking);
    }

    @Override
    public DeskBooking updateBooking(DeskBooking booking) throws BookingNotFoundException, DeskNotAvailableException, DeskNotFoundException {
        try {
            DeskBooking existingBooking = deskBookingDBAccess.getBookingByBookingId(booking.getId())
                    .orElseThrow(() -> new BookingNotFoundException("Booking not found for id: " + booking.getId()));
            // Check if the desk is available for the updated booking period
            List<DeskBooking> bookings = deskBookingDBAccess.getBookingsByDeskAndDateAndBookingTimeBetween(booking.getDesk(), booking.getDate(), booking.getStart(), booking.getEndTime());
            bookings.remove(existingBooking);
            if (!bookings.isEmpty()) {
                throw new DeskNotAvailableException("Desk not available for booking period");
            }
            existingBooking.setEmployee(booking.getEmployee());
            existingBooking.setDesk(booking.getDesk());
            existingBooking.setDate(booking.getDate());
            existingBooking.setStart(booking.getStart());
            existingBooking.setEndTime(booking.getEndTime());
            existingBooking.setTimeStamp(LocalDateTime.now());
            return deskBookingDBAccess.addBooking(existingBooking);
        }catch (DataAccessException e){
            throw new BookingNotFoundException("Database access error occurred for id: " + booking.getId(), e);
        }

    }

    @Override
    public List<DeskBooking> findByDeskAndBookingEndAfterAndBookingStartBefore(Desk desk, LocalDate date, LocalTime start, LocalTime endTime) {
        return deskBookingDBAccess.getBookingsByDeskAndDateAndBookingTimeBetween(desk, date, start, endTime);
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
    public boolean isDeskAvailable(Desk desk, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return false;
    }

    /*@Override
    public List<Desk> getAvailableDesks(LocalDate date, LocalTime start, LocalTime endTime) {
        List<Desk> allDesks = deskDBAccess.getAllDesks();
        List<Desk> availableDesks = new ArrayList<>();
        for (Desk desk : allDesks) {
            List<DeskBooking> bookings = deskBookingDBAccess.getBookingsByDeskAndDateAndBookingTimeBetween(desk, date, start, endTime);
            if (bookings != null && bookings.isEmpty()) {
                availableDesks.add(desk);
            }
        }
        return availableDesks;
    }*/
    @Override
    public List<Desk> getAvailableDesks(LocalDate date, LocalTime start, LocalTime endTime) {
        return deskDBAccess.getAllDesks().stream()
                .filter(desk -> deskBookingDBAccess.getBookingsByDeskAndDateAndBookingTimeBetween(desk, date, start, endTime).isEmpty())
                .collect(Collectors.toList());
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
        return deskBookingDBAccess.getBookingsByEmployeeId(employeeId);
    }

}