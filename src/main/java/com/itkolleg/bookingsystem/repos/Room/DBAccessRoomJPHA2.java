package com.itkolleg.bookingsystem.repos.Room;

import com.itkolleg.bookingsystem.domains.Booking.RoomBooking;

import com.itkolleg.bookingsystem.domains.Room;

import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomNotFoundException;

import com.itkolleg.bookingsystem.repos.RoomBooking.RoomBookingRepo;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class DBAccessRoomJPHA2 implements DBAccessRoom {

    private final RoomJPARepo roomJPARepo;
    private final RoomBookingRepo roomBookingRepo;

    /**
     * Konstruktor
     * @param roomJPARepo
     */
    public DBAccessRoomJPHA2(RoomJPARepo roomJPARepo, RoomBookingRepo roomBookingRepo) {
        this.roomJPARepo = roomJPARepo;
        this.roomBookingRepo = roomBookingRepo;
    }


    @Override
    public Room addRoom(Room room) throws ExecutionException, InterruptedException {
/*
        if(room == null) {
            throw new IllegalArgumentException("room darf nicht null sein");
        }

        if (!roomIsOccupied(room, deskBooking.getDate(), deskBooking.getStart(), deskBooking.getEndTime())) {
            throw new DeskNotAvailableException("Desk not available for booking period");
        }

*/
        return this.roomJPARepo.save(room); //.save added und updatet.
    }

    @Override
    public List<Room> getAllRooms() throws ExecutionException, InterruptedException {
        return this.roomJPARepo.findAll();

    }

    @Override
    public Room getRoomById(Long id) throws RoomNotFoundException, ExecutionException, InterruptedException {
        Optional<Room> roomOptional = this.roomJPARepo.findById(id);
        if (roomOptional.isPresent()) {
            return roomOptional.get();
        } else {
            throw new RoomNotFoundException("Booking not found for id: " + id);
        }
    }

    @Override
    public Room updateRoom(Room room) throws RoomNotFoundException, ExecutionException, InterruptedException {
       return this.roomJPARepo.save(room);
    }


    @Override
    public void deleteRoomById(Long id) throws RoomDeletionNotPossibleException {
        List<RoomBooking> bookings = this.roomBookingRepo.getBookingsByRoomId(id);

        if (!bookings.isEmpty()) {
            throw new RoomDeletionNotPossibleException("Room already booked");
        }
        this.roomJPARepo.deleteById(id);
    }

}
