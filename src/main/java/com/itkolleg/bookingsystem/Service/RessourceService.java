package com.itkolleg.bookingsystem.Service;

import com.itkolleg.bookingsystem.domains.Ressource;

import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
@Service
public interface RessourceService {
    public Ressource addRessource(Ressource ressource) throws ExecutionException, InterruptedException;
    public List<Ressource> getAllRessource() throws ExecutionException, InterruptedException;
    public Ressource getRessourceById(Long id) throws RessourceNotFoundException, ExecutionException, InterruptedException;
    public Ressource updateRessourceById(Ressource ressource) throws RessourceNotFoundException, ExecutionException, InterruptedException;
    public void deleteRessourceById(Long id) throws RessourceDeletionNotPossibleException;
    public Ressource findRessourceBySerialnumber (String Serialnumber) throws RessourceNotFoundException; //Must Equal


    //public void deleteRessourceByRessourceType(Ressourcetype ressourcetype) throws RessourceDeletionNotPossibleException;
    //public Ressource updateRessourceByRessourceType(Ressourcetype ressourcetype) throws RessourceNotFoundException;
}
