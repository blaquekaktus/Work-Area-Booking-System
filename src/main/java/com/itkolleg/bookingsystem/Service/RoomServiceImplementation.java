package com.itkolleg.bookingsystem.Service;

import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomNotFoundException;
import com.itkolleg.bookingsystem.repos.DBAccessRoom;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class RoomServiceImplementation implements RoomService {
    private DBAccessRoom dbAccessRoom;

    public RoomServiceImplementation(DBAccessRoom dbAccessRoom){
        this.dbAccessRoom = dbAccessRoom;
    }

    @Override
    public Room addRoom(Room room) throws ExecutionException, InterruptedException {
        return this.dbAccessRoom.addRoom(room);
    }

    @Override
    public List<Room> getAllRooms() throws ExecutionException, InterruptedException {
        return this.dbAccessRoom.getAllRooms();
    }

    @Override
    public Room getRoomById(Long id) throws RoomNotFoundException, ExecutionException, InterruptedException {
        return this.dbAccessRoom.getRoomById(id);
    }

    @Override
    public Room updateRoomById(Room room) throws RoomNotFoundException, ExecutionException, InterruptedException {
        return this.dbAccessRoom.updateRoomById(room);
    }

    @Override
    public void deleteRoomById(Long id) throws RoomDeletionNotPossibleException {
        this.dbAccessRoom.deleteRoomById(id);
    }
}
