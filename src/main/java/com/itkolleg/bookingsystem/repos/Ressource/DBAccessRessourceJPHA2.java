package com.itkolleg.bookingsystem.repos.Ressource;

import com.itkolleg.bookingsystem.domains.Booking.RessourceBooking;
import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceNotFoundException;
import com.itkolleg.bookingsystem.repos.RessourceBooking.RessourceBookingRepo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Die gegebene Klasse DBAccessRessourceJPHA2 ist eine Implementierung des Interfaces DBAccessRessource. Sie dient als Datenbankzugriffsschicht für die Ressourcen in einem Buchungssystem.
 *
 * @author Manuel Payer
 * @version 1.0
 * @since 28.06.2023
 */
@Component
public class DBAccessRessourceJPHA2 implements DBAccessRessource {

    private final RessourceJPARepo ressourceJPARepo;
    private final RessourceBookingRepo ressourceBookingRepo;

    /**
     * Konstruktor der Klasse DBAccessRessourceJPAH2. Er benötigt folgende Parameter:
     *
     * @param ressourceJPARepo     vom Typ RessourceJPARepo
     * @param ressourceBookingRepo vom Typ RessourceBookingRepo
     */
    public DBAccessRessourceJPHA2(RessourceJPARepo ressourceJPARepo, RessourceBookingRepo ressourceBookingRepo) {
        this.ressourceJPARepo = ressourceJPARepo;
        this.ressourceBookingRepo = ressourceBookingRepo;
    }

    /**
     * Diese Methode fügt dem ressourceJPARepo eine Ressource hinzu. Es werden folgenden Parameter benötigt:
     *
     * @param ressource vom Typ Ressource
     * @return eine Neue Ressource
     */
    @Override
    public Ressource addRessource(Ressource ressource) {

        return this.ressourceJPARepo.save(ressource); //.save added und updatet.
    }

    /**
     * Diese Methode gibt eine Liste von Ressourcen zurück.
     *
     * @return this.ressourceJPARepo
     */
    @Override
    public List<Ressource> getAllRessource() {
        return this.ressourceJPARepo.findAll();

    }

    /**
     * Diese Methode gibt eine Ressource anhand der mitgegeben ID zurück. Sie benötigt folgende Parameter:
     *
     * @param id vom Typ Long
     * @return Liste als Optional von Ressourcen
     * @throws RessourceNotFoundException Ressource nicht gefunden
     */
    @Override
    public Ressource getRessourceById(Long id) throws RessourceNotFoundException {
        Optional<Ressource> ressourceOptional = this.ressourceJPARepo.findById(id);
        if (ressourceOptional.isPresent()) {
            return ressourceOptional.get();
        } else {
            throw new RessourceNotFoundException();
        }
    }

    /**
     * Diese Methode updated die als Parameter übergebene Ressource.
     *
     * @param ressource vom Typ Ressource
     * @return Speicheraufruf
     */
    @Override
    public Ressource updateRessource(Ressource ressource) {
        return this.ressourceJPARepo.save(ressource);
    }

    /**
     * Diese Methode löscht eine Ressource anhand der mitgelieferten ID.
     *
     * @param id vom Typ Long
     * @throws RessourceDeletionNotPossibleException Ressource konnte nicht gelöscht werden.
     */
    @Override
    public void deleteRessourceById(Long id) throws RessourceDeletionNotPossibleException {
        List<RessourceBooking> bookings = this.ressourceBookingRepo.getBookingsByRessourceId(id);

        if (!bookings.isEmpty()) {
            throw new RessourceDeletionNotPossibleException("Ressource already booked");
        }
        this.ressourceJPARepo.deleteById(id);
    }

    @Override
    public Ressource getRessourceBySerialnumber(String serialnumber) {
        return null;
    }
}
