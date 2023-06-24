package com.itkolleg.bookingsystem.repos.RessourceBooking;

import com.itkolleg.bookingsystem.domains.Booking.RessourceBooking;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Ressource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RessourceBookingJPARepo extends JpaRepository<RessourceBooking, Long> {

    Optional<RessourceBooking> getBookingByBookingId(Long bookingId);

    List<RessourceBooking> getBookingsByRessource(Ressource ressource);

    List<RessourceBooking> getBookingsByDate(LocalDate date);

    List<RessourceBooking> getBookingsByEmployee(Employee employee);

    List<RessourceBooking> getBookingsByEmployeeId(Long employeeId);

    List<RessourceBooking>getBookingsByRessourceAndDate(Ressource ressource, LocalDate date);

    List<RessourceBooking> getBookingByRessourceAndEmployee(Ressource ressource, Employee employee);

    List<RessourceBooking> getBookingsByEmployeeAndDate(Employee employee, LocalDate date);

    List<RessourceBooking> getBookingsByEmployeeAndDateAndRessource(Employee employee, LocalDate date, Ressource ressource);

    List<RessourceBooking> getBookingsByDateAndStartBetween(LocalDate date, LocalTime start, LocalTime end);

    List<RessourceBooking>getBookingsByRessourceAndDateAndStartBetween(Ressource ressource, LocalDate date, LocalTime start, LocalTime end);

    List<RessourceBooking> getBookingsByEmployeeIdAndDateAndRessourceId(Long employeeId, LocalDate date, Long ressourceId);

    List<RessourceBooking> getBookingsByRessourceId(Long ressourceId);
}
