package com.itkolleg.bookingsystem.repos.Desk;

import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Port;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DeskDBAccess_JPAH2 implements DeskDBAccess {

    DeskJPARepo deskJPARepo;

    public DeskDBAccess_JPAH2(DeskJPARepo deskJPARepo) {
        this.deskJPARepo = deskJPARepo;
    }

    @Override
    public Desk addDesk(Desk desk) {
        return this.deskJPARepo.save(desk);
    }

    @Override
    public List<Desk> getAllDesks() {
        return this.deskJPARepo.findAll();
    }

    public int getTotalDesks() {
        return this.getAllDesks().size();
    }
    public Page<Desk> getAllDesksByPage(Pageable pageable){
        return this.deskJPARepo.findAllDesksByPage(pageable);
    }

    @Override
    public Desk getDeskById(Long id) throws DeskNotFoundException {
        Optional<Desk> deskOptional = this.deskJPARepo.findById(id);
        if (deskOptional.isPresent()) {
            return deskOptional.get();
        } else {
            throw new DeskNotFoundException("The Desk with the ID: " + id + " was not found!");
        }
    }

    @Override
    public Desk updateDeskById(Long id, Desk updatedDesk) throws DeskNotFoundException {
        Optional<Desk> deskOptional = this.deskJPARepo.findById(id);
        if (deskOptional.isPresent()){
            Desk desk = deskOptional.get();
            desk.setDeskNr(updatedDesk.getDeskNr());
            desk.setNrOfMonitors(updatedDesk.getNrOfMonitors());
            desk.setPorts(updatedDesk.getPorts());
            return this.deskJPARepo.save(desk);
        }else{
            throw new DeskNotFoundException("The Desk with the ID: " + id + " was not found!");
        }
    }

    public Desk updateDesk(Desk updatedDesk) throws DeskNotFoundException {
        try {
            Optional<Desk> desk = this.deskJPARepo.findById(updatedDesk.getId());
            if(desk.isPresent())
            {
                Desk deskToUpdate = desk.get();
                deskToUpdate.setDeskNr(updatedDesk.getDeskNr());
                deskToUpdate.setNrOfMonitors(updatedDesk.getNrOfMonitors());
                deskToUpdate.deleteAllPorts();
                for(Port p : updatedDesk.getPorts())
                {
                    deskToUpdate.addPort(new Port(p.getName()));
                }
                return this.deskJPARepo.save(deskToUpdate);
            } else
            {
                throw new DeskNotFoundException("Desk with ID " +updatedDesk.getId() + " not found!");
            }

        } catch (Exception e) {
            throw new DeskNotFoundException("Desk not found");
        }
    }

    @Override
    public List<Desk> deleteDeskById(Long id) throws DeskDeletionFailureException {
        Optional<Desk> deskOptional = this.deskJPARepo.findById(id);
        if (deskOptional.isPresent()) {
            this.deskJPARepo.deleteById(id);
            return this.deskJPARepo.findAll();
        } else {
            throw new DeskDeletionFailureException("The Desk with the ID: " + id + " was not possible!");
        }
    }

    @Override
    public Desk createPort(Long deskId, Port newPort) {
        Desk desk = deskJPARepo.findById(deskId).orElse(null);
        if (desk != null) {
            List<Port> ports = desk.getPorts();
            ports.add(newPort);
            desk.setPorts(ports);
            return deskJPARepo.save(desk);
        }
        return null;

    }

    @Override
    public Desk updatePort(Long deskId, String portName, Port updatedPort) {
        Desk desk = deskJPARepo.findById(deskId).orElse(null);
        if (desk != null) {
            List<Port> ports = desk.getPorts();
            for (Port port : ports) {
                if (port.getName().equals(portName)) {
                    port.setName(updatedPort.getName());
                    break;
                }
            }
            desk.setPorts(ports);
            return deskJPARepo.save(desk);
        }
        return null;
    }

    @Override
    public Desk deletePort(Long deskId, String portName) {
        Desk desk = deskJPARepo.findById(deskId).orElse(null);
        if (desk != null) {
            List<Port> ports = desk.getPorts();
            ports.removeIf(port -> port.getName().equals(portName));
            desk.setPorts(ports);
            return deskJPARepo.save(desk);
        }
        return null;
    }

    @Override
    public List<Port> getPorts(Long deskId) {
        Desk desk = deskJPARepo.findById(deskId).orElse(null);
        if (desk != null) {
            return desk.getPorts();
        }
        return null;
    }

}
