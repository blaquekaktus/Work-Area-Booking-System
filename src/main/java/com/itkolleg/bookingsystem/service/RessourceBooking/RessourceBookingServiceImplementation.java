package com.itkolleg.bookingsystem.service.RessourceBooking;

import com.itkolleg.bookingsystem.domains.Booking.DeskBooking;
import com.itkolleg.bookingsystem.domains.Booking.RessourceBooking;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.DeskNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceNotAvailableException;
import com.itkolleg.bookingsystem.repos.Desk.DeskRepo;
import com.itkolleg.bookingsystem.repos.DeskBooking.DeskBookingRepo;
import com.itkolleg.bookingsystem.repos.Employee.EmployeeDBAccess;
import com.itkolleg.bookingsystem.repos.Holiday.HolidayRepo;
import com.itkolleg.bookingsystem.repos.Ressource.DBAccessRessource;
import com.itkolleg.bookingsystem.repos.Ressource.RessourceJPARepo;
import com.itkolleg.bookingsystem.repos.RessourceBooking.RessourceBookingRepo;
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
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class RessourceBookingServiceImplementation implements RessourceBookingService {

    Logger logger = LoggerFactory.getLogger(RessourceBookingServiceImplementation.class);

    private final RessourceBookingRepo ressourceBookingRepo;
    private final DBAccessRessource ressourceRepo;
    private final EmployeeDBAccess employeeDBAccess;


    public RessourceBookingServiceImplementation(RessourceBookingRepo ressourceBookingRepo, DBAccessRessource ressourceRepo, EmployeeDBAccess employeeDBAccess) {
        this.ressourceBookingRepo = ressourceBookingRepo;
        this.ressourceRepo = ressourceRepo;
        this.employeeDBAccess = employeeDBAccess;
    }

    @Override
    public RessourceBooking addRessourceBooking(RessourceBooking booking) throws RessourceNotAvailableException, ResourceNotFoundException {
        List<RessourceBooking> bookings = this.ressourceBookingRepo.getBookingsByRessourceAndDateAndBookingTimeBetween(booking.getRessource(), booking.getDate(),booking.getStart(), booking.getEndTime());
        LocalDate currentDate = LocalDate.now();
        System.out.println("Booking date: " + booking.getDate());
        System.out.println("Current date: " + LocalDate.now());
        //Check if ressource is available for the date and time chosen
        if (!bookings.isEmpty()) {
            throw new RessourceNotAvailableException("Ressource not available for booking period");
        }
        // Check if booking is for a past date
        if (booking.getDate().isBefore(currentDate)) {
            throw new IllegalArgumentException("Cannot create booking for a past date");
        }

        return this.ressourceBookingRepo.addBooking(booking);
    }

    @Override
    public List<RessourceBooking> getAllBookings() {
        return this.ressourceBookingRepo.getAllBookings();
    }

    @Override
    public List<RessourceBooking> getBookingsByEmployeeId(Long employeeId) {
        return this.ressourceBookingRepo.getBookingsByEmployeeId(employeeId);
    }

    @Override
    public List<RessourceBooking> getBookingsByEmployee(Employee employee) {
        return this.ressourceBookingRepo.getBookingsByEmployee(employee);
    }

    @Override
    public List<RessourceBooking> getBookingsByRessource(Ressource ressource) {
        return this.ressourceBookingRepo.getBookingsByRessource(ressource);
    }

    @Override
    public List<RessourceBooking> getBookingsByDate(LocalDate date) {
        LocalTime startOfDay = LocalTime.from(date.atStartOfDay());
        LocalTime endOfDay = startOfDay.plusHours(24).minusSeconds(1);
        return this.ressourceBookingRepo.getBookingByDateAndByStartBetween(date, startOfDay, endOfDay);
    }

    @Override
    public RessourceBooking getBookingById(Long bookingId) throws ResourceNotFoundException {
        Optional<RessourceBooking> optionalBooking = this.ressourceBookingRepo.getBookingByBookingId(bookingId);
        if (optionalBooking.isPresent()) {
            return optionalBooking.get();
        } else {
            throw new ResourceNotFoundException("Booking with ID " + bookingId + " not found.");
        }
    }

    public RessourceBooking updateBookingById(Long bookingId, RessourceBooking updatedBooking) throws ResourceNotFoundException, RessourceNotAvailableException {
        Optional<RessourceBooking> booking = this.ressourceBookingRepo.getBookingByBookingId(bookingId);
        if (booking.isEmpty()) {
            throw new ResourceNotFoundException("Booking not found for id: " + bookingId);
        }

        Ressource ressource = booking.get().getRessource();
        LocalDate date = booking.get().getDate();
        LocalTime start = booking.get().getStart();
        LocalTime endTime = booking.get().getEndTime();
        if(isRessourceAvailable(ressource, date, start, endTime)) {
            updatedBooking.setId(bookingId);
        }
        return this.ressourceBookingRepo.updateBooking(updatedBooking);
    }

    @Override
    public RessourceBooking updateBooking(RessourceBooking booking) throws ResourceNotFoundException, RessourceNotAvailableException, ResourceNotFoundException {
        try {
            RessourceBooking existingBooking = this.ressourceBookingRepo.getBookingByBookingId(booking.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found for id: " + booking.getId()));
            // Check if the ressource is available for the updated booking period
            List<RessourceBooking> bookings = ressourceBookingRepo.getBookingsByRessourceAndDateAndBookingTimeBetween(booking.getRessource(), booking.getDate(), booking.getStart(), booking.getEndTime());
            bookings.remove(existingBooking);
            if (!bookings.isEmpty()) {
                throw new RessourceNotAvailableException("Ressource not available for booking period");
            }
            existingBooking.setEmployee(booking.getEmployee());
            existingBooking.setRessource(booking.getRessource());
            existingBooking.setDate(booking.getDate());
            existingBooking.setStart(booking.getStart());
            existingBooking.setEndTime(booking.getEndTime());
            existingBooking.setCreatedOn(LocalDateTime.now());
            return this.ressourceBookingRepo.addBooking(existingBooking);
        }catch (DataAccessException e){
            throw new ResourceNotFoundException("Database access error occurred for id: " + booking.getId(), e);
        }

    }

    @Override
    public List<RessourceBooking> findByRessourceAndBookingEndAfterAndBookingStartBefore(Ressource ressource, LocalDate date, LocalTime start, LocalTime endTime) {
        return ressourceBookingRepo.getBookingsByRessourceAndDateAndBookingTimeBetween(ressource, date, start, endTime);
    }

    @Override
    public void deleteBookingById(Long bookingId) throws ResourceDeletionFailureException, ResourceNotFoundException {
        Optional<RessourceBooking> booking = this.ressourceBookingRepo.getBookingByBookingId(bookingId);
        if (booking.isEmpty()) {
            throw new ResourceDeletionFailureException("Booking not Found!");
        }
        ressourceBookingRepo.deleteBookingById(bookingId);
    }

    @Override
    public List<Ressource> getAvailableRessources(LocalDate date, LocalTime start, LocalTime endTime) throws ExecutionException, InterruptedException {
        return this.ressourceRepo.getAllRessource().stream()
                .filter(ressource -> ressourceBookingRepo.getBookingsByRessourceAndDateAndBookingTimeBetween(ressource, date, start, endTime).isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public boolean isRessourceAvailable(Ressource ressource, LocalDate date, LocalTime startDateTime, LocalTime endDateTime) {
        List<RessourceBooking> bookings = this.ressourceBookingRepo.getBookingsByRessourceAndDateAndBookingTimeBetween(ressource, date, startDateTime, endDateTime);
        return bookings.isEmpty();
    }

    @Override
    public void deleteBooking(Long id) throws ResourceDeletionFailureException, ResourceNotFoundException {
        this.ressourceBookingRepo.deleteBookingById(id);
    }


    @Override
    public List<RessourceBooking> getMyBookingHistory(Long employeeId) {
        return this.ressourceBookingRepo.getBookingsByEmployeeId(employeeId);
    }

    @Override
    public RessourceBooking save(RessourceBooking booking) {
        return this.ressourceBookingRepo.save(booking);
    }
}