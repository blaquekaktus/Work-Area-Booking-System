package com.itkolleg.bookingsystem.repos;

import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomNotFoundException;

import java.util.List;

public class DBAccessRoomFirebaseImpl implements DBAccessRoom {
    @Override
    public Room addRoom(Room room) {
        return null;
    }

    @Override
    public List<Room> getAllRoom() {
        return null;
    }

    @Override
    public Room getRoomById(Long id) throws RoomNotFoundException {
        return null;
    }

    @Override
    public Room updateRoomById(Long id) throws RoomNotFoundException {
        return null;
    }

    @Override
    public void deleteRoomById(Long id) throws RoomDeletionNotPossibleException {

    }
}
