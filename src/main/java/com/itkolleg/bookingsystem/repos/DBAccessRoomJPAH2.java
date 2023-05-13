package com.itkolleg.bookingsystem.repos;

import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class DBAccessRoomJPAH2 implements DBAccessRoom {
    @Override
    public Room addRoom(Room room) throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public List<Room> getAllRooms() throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public Room getRoomById(Long id) throws RoomNotFoundException, ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public Room updateRoomById(Room room) throws RoomNotFoundException, ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public void deleteRoomById(Long id) throws RoomDeletionNotPossibleException {

    }
}
