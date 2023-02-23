package com.itkolleg.bookingsystem.repos;


import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceNotFoundException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface DBAccessRessource {
    public Ressource addRessource(Ressource ressource) throws ExecutionException, InterruptedException, RessourceAlreadyExistsException;
    public List<Ressource> getAllRessource();
    public Ressource getRessourceById(Long id) throws RessourceNotFoundException;
    public Ressource updateRessourceById(Long id) throws RessourceNotFoundException;
    public void deleteRessourceById(Long id) throws RessourceDeletionNotPossibleException;
    public Ressource getRessourceBySerialnumber (String serialnumber) throws RessourceNotFoundException;
}
