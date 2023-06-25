package com.itkolleg.bookingsystem.repos.Ressource;


import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceNotFoundException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface DBAccessRessource {
    Ressource addRessource(Ressource ressource) throws ExecutionException, InterruptedException;

    List<Ressource> getAllRessource() throws ExecutionException, InterruptedException;

    Ressource getRessourceById(Long id) throws RessourceNotFoundException, ExecutionException, InterruptedException;

    Ressource updateRessourceById(Ressource updatedRessource) throws RessourceNotFoundException, ExecutionException, InterruptedException;

    void deleteRessourceById(Long id) throws RessourceDeletionNotPossibleException;

    Ressource getRessourceBySerialnumber(String serialnumber) throws RessourceNotFoundException, ExecutionException, InterruptedException;
}
