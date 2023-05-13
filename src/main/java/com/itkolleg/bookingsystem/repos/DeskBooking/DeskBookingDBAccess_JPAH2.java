package com.itkolleg.bookingsystem.repos.DeskBooking;

import com.itkolleg.bookingsystem.domains.Booking.DeskBooking;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.BookingExceptions.BookingNotFoundException;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskNotAvailableException;
import com.itkolleg.bookingsystem.repos.Desk.DeskJPARepo;
import com.itkolleg.bookingsystem.repos.Employee.EmployeeJPARepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DeskBookingDBAccess_JPAH2 implements DeskBookingDBAccess {
    private static final Logger logger = LoggerFactory.getLogger(DeskBookingDBAccess_JPAH2.class);
    private final DeskBookingJPARepo deskBookingJPARepo;
    private final DeskJPARepo deskJPARepo;
    private final EmployeeJPARepo employeeJPARepo;

    public DeskBookingDBAccess_JPAH2(DeskBookingJPARepo deskBookingJPARepo, DeskJPARepo deskJPARepo, EmployeeJPARepo employeeJPARepo) {
        this.deskBookingJPARepo = deskBookingJPARepo;
        this.deskJPARepo = deskJPARepo;
        this.employeeJPARepo = employeeJPARepo;
    }

    @Override
    public DeskBooking addBooking(DeskBooking booking) throws DeskNotAvailableException {
        // Check if the desk is available for the booking period
        List<DeskBooking> bookings = deskBookingJPARepo.getBookingsByDeskAndDate(Optional.ofNullable(booking.getDesk()), booking.getDate());
        if (!bookings.isEmpty()) {
            throw new DeskNotAvailableException("Desk not available for booking period");
        }
        return this.deskBookingJPARepo.save(booking);
    }

    @Override
    public List<DeskBooking> getAllBookings() {
        return this.deskBookingJPARepo.findAll();
    }

    @Override
    public Optional<DeskBooking> getBookingByBookingId(Long bookingId) throws BookingNotFoundException {
        Optional<DeskBooking> bookingOptional = deskBookingJPARepo.findById(bookingId);
        if (bookingOptional.isEmpty()) {
            String message = "The Booking with the ID: " + bookingId + " was not found!";
            logger.error(message);
            throw new BookingNotFoundException(message);
        }
        return bookingOptional;
    }

    @Override
    public List<DeskBooking> getBookingByDesk(Desk desk) {
        return this.deskBookingJPARepo.getBookingByDesk(Optional.ofNullable(desk));
    }

    @Override
    public List<DeskBooking> getBookingsByEmployee(Employee employee) {
        return this.deskBookingJPARepo.getBookingsByEmployee(employee);
    }

    @Override
    public List<DeskBooking> getBookingsByEmployeeAndDate(Employee employee, LocalDate date) {
        return this.deskBookingJPARepo.getBookingsByEmployeeAndDate(Optional.ofNullable(employee), date);
    }

    @Override
    public List<DeskBooking> searchBookings(Long employeeId, Long deskId, LocalDate date) {
        List<DeskBooking> bookings = new ArrayList<>();
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


    @Override
    public List<DeskBooking> getByDeskAndDate(Desk desk, LocalDate date) {
        return this.deskBookingJPARepo.getBookingsByDeskAndDate(Optional.ofNullable(desk), date);
    }

    @Override
    public List<DeskBooking> getBookingByDate(LocalDate date) {
        return null;
    }

    @Override
    public List<DeskBooking> getBookingsByEmployeeId(Long employeeId) {
        return this.deskBookingJPARepo.getBookingsByEmployeeId(employeeId);
    }
    public List<DeskBooking> getBookingsByDateAndBookingStartBetween(LocalDate date, LocalTime bookingStart, LocalTime bookingEnd) {
        return this.deskBookingJPARepo.getBookingsByDateAndStartBetween(date, bookingStart, bookingEnd);
    }
    @Override
    public DeskBooking updateBookingByBookingId(Long deskBookingId, DeskBooking updatedDeskBooking) throws BookingNotFoundException {
        Optional<DeskBooking>bookingOptional = this.deskBookingJPARepo.findById(deskBookingId);
        if (bookingOptional.isPresent()) {
            DeskBooking deskBooking = bookingOptional.get();
            deskBooking.setDesk(updatedDeskBooking.getDesk());
            deskBooking.setEmployee(updatedDeskBooking.getEmployee());
            deskBooking.setDate(updatedDeskBooking.getDate());
            deskBooking.setEndTime(updatedDeskBooking.getEndTime());
            deskBooking.setTimeStamp(updatedDeskBooking.getTimeStamp());
            return this.deskBookingJPARepo.save(deskBooking);
        }else{
            throw new BookingNotFoundException("The Desk Booking with the ID: " + deskBookingId + " was not found!");
        }
    }
    @Override
    public DeskBooking updateBooking(DeskBooking updatedBooking) {
        return this.deskBookingJPARepo.save(updatedBooking);
    }
    @Override
    public void deleteBookingById(Long bookingId) throws BookingNotFoundException {
        Optional<DeskBooking>bookingOptional = this.deskBookingJPARepo.findById(bookingId);
        if (bookingOptional.isPresent()) {
            this.deskBookingJPARepo.deleteById(bookingId);
        }else{
            throw new BookingNotFoundException("The Desk Booking with the ID: " + bookingId + " was not found!");
        }
    }

    @Override
    public List<Desk> getAvailableDesks(LocalDate date, LocalTime start, LocalTime end) {
        List<Desk> allDesks = deskJPARepo.findAll();
        List<DeskBooking> bookings = deskBookingJPARepo.getBookingsByDateAndStartBetween(date, start, end);
        List<Desk> availableDesks = new ArrayList<>();

        for (Desk desk : allDesks) {
            boolean isBooked = false;
            for (DeskBooking booking : bookings) {
                if (desk.equals(booking.getDesk())) {
                    isBooked = true;
                    break;
                }
            }
            if (!isBooked) {
                availableDesks.add(desk);
            }
        }
        return availableDesks;
    }

    @Override
    public boolean isDeskAvailable(Desk desk, LocalDate date, LocalTime start, LocalTime end) {
        List<DeskBooking> overlappingBookings = deskBookingJPARepo.getBookingsByDeskAndDateAndStartBetween(desk, date, start, end);
        return overlappingBookings.isEmpty();
    }

    @Override
    public List<DeskBooking> getBookingByDateAndByStartBetween(LocalDate date, LocalTime startOfDay, LocalTime endOfDay) {
        return deskBookingJPARepo.getBookingsByDateAndStartBetween(date, startOfDay, endOfDay);
    }

    @Override
    public List<DeskBooking> getBookingsByDeskAndDateAndBookingTimeBetween(Desk desk, LocalDate date, LocalTime start, LocalTime end) {
        return deskBookingJPARepo.getBookingsByDeskAndDateAndStartBetween(desk, date, start, end);
    }


}