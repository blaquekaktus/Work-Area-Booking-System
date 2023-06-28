package com.itkolleg.bookingsystem.repos.RessourceBooking;
import com.itkolleg.bookingsystem.domains.Booking.RessourceBooking;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceNotAvailableException;
import com.itkolleg.bookingsystem.repos.Employee.EmployeeJPARepo;
import com.itkolleg.bookingsystem.repos.Ressource.RessourceJPARepo;
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

@Component
@ComponentScan({"com.itkolleg.repos"})
public class RessourceBookingRepo_JPAH2 implements RessourceBookingRepo {
    private static final Logger logger = LoggerFactory.getLogger(RessourceBookingRepo_JPAH2.class);
    private final RessourceBookingJPARepo ressourceBookingJPARepo;
    private final RessourceJPARepo ressourceJPARepo;
    private final EmployeeJPARepo employeeJPARepo;

    public RessourceBookingRepo_JPAH2(RessourceBookingJPARepo ressourceBookingJPARepo, RessourceJPARepo ressourceJPARepo, EmployeeJPARepo employeeJPARepo) {
        this.ressourceBookingJPARepo = ressourceBookingJPARepo;
        this.ressourceJPARepo = ressourceJPARepo;
        this.employeeJPARepo = employeeJPARepo;
    }

    @Override
    public RessourceBooking addBooking(RessourceBooking ressourceBooking) throws ResourceNotFoundException, RessourceNotAvailableException {
        // Check for null values
        if(ressourceBooking == null || ressourceBooking.getRessource() == null) {
            throw new IllegalArgumentException("The RessourceBooking or Ressource cannot be null.");
        }

        // Load the associated ressource entity from the database
        Long ressourceid = ressourceBooking.getRessource().getId();
        Ressource ressource = this.ressourceJPARepo.findRessourceById(ressourceid);

        // Check if the ressource is available for the booking period
        if (!isRessourceAvailable(ressource, ressourceBooking.getDate(), ressourceBooking.getStart(), ressourceBooking.getEndTime())) {
          throw new RessourceNotAvailableException("Ressource not available for booking period");
        }

        // Create a new Booking entity
        RessourceBooking booking = new RessourceBooking();
        booking.setEmployee(ressourceBooking.getEmployee());
        booking.setRessource(ressource);
        booking.setDate(ressourceBooking.getDate());
        booking.setStart(ressourceBooking.getStart());
        booking.setEndTime(ressourceBooking.getEndTime());

        // Set created and updated timestamps
        booking.setCreatedOn(LocalDateTime.now());
        booking.setUpdatedOn(LocalDateTime.now());

        // Save the booking
        try {
            return this.ressourceBookingJPARepo.save(booking);
        } catch (Exception e) {
            throw new RuntimeException("Error saving the booking to the database", e);
        }
    }

    @Override
    public List<RessourceBooking> getAllBookings() {
        return this.ressourceBookingJPARepo.findAll();
    }

    public Optional<RessourceBooking> getBookingsByBookingId(Long id) throws ResourceNotFoundException {
        return this.ressourceBookingJPARepo.findById(id);
    }

    @Override
    public List<RessourceBooking> getBookingsByEmployeeId(Long employeeId) {
        return this.ressourceBookingJPARepo.getBookingsByEmployeeId(employeeId);
    }

    @Override
    public List<RessourceBooking> getBookingsByRessource(Ressource ressource) {
        return this.ressourceBookingJPARepo.getBookingsByRessource(ressource);
    }

    public List<RessourceBooking> getBookingsByRessourceId(Long id) {
        return this.ressourceBookingJPARepo.getBookingsByRessourceId(id);
    }

    @Override
    public Optional<RessourceBooking> getBookingByBookingId(Long id) {
        return this.ressourceBookingJPARepo.findById(id);
    }

    @Override
    public List<RessourceBooking> getBookingsByEmployee(Employee employee) {
        return this.ressourceBookingJPARepo.getBookingsByEmployee(employee);
    }

    @Override
    public List<RessourceBooking> getBookingsByEmployeeAndDate(Employee employee, LocalDate date) {
        return this.ressourceBookingJPARepo.getBookingsByEmployeeAndDate(employee, date);
    }

    @Override
    public List<RessourceBooking> getBookingsByRessourceAndDate(Ressource ressource, LocalDate date) {
        return this.ressourceBookingJPARepo.getBookingsByRessourceAndDate(ressource, date);
    }

    @Override
    public List<RessourceBooking> getBookingByDate(LocalDate date) {
        return this.ressourceBookingJPARepo.getBookingsByDate(date);
    }

