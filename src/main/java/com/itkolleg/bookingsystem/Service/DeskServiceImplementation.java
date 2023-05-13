package com.itkolleg.bookingsystem.Service;

import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.repos.DBAccessDesks;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
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
    public Desk addDesk(Desk desk) throws ExecutionException, InterruptedException {
        return this.dbAccessDesks.addDesk(desk);
    }

    /**
     * @return
     */
    @Override
    public List<Desk> getAllDesk() throws ExecutionException, InterruptedException {
        return this.dbAccessDesks.getAllDesk();
    }

    /**
     * @param id
     * @return
     * @throws DeskNotFoundException
     */
    @Override
    public Desk getDeskById(Long id) throws DeskNotFoundException, ExecutionException, InterruptedException {
        return this.dbAccessDesks.getDeskById(id);
    }

    /**
     * @param
     * @return
     * @throws DeskNotFoundException
     */
    @Override
    public Desk updateDeskById(Desk desk) throws DeskNotFoundException, ExecutionException, InterruptedException {
        return this.dbAccessDesks.updateDeskById(desk);
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
