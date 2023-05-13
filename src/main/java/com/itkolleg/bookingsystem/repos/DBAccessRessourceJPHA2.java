package com.itkolleg.bookingsystem.repos;

import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class DBAccessRessourceJPHA2 implements DBAccessRessource{
    @Override
    public Ressource addRessource(Ressource ressource) throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public List<Ressource> getAllRessource() throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public Ressource getRessourceById(Long id) throws RessourceNotFoundException, ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public Ressource updateRessourceById(Ressource updatedRessource) throws RessourceNotFoundException, ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public void deleteRessourceById(Long id) throws RessourceDeletionNotPossibleException {

    }

    @Override
    public Ressource getRessourceBySerialnumber(String serialnumber) throws RessourceNotFoundException, ExecutionException, InterruptedException {
        return null;
    }
}
