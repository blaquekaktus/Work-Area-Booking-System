package com.itkolleg.bookingsystem.Service;

import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomNotFoundException;

import java.util.List;

public interface RoomService {
    public Room addRoom(Room room);
    public List<Room> getAllRooms();
    public Ressource getRoomById(Long id) throws RoomNotFoundException;
    public Ressource updateRoomById(Long id) throws RoomNotFoundException;
    public void deleteRoomById(Long id) throws RoomDeletionNotPossibleException;
}
