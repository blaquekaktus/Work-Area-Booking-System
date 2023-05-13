package com.itkolleg.bookingsystem.Service.Desk;

import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Port;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskNotFoundException;
import com.itkolleg.bookingsystem.repos.Desk.DeskDBAccess;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DeskServiceImplementation implements DeskService {

    private final DeskDBAccess deskDBAccess;


    public DeskServiceImplementation(DeskDBAccess deskDBAccess) {
        this.deskDBAccess = deskDBAccess;
    }

    @Override
    public Desk addDesk(Desk desk) {
        return this.deskDBAccess.addDesk(desk);
    }

    public int getTotalDesks() {
        return this.deskDBAccess.getAllDesks().size();
    }
    @Override
    public List<Desk> getAllDesks() {
        return this.deskDBAccess.getAllDesks();
    }

    public Page<Desk> getAllDesksByPage(Pageable pageable){
        return this.deskDBAccess.getAllDesksByPage(pageable);
    }
    @Override
    public Desk getDeskById(Long id) throws DeskNotFoundException {
        return this.deskDBAccess.getDeskById(id);
    }

    @Override
    public Desk updateDeskById(Long id, Desk desk) throws DeskNotFoundException{
        return this.deskDBAccess.updateDeskById(id, desk);
    }

    public Desk updateDesk(Desk desk) throws DeskNotFoundException {
        return this.deskDBAccess.updateDesk(desk);
    }
    @Override
    public List<Desk> deleteDeskById(Long id) throws DeskDeletionFailureException {
        this.deskDBAccess.deleteDeskById(id);
        return this.deskDBAccess.getAllDesks();
    }

    @Override
    public Desk createPort(Long deskId, Port newPort) {
        return this.deskDBAccess.createPort(deskId, newPort);
    }

    @Override
    public Desk updatePort(Long deskId, String portName, Port updatedPort) {
        return this.deskDBAccess.updatePort(deskId, portName, updatedPort);
    }

    @Override
    public Desk deletePort(Long deskId, String portName) {
        return this.deskDBAccess.deletePort(deskId, portName);
    }

    public List<Port>getPorts(Long deskId){
        return this.deskDBAccess.getPorts(deskId);
    }

}