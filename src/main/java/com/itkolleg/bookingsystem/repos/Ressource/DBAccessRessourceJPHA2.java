package com.itkolleg.bookingsystem.repos.Ressource;

import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceIsOccupied;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class DBAccessRessourceJPHA2 implements DBAccessRessource {

    private final RessourceBookingJPARepo ressourceBookingJPARepo;

    /**
     * Konstruktor
     * @param ressourceBookingJPARepo
     */
    public DBAccessRessourceJPHA2(RessourceBookingJPARepo ressourceBookingJPARepo) {
        this.ressourceBookingJPARepo = ressourceBookingJPARepo;
    }


    @Override
    public Ressource addRessource(Ressource ressource) throws ExecutionException, InterruptedException {
/*
        if(ressource == null) {
            throw new IllegalArgumentException("Ressource darf nicht null sein");
        }

        if (!RessourceIsOccupied(ressource, deskBooking.getDate(), deskBooking.getStart(), deskBooking.getEndTime())) {
            throw new DeskNotAvailableException("Desk not available for booking period");
        }

*/
        return this.ressourceBookingJPARepo.save(ressource); //.save added und updatet.
    }

    @Override
    public List<Ressource> getAllRessource() throws ExecutionException, InterruptedException {
        return this.ressourceBookingJPARepo.findAll();

    }

    @Override
    public Ressource getRessourceById(Long id) throws RessourceNotFoundException, ExecutionException, InterruptedException {
        Optional<Ressource> ressourceOptional = this.ressourceBookingJPARepo.findById(id);
        if (ressourceOptional.isPresent()) {
            return ressourceOptional.get();
        } else {
            throw new RessourceNotFoundException();
        }
    }

    @Override
    public Ressource updateRessource(Ressource ressource) throws RessourceNotFoundException, ExecutionException, InterruptedException {
        return this.ressourceBookingJPARepo.save(ressource);
    }

    @Override
    public void deleteRessourceById(Long id) throws RessourceDeletionNotPossibleException {
        this.ressourceBookingJPARepo.deleteById(id);
    }

    @Override
    public Ressource getRessourceBySerialnumber(String serialnumber) throws RessourceNotFoundException, ExecutionException, InterruptedException {
        return null;
    }
}
