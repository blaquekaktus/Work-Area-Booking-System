package com.itkolleg.bookingsystem.repos;

import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskNotFoundException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface DBAccessDesks {
    public Desk addDesk(Desk desk) throws ExecutionException, InterruptedException;
    public List<Desk> getAllDesk() throws ExecutionException, InterruptedException;


    public Desk getDeskById(Long id) throws DeskNotFoundException, ExecutionException, InterruptedException;
    public Desk updateDeskById(Desk desk) throws DeskNotFoundException, ExecutionException, InterruptedException;
    public void deleteDeskById(Long id) throws DeskDeletionNotPossibleException;
}
