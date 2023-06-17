package com.itkolleg.bookingsystem.service.Desk;

import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Port;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.repos.Desk.DeskRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DeskServiceImplementation implements DeskService {

    private final DeskRepo deskRepo;


    public DeskServiceImplementation(DeskRepo deskRepo) {
        this.deskRepo = deskRepo;
    }

    @Override
    public Desk addDesk(Desk desk) {
        return this.deskRepo.addDesk(desk);
    }

    public int getTotalDesks() {
        return this.deskRepo.getAllDesks().size();
    }

    @Override
    public List<Desk> getAllDesks() {
        return this.deskRepo.getAllDesks();
    }

    public Page<Desk> getAllDesksByPage(Pageable pageable) {
        return this.deskRepo.getAllDesksByPage(pageable);
    }

    @Override
    public Desk getDeskById(Long id) throws ResourceNotFoundException {
        return this.deskRepo.getDeskById(id);
    }

    @Override
    public Desk updateDeskById(Long id, Desk desk) throws ResourceNotFoundException {
        return this.deskRepo.updateDeskById(id, desk);
    }

    public Desk updateDesk(Desk desk) throws ResourceNotFoundException {
        return this.deskRepo.updateDesk(desk);
    }

    @Override
    public List<Desk> deleteDeskById(Long id) throws ResourceDeletionFailureException {
        this.deskRepo.deleteDeskById(id);
        return this.deskRepo.getAllDesks();
    }

    @Override
    public Desk createPort(Long deskId, Port newPort) {
        return this.deskRepo.createPort(deskId, newPort);
    }

    @Override
    public Desk updatePort(Long deskId, String portName, Port updatedPort) {
        return this.deskRepo.updatePort(deskId, portName, updatedPort);
    }

    @Override
    public Desk deletePort(Long deskId, String portName) {
        return this.deskRepo.deletePort(deskId, portName);
    }

    public List<Port> getPorts(Long deskId) {
        return this.deskRepo.getPorts(deskId);
    }

}