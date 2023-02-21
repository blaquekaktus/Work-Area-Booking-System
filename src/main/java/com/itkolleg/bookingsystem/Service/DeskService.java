package com.itkolleg.bookingsystem.Service;

import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskNotFoundException;

import java.util.List;

public interface DeskService {
    public Desk addDesk(Desk desk);
    public List<Desk> getAllDesk();
    public Desk getDeskById(Long id) throws DeskNotFoundException;
    public Desk updateDeskById(Long id) throws DeskNotFoundException;
    public void deleteDeskById(Long id) throws DeskDeletionNotPossibleException;
}
