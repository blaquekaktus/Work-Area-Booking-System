package com.itkolleg.bookingsystem.repos.RoomBooking;

import com.itkolleg.bookingsystem.domains.Booking.RoomBooking;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomNotFoundException;
import com.itkolleg.bookingsystem.repos.Employee.EmployeeJPARepo;
import com.itkolleg.bookingsystem.repos.Room.RoomJPARepo;
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
public class RoomBookingRepo_JPAH2 implements RoomBookingRepo {
    private static final Logger logger = LoggerFactory.getLogger(RoomBookingRepo_JPAH2.class);
    private final RoomBookingJPARepo roomBookingJPARepo;
    private final RoomJPARepo roomJPARepo;
    private final EmployeeJPARepo employeeJPARepo;

    public RoomBookingRepo_JPAH2(RoomBookingJPARepo roomBookingJPARepo, RoomJPARepo roomJPARepo, EmployeeJPARepo employeeJPARepo) {
        this.roomBookingJPARepo = roomBookingJPARepo;
        this.roomJPARepo = roomJPARepo;
        this.employeeJPARepo = employeeJPARepo;
    }

    @Override
    public RoomBooking addBooking(RoomBooking booking) throws RoomNotAvailableException, RoomNotFoundException {
        if (booking == null || booking.getRoom() == null) {

            throw new IllegalArgumentException("The RoomBooking or Room cannot be null!");
        }

        Long roomId = booking.getRoom().getId();
        Room room = this.roomJPARepo.findRoomById(roomId);

        if (!isRoomAvailable(room, booking.getDate(), booking.getStart(), booking.getEndTime())) {
            throw new RoomNotAvailableException("Room not available for booking period!");
        }
        RoomBooking roomBooking = new RoomBooking();
        roomBooking.setEmployee(booking.getEmployee());
        roomBooking.setRoom(room);
        roomBooking.setDate(booking.getDate());
        roomBooking.setStart(booking.getStart());
        roomBooking.setEndTime(booking.getEndTime());
        roomBooking.setCreatedOn(LocalDateTime.now());
        roomBooking.setUpdatedOn(LocalDateTime.now());

        try {
            return this.roomBookingJPARepo.save(roomBooking);
        } catch (Exception e) {
            throw new RuntimeException("Error saving the booking to the database", e);
        }
    }

    @Override
    public List<Room> getAllRooms() {
        return this.roomJPARepo.findAll();
    }

    @Override
    public List<RoomBooking> getAllBookings() {
        return this.roomBookingJPARepo.findAll();
    }

    @Override
    public Optional<RoomBooking> getBookingByBookingId(Long id) {
        return this.roomBookingJPARepo.findById(id);
    }

    @Override
    public List<RoomBooking> getBookingsByRoom(Room room) {
        return this.roomBookingJPARepo.getBookingsByRoom(room);
    }

    @Override
    public List<RoomBooking> getBookingsByRoomId(Long roomId) {
        return this.roomBookingJPARepo.getBookingsByRoomId(roomId);
    }

    @Override
    public List<RoomBooking> getBookingsByDate(LocalDate date) {
        return this.roomBookingJPARepo.getBookingsByDate(date);
    }

    @Override
    public List<RoomBooking> getBookingsByEmployee(Employee employee) {
        return this.roomBookingJPARepo.getBookingsByEmployee(employee);
    }

    @Override
    public List<RoomBooking> getBookingsByEmployeeId(Long employeeId) {
        return this.roomBookingJPARepo.getBookingsByEmployeeId(employeeId);
    }

    @Override
    public List<RoomBooking> getBookingsByRoomAndDate(Room room, LocalDate date) {
        return this.roomBookingJPARepo.getBookingsByRoomAndDate(room, date);
    }

    @Override
    public List<RoomBooking> getBookingsByRoomAndDateAndBookingTimeBetween(Room room, LocalDate date, LocalTime start, LocalTime endTime) {
        return this.roomBookingJPARepo.getBookingsByRoomAndDateAndStartBetween(room, date, start, endTime);
    }

