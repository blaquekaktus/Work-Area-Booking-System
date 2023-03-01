package com.itkolleg.bookingsystem.repos;

import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskNotFoundException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface DBAccessDesks {
    public Desk addDesk(Desk desk);
    public List<Desk> getAllDesk() throws ExecutionException, InterruptedException;


    public Desk getDeskById(Long id) throws DeskNotFoundException;
    public Desk updateDeskById(Desk desk) throws DeskNotFoundException;
    public void deleteDeskById(Long id) throws DeskDeletionNotPossibleException;
}
