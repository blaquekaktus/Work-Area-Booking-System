package com.itkolleg.bookingsystem.repos.DeskBooking;

import com.itkolleg.bookingsystem.domains.Booking.DeskBooking;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeskBookingJPARepo extends JpaRepository<DeskBooking, Long> {

    //Page<DeskBooking> getAllBookingsByPage(Pageable pageable);
    Optional<DeskBooking> getBookingById(Long bookingId);
    List<DeskBooking> getBookingByDesk(Optional<Desk> desk);
    List<DeskBooking> getBookingsByDate(LocalDate date);
    List<DeskBooking> getBookingsByEmployee(Employee employee);
    List<DeskBooking> getBookingsByEmployeeId(Long employeeId);
    List<DeskBooking> getBookingsByDeskAndDate(Optional<Desk> desk, LocalDate date);
    List<DeskBooking> getBookingByDeskAndEmployee(Desk desk, Employee employee);
    List<DeskBooking> getBookingsByEmployeeAndDate(Optional<Employee> employee, LocalDate date);
    List<DeskBooking> getBookingsByEmployeeAndDateAndDesk(Employee employee, LocalDate date, Desk desk);
    List<DeskBooking> getBookingsByDateAndStartBetween(LocalDate date, LocalTime start, LocalTime end);
    List<DeskBooking> getBookingsByDeskAndDateAndStartBetween(Desk desk, LocalDate date, LocalTime start, LocalTime end);
    List<DeskBooking> getBookingsByEmployeeIdAndDateAndDeskId(Long employeeId, LocalDate date, Long deskId);

    Desk getBookingByDeskId(Long id);
}
