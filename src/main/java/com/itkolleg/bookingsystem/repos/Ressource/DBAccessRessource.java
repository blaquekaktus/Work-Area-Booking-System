package com.itkolleg.bookingsystem.repos.Ressource;


import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceNotFoundException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface DBAccessRessource {
    public Ressource addRessource(Ressource ressource) throws ExecutionException, InterruptedException;
    public List<Ressource> getAllRessource() throws ExecutionException, InterruptedException;
    public Ressource getRessourceById(Long id) throws RessourceNotFoundException, ExecutionException, InterruptedException;
    public Ressource updateRessourceById(Ressource updatedRessource) throws RessourceNotFoundException, ExecutionException, InterruptedException;
    public void deleteRessourceById(Long id) throws RessourceDeletionNotPossibleException;
    public Ressource getRessourceBySerialnumber (String serialnumber) throws RessourceNotFoundException, ExecutionException, InterruptedException;
}
