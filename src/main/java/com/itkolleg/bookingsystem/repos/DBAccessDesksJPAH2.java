package com.itkolleg.bookingsystem.repos;

import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class DBAccessDesksJPAH2 implements DBAccessDesks{
    @Override
    public Desk addDesk(Desk desk) throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public List<Desk> getAllDesk() throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public Desk getDeskById(Long id) throws DeskNotFoundException, ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public Desk updateDeskById(Desk desk) throws DeskNotFoundException, ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public void deleteDeskById(Long id) throws DeskDeletionNotPossibleException {

    }
}
