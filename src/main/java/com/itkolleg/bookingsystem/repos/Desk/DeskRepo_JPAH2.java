package com.itkolleg.bookingsystem.repos.Desk;

import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Port;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DeskRepo_JPAH2 implements DeskRepo {

    DeskJPARepo deskJPARepo;

    public DeskRepo_JPAH2(DeskJPARepo deskJPARepo) {
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

    public Page<Desk> getAllDesksByPage(Pageable pageable) {
        return this.deskJPARepo.findAllDesksByPage(pageable);
    }

    @Override
    public Desk getDeskById(Long id) throws ResourceNotFoundException {
        Optional<Desk> deskOptional = this.deskJPARepo.findById(id);
        if (deskOptional.isPresent()) {
            return deskOptional.get();
        } else {
            throw new ResourceNotFoundException("The Desk with the ID: " + id + " was not found!");
        }
    }

    @Override
    public Desk updateDeskById(Long id, Desk updatedDesk) throws ResourceNotFoundException {
        Optional<Desk> deskOptional = this.deskJPARepo.findById(id);
        if (deskOptional.isPresent()) {
            Desk desk = deskOptional.get();
            desk.setDeskNr(updatedDesk.getDeskNr());
            desk.setNrOfMonitors(updatedDesk.getNrOfMonitors());
            desk.setPorts(updatedDesk.getPorts());
            return this.deskJPARepo.save(desk);
        } else {
            throw new ResourceNotFoundException("The Desk with the ID: " + id + " was not found!");
        }
    }

    public Desk updateDesk(Desk updatedDesk) throws ResourceNotFoundException {
        try {
            Optional<Desk> desk = this.deskJPARepo.findById(updatedDesk.getId());
            if (desk.isPresent()) {
                Desk deskToUpdate = desk.get();
                deskToUpdate.setDeskNr(updatedDesk.getDeskNr());
                deskToUpdate.setNrOfMonitors(updatedDesk.getNrOfMonitors());
                deskToUpdate.deleteAllPorts();
                for (Port p : updatedDesk.getPorts()) {
                    deskToUpdate.addPort(new Port(p.getName()));
                }
                return this.deskJPARepo.save(deskToUpdate);
            } else {
                throw new ResourceNotFoundException("Desk with ID " + updatedDesk.getId() + " not found!");
            }

        } catch (Exception e) {
            throw new ResourceNotFoundException("Desk not found");
        }
    }

    @Override
    public List<Desk> deleteDeskById(Long id) throws ResourceDeletionFailureException {
        Optional<Desk> deskOptional = this.deskJPARepo.findById(id);
        if (deskOptional.isPresent()) {
            this.deskJPARepo.deleteById(id);
            return this.deskJPARepo.findAll();
        } else {
            throw new ResourceDeletionFailureException("The Desk with the ID: " + id + " was not possible!");
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