    @Override
    public RessourceBooking updateBookingById(Long ressourceBookingId, RessourceBooking updatedRessourceBooking) throws ResourceNotFoundException {
        // Checking for mandatory fields on the updated booking
        if(updatedRessourceBooking.getRessource() == null || updatedRessourceBooking.getStart() == null || updatedRessourceBooking.getEndTime() == null || updatedRessourceBooking.getCreatedOn() == null) {
            throw new IllegalArgumentException("Updated Booking must have valid Desk, Employee, Date, StartTime, EndTime and Creation Date.");
        }

        return this.ressourceBookingJPARepo.findById(ressourceBookingId).map(existingBooking -> {
            Ressource fetchedRessource;
            try {
                fetchedRessource = this.ressourceJPARepo.findById(updatedRessourceBooking.getRessource().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("The Ressource with the ID: " + updatedRessourceBooking.getRessource().getId() + " was not found!"));
            } catch (ResourceNotFoundException e) {
                logger.error(e.getMessage());
                throw new RuntimeException("Failed to update booking due to missing ressource. Original error: " + e.getMessage());
            }

            Employee fetchedEmployee;
            try {
                fetchedEmployee = this.employeeJPARepo.findById(updatedRessourceBooking.getEmployee().getId())
                        .orElseThrow(() -> new EmployeeNotFoundException("The Employee with the ID: " + updatedRessourceBooking.getEmployee().getId() + " was not found!"));
            } catch (EmployeeNotFoundException e) {
                logger.error(e.getMessage());
                throw new RuntimeException("Failed to update booking due to missing employee. Original error: " + e.getMessage());
            }
            existingBooking.setRessource(fetchedRessource);
            existingBooking.setEmployee(fetchedEmployee);
            existingBooking.setDate(updatedRessourceBooking.getDate());
            existingBooking.setStart(updatedRessourceBooking.getStart());
            existingBooking.setEndTime(updatedRessourceBooking.getEndTime());
            existingBooking.setUpdatedOn(LocalDateTime.now());
            return existingBooking;
        }).orElseThrow(() -> new ResourceNotFoundException("The Ressource Booking with the ID: " + ressourceBookingId + " was not found!"));
    }

    @Override
    public RessourceBooking updateBooking(RessourceBooking updatedBooking) throws ResourceNotFoundException {
        if(updatedBooking.getId() == null) {
            throw new IllegalArgumentException("Id cannot be null when updating");
        }
        return this.ressourceBookingJPARepo.saveAndFlush(updatedBooking);
    }

    @Override
    public void deleteBookingById(Long id) throws ResourceDeletionFailureException {
        Optional<RessourceBooking> bookingOptional = this.ressourceBookingJPARepo.findById(id);
        if (bookingOptional.isPresent()) {
            this.ressourceBookingJPARepo.deleteById(id);
        } else {
            throw new ResourceDeletionFailureException("The Ressource Booking with the ID: " + id + " was not found!");
        }
    }

    @Override
    public List<Ressource> getAvailableRessources(LocalDate date, LocalTime start, LocalTime end) {
        List<Ressource> allRessources = this.ressourceJPARepo.findAll();
        List<Ressource> availableRessources = new ArrayList<>();

        for (Ressource ressource : allRessources) {
            if (isRessourceAvailable(ressource, date, start, end)) {
                availableRessources.add(ressource);
            }
        }
        return availableRessources;
    }

    @Override
    public boolean isRessourceAvailable(Ressource ressource, LocalDate date, LocalTime start, LocalTime end) {
        List<RessourceBooking> overlappingBookings = this.ressourceBookingJPARepo.getBookingsByRessourceAndDateAndStartBetween(ressource, date, start, end);
        return overlappingBookings.isEmpty();
    }

    @Override
    public List<RessourceBooking> getBookingsByRessourceAndDateAndBookingTimeBetween(Ressource ressource, LocalDate date, LocalTime start, LocalTime endTime) {
        return this.ressourceBookingJPARepo.getBookingsByRessourceAndDateAndStartBetween(ressource, date, start, endTime);
    }

    @Override
    public List<RessourceBooking> getBookingByDateAndByStartBetween(LocalDate date, LocalTime startOfDay, LocalTime endOfDay) {
        return ressourceBookingJPARepo.getBookingsByDateAndStartBetween(date, startOfDay, endOfDay);
    }

    @Override
    public RessourceBooking save(RessourceBooking booking) {
        return this.ressourceBookingJPARepo.save(booking);
    }
}