    @Override
    public RoomBooking updateBookingById(Long id, RoomBooking updatedBooking) throws RoomNotFoundException {
        if (updatedBooking.getRoom() == null || updatedBooking.getStart() == null || updatedBooking.getEndTime() == null || updatedBooking.getCreatedOn() == null) {
            throw new IllegalArgumentException("Updated Booking must have valid Room, Employee, Date, StartTime, EndTime and Creation Date.");
        }
        return this.roomBookingJPARepo.findById(id).map(existingBooking -> {
            Room fetchedRoom;
            try {
                fetchedRoom = this.roomJPARepo.findById(updatedBooking.getRoom().getId())
                        .orElseThrow(() -> new RoomNotFoundException());
            } catch (RoomNotFoundException e) {
                logger.error(e.getMessage());
                throw new RuntimeException("Failed to update booking due to missing room. Original error: " + e.getMessage());
            }
            Employee fetchedEmployee;
            try {
                fetchedEmployee = this.employeeJPARepo.findById(updatedBooking.getEmployee().getId())
                        .orElseThrow(() -> new EmployeeNotFoundException("The Employee with the ID: " + updatedBooking.getEmployee().getId() + " was not found!"));
            } catch (EmployeeNotFoundException e) {
                logger.error(e.getMessage());
                throw new RuntimeException("Failed to update booking due to missing employee. Original error: " + e.getMessage());
            }
            existingBooking.setRoom(fetchedRoom);
            existingBooking.setEmployee(fetchedEmployee);
            existingBooking.setDate(updatedBooking.getDate());
            existingBooking.setStart(updatedBooking.getStart());
            existingBooking.setEndTime(updatedBooking.getEndTime());
            existingBooking.setUpdatedOn(LocalDateTime.now());
            return existingBooking;
        }).orElseThrow(() -> new RoomNotFoundException());
    }


    @Override
    public RoomBooking updateBooking(RoomBooking updatedBooking) throws RoomNotFoundException {
        if (updatedBooking.getId() == null) {
            throw new IllegalArgumentException("ID cannot be null when updating");
        }
        return this.roomBookingJPARepo.saveAndFlush(updatedBooking);
    }

    @Override
    public void deleteBookingById(Long id) throws RoomDeletionNotPossibleException {
        Optional<RoomBooking> bookingOptional = this.roomBookingJPARepo.findById(id);
        if (bookingOptional.isPresent()) {
            this.roomBookingJPARepo.deleteById(id);
        } else {
            throw new RoomDeletionNotPossibleException("The room booking with ID:" + id + "was not found!");
        }
    }

    @Override
    public List<Room> getAvailableRooms(LocalDate date, LocalTime start, LocalTime endTime) {
        List<Room> allRooms = this.roomJPARepo.findAll();
        List<Room> availableRooms = new ArrayList<>();

        for (Room room : allRooms) {
            if (isRoomAvailable(room, date, start, endTime)) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    @Override
    public boolean isRoomAvailable(Room room, LocalDate date, LocalTime start, LocalTime endTime) {
        List<RoomBooking> overlappingBookings = this.roomBookingJPARepo.getBookingsByRoomAndDateAndStartBetween(room, date, start, endTime);
        return overlappingBookings.isEmpty();
    }

    @Override
    public List<RoomBooking> getBookingsByEmployeeAndDate(Employee employee, LocalDate date) {
        return this.roomBookingJPARepo.getBookingsByEmployeeAndDate(employee, date);
    }

    @Override
    public List<RoomBooking> getBookingsByDateAndByStartBetween(LocalDate date, LocalTime start, LocalTime endTime) {
        return this.roomBookingJPARepo.getBookingsByDateAndStartBetween(date, start, endTime);
    }

    @Override
    public List<RoomBooking> getBookingsByEmployeeIdAndDateAndRoomId(Long employeeId, LocalDate date, Long roomId) {
        return this.roomBookingJPARepo.getBookingsByEmployeeIdAndDateAndRoomId(employeeId, date, roomId);
    }

    @Override
    public RoomBooking save(RoomBooking booking) {
        return this.roomBookingJPARepo.save(booking);
    }
}
