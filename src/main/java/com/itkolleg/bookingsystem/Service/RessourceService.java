package com.itkolleg.bookingsystem.Service;

import com.itkolleg.bookingsystem.domains.Ressource;

import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceNotFoundException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RessourceService {
    public Ressource addRessource(Ressource ressource) throws RessourceAlreadyExistsException, ExecutionException, InterruptedException;
    public List<Ressource> getAllRessource();
    public Ressource getRessourceById(Long id) throws RessourceNotFoundException;
    public Ressource updateRessourceById(Long id) throws RessourceNotFoundException;
    public void deleteRessourceById(Long id) throws RessourceDeletionNotPossibleException;
    public Ressource findRessourceBySerialnumber (String Serialnumber) throws RessourceNotFoundException; //Must Equal


    //public void deleteRessourceByRessourceType(Ressourcetype ressourcetype) throws RessourceDeletionNotPossibleException;
    //public Ressource updateRessourceByRessourceType(Ressourcetype ressourcetype) throws RessourceNotFoundException;
}
