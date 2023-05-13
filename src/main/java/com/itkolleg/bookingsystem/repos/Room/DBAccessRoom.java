package com.itkolleg.bookingsystem.repos.Room;

import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceNotFoundException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomNotFoundException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface DBAccessRoom {
    public Room addRoom(Room room) throws ExecutionException, InterruptedException;
    public List<Room> getAllRooms() throws ExecutionException, InterruptedException;
    public Room getRoomById(Long id) throws RoomNotFoundException, ExecutionException, InterruptedException;
    public Room updateRoomById(Room room) throws RoomNotFoundException, ExecutionException, InterruptedException;

    public void deleteRoomById(Long id) throws RoomDeletionNotPossibleException;
}
