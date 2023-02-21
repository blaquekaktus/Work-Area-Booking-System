package com.itkolleg.bookingsystem.Service;

import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomNotFoundException;

import java.util.List;

public class RoomServiceImplementation implements RoomService {
    @Override
    public Room addRoom(Room room) {
        return null;
    }

    @Override
    public List<Room> getAllRooms() {
        return null;
    }

    @Override
    public Ressource getRoomById(Long id) throws RoomNotFoundException {
        return null;
    }

    @Override
    public Ressource updateRoomById(Long id) throws RoomNotFoundException {
        return null;
    }

    @Override
    public void deleteRoomById(Long id) throws RoomDeletionNotPossibleException {

    }
}
