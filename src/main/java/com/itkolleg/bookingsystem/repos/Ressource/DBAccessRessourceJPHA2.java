package com.itkolleg.bookingsystem.repos.Ressource;

import com.itkolleg.bookingsystem.domains.Booking.RessourceBooking;
import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.DeskNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceNotFoundException;
import com.itkolleg.bookingsystem.repos.RessourceBooking.RessourceBookingRepo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class DBAccessRessourceJPHA2 implements DBAccessRessource {

    private final RessourceJPARepo ressourceJPARepo;
    private final RessourceBookingRepo ressourceBookingRepo;

    /**
     * Konstruktor
     * @param ressourceJPARepo
     */
    public DBAccessRessourceJPHA2(RessourceJPARepo ressourceJPARepo, RessourceBookingRepo ressourceBookingRepo) {
        this.ressourceJPARepo = ressourceJPARepo;
        this.ressourceBookingRepo = ressourceBookingRepo;
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
        return this.ressourceJPARepo.save(ressource); //.save added und updatet.
    }

    @Override
    public List<Ressource> getAllRessource() throws ExecutionException, InterruptedException {
        return this.ressourceJPARepo.findAll();

    }

    @Override
    public Ressource getRessourceById(Long id) throws RessourceNotFoundException, ExecutionException, InterruptedException {
        Optional<Ressource> ressourceOptional = this.ressourceJPARepo.findById(id);
        if (ressourceOptional.isPresent()) {
            return ressourceOptional.get();
        } else {
            throw new RessourceNotFoundException();
        }
    }

    @Override
    public Ressource updateRessource(Ressource ressource) throws RessourceNotFoundException, ExecutionException, InterruptedException {
        return this.ressourceJPARepo.save(ressource);
    }

    @Override
    public void deleteRessourceById(Long id) throws RessourceDeletionNotPossibleException {
        List<RessourceBooking> bookings = this.ressourceBookingRepo.getBookingsByRessourceId(id);

        if (!bookings.isEmpty()) {
            throw new RessourceDeletionNotPossibleException("Ressource already booked");
        }
        this.ressourceJPARepo.deleteById(id);
    }

    @Override
    public Ressource getRessourceBySerialnumber(String serialnumber) throws RessourceNotFoundException, ExecutionException, InterruptedException {
        return null;
    }
}
