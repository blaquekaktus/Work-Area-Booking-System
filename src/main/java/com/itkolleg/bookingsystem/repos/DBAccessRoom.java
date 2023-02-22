package com.itkolleg.bookingsystem.repos;


import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomNotFoundException;

import java.util.List;

public interface DBAccessRoom {
    public Room addRoom(Room room);
    public List<Room> getAllRoom();
    public Room getRoomById(Long id) throws RoomNotFoundException;
    public Room updateRoomById(Long id) throws RoomNotFoundException;
    public void deleteRoomById(Long id) throws RoomDeletionNotPossibleException;
}
