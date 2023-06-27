package com.itkolleg.bookingsystem.service.RoomBooking;

import com.itkolleg.bookingsystem.domains.Booking.RoomBooking;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomNotFoundException;
import com.itkolleg.bookingsystem.repos.Employee.EmployeeDBAccess;
import com.itkolleg.bookingsystem.repos.Room.DBAccessRoom;
import com.itkolleg.bookingsystem.repos.RoomBooking.RoomBookingRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class RoomBookingServiceImplementation implements RoomBookingService {
    Logger logger= LoggerFactory.getLogger(RoomBookingServiceImplementation.class);

    private final RoomBookingRepo roomBookingRepo;
    private final DBAccessRoom dbAccessRoom;
    private final EmployeeDBAccess employeeDBAccess;

    public RoomBookingServiceImplementation(RoomBookingRepo roomBookingRepo, DBAccessRoom dbAccessRoom, EmployeeDBAccess employeeDBAccess) {
        this.roomBookingRepo = roomBookingRepo;
        this.dbAccessRoom = dbAccessRoom;
        this.employeeDBAccess = employeeDBAccess;
    }

    @Override
    public RoomBooking addRoomBooking(RoomBooking roomBooking) throws RoomNotAvailableException, RoomNotFoundException {
        List<RoomBooking> bookings= this.roomBookingRepo.getBookingsByRoomAndDateAndBookingTimeBetween(roomBooking.getRoom(),roomBooking.getDate(),roomBooking.getStart(),roomBooking.getEndTime());
        LocalDate currentDate=LocalDate.now();
        System.out.println("Booking date: " + roomBooking.getDate());
        System.out.println("Current date: " + LocalDate.now());

        if(!bookings.isEmpty()){
            throw new RoomNotAvailableException("Room not available for booking period");
        }

        if(roomBooking.getDate().isBefore(currentDate)){
            throw new IllegalArgumentException("Cannot create booking a past date");
        }
        return this.roomBookingRepo.addBooking(roomBooking);
    }

    @Override
    public List<RoomBooking> getAllBookings() {
        return this.roomBookingRepo.getAllBookings();
    }

    @Override
    public List<RoomBooking> getBookingsByEmployeeId(Long employeeId) {
        return this.roomBookingRepo.getBookingsByEmployeeId(employeeId);
    }

    @Override
    public List<RoomBooking> getBookingsByEmployee(Employee employee) {
        return this.roomBookingRepo.getBookingsByEmployee(employee);
    }

    @Override
    public List<RoomBooking> getBookingsByRoom(Room room) {
        return this.roomBookingRepo.getBookingsByRoom(room);
    }

    @Override
    public List<RoomBooking> getBookingsByDate(LocalDate localDate) {
        LocalTime startOfDay=LocalTime.from(localDate.atStartOfDay());
        LocalTime endOfDay=startOfDay.plusHours(24).minusSeconds(1);
        return this.roomBookingRepo.getBookingsByDateAndByStartBetween(localDate,startOfDay,endOfDay);
    }

    @Override
    public RoomBooking getBookingById(Long bookingId) throws RoomNotFoundException {
        Optional<RoomBooking> bookingOptional=this.roomBookingRepo.getBookingByBookingId(bookingId);
        if(bookingOptional.isPresent()){
            return bookingOptional.get();
        }else{
            throw new RoomNotFoundException();

        }
    }

    @Override
    public RoomBooking updateBookingById(Long bookingId, RoomBooking updatedBooking) throws RoomNotFoundException, RoomNotAvailableException {
        Optional<RoomBooking> booking=this.roomBookingRepo.getBookingByBookingId(bookingId);
        if(booking.isEmpty()){
            throw new RoomNotFoundException();
        }
        Room room= booking.get().getRoom();
        LocalDate date= booking.get().getDate();
        LocalTime start= booking.get().getStart();
        LocalTime endTime=booking.get().getEndTime();
        if(isRoomAvailable(room,date,start,endTime)){
            updatedBooking.setId(bookingId);
        }

        return this.roomBookingRepo.updateBooking(updatedBooking);



    }

    @Override
    public RoomBooking updateBooking(RoomBooking booking) throws RoomNotFoundException, RoomNotAvailableException {
       try{
           RoomBooking existingBooking =this.roomBookingRepo.getBookingByBookingId(booking.getId())
                   .orElseThrow(() -> new RoomNotFoundException());

           List <RoomBooking> bookings = roomBookingRepo.getBookingsByRoomAndDateAndBookingTimeBetween(booking.getRoom(),booking.getDate(),booking.getStart(),booking.getEndTime());
           bookings.remove(existingBooking);
           if(!bookings.isEmpty()){
               throw new RoomNotAvailableException("Room not available for booking period!");
           }
           existingBooking.setEmployee(booking.getEmployee());
           existingBooking.setRoom(booking.getRoom());
           existingBooking.setDate(booking.getDate());
           existingBooking.setStart(booking.getStart());
           existingBooking.setEndTime(booking.getEndTime());
           existingBooking.setCreatedOn(LocalDateTime.now());
           return this.roomBookingRepo.addBooking(existingBooking);
       }catch(DataAccessException e){
           throw new RoomNotFoundException();
       }
    }


   public List<RoomBooking> findByRoomAndBookingEndAfterAndBookingStartBefore(Room room, LocalDate date, LocalTime start, LocalTime endTime) {
        return this.roomBookingRepo.getBookingsByRoomAndDateAndBookingTimeBetween(room,date,start,endTime);
    }

    @Override
    public void deleteBookingById(Long BookingId) throws RoomNotFoundException, RoomDeletionNotPossibleException {
Optional<RoomBooking> booking= this.roomBookingRepo.getBookingByBookingId(BookingId);
if(booking.isEmpty()){
    throw new RoomDeletionNotPossibleException("Room not found!");
}
roomBookingRepo.deleteBookingById(BookingId);
    }

    @Override
    public List<Room> getAvailableRooms(LocalDate date, LocalTime start, LocalTime endTime) throws ExecutionException, InterruptedException {
        return this.roomBookingRepo.getAllRooms().stream()
                .filter(room -> roomBookingRepo.getBookingsByRoomAndDateAndBookingTimeBetween(room,date,start,endTime).isEmpty()).collect(Collectors.toList());
    }

    @Override
    public boolean isRoomAvailable(Room room, LocalDate date, LocalTime startDateTime, LocalTime endtime) {
        List<RoomBooking> bookings=this.roomBookingRepo.getBookingsByRoomAndDateAndBookingTimeBetween(room,date,startDateTime,endtime);
    return bookings.isEmpty();
    }

    @Override
    public void deleteBooking(Long id) throws RoomNotFoundException, RoomDeletionNotPossibleException {
this.roomBookingRepo.deleteBookingById(id);
    }

    @Override
    public List<RoomBooking> getMyBookingHistory(Long employeeId) {
        return this.roomBookingRepo.getBookingsByEmployeeId(employeeId);
    }

    @Override
    public RoomBooking save(RoomBooking roomBooking) {
        return this.roomBookingRepo.save(roomBooking);
    }
}
