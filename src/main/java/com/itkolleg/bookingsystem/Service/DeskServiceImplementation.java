package com.itkolleg.bookingsystem.Service;

import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskNotFoundException;
import com.itkolleg.bookingsystem.repos.DBAccessDesks;

import java.util.List;

public class DeskServiceImplementation implements DeskService{

    private DBAccessDesks dbAccessDesks;

    public DeskServiceImplementation(DBAccessDesks dbAccessDesks){
        this.dbAccessDesks=dbAccessDesks;
    }

    /**
     * Adds a desk entity to the database.
     * Returns the added desk.
     * @param desk
     * @return desk
     */
    @Override
    public Desk addDesk(Desk desk) {
        return this.dbAccessDesks.addDesk(desk);
    }

    /**
     * @return
     */
    @Override
    public List<Desk> getAllDesk() {
        return this.dbAccessDesks.getAllDesk();
    }

    /**
     * @param id
     * @return
     * @throws DeskNotFoundException
     */
    @Override
    public Desk getDeskById(Long id) throws DeskNotFoundException {
        return this.dbAccessDesks.getDeskById(id);
    }

    /**
     * @param id
     * @return
     * @throws DeskNotFoundException
     */
    @Override
    public Desk updateDeskById(Long id) throws DeskNotFoundException {
        return this.dbAccessDesks.updateDeskById(id);
    }

    /**
     * @param id
     * @throws DeskDeletionNotPossibleException
     */
    @Override
    public void deleteDeskById(Long id) throws DeskDeletionNotPossibleException {
        this.dbAccessDesks.deleteDeskById(id);
    }
}
