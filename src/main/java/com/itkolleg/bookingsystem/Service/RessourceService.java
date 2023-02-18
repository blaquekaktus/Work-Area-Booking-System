package com.itkolleg.bookingsystem.Service;

import com.itkolleg.bookingsystem.domains.Ressource;

import com.itkolleg.bookingsystem.domains.Ressourcetype;
import com.itkolleg.bookingsystem.exceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RessourceNotFoundException;

import java.util.List;

public interface RessourceService {
    public Ressource addRessource(Ressource ressource);
    public List<Ressource> getAllRessource();
    public Ressource getRessourceById(Long id) throws RessourceNotFoundException;
    public Ressource getRessourceByRessourceType(Ressourcetype ressourcetype) throws RessourceNotFoundException;
    public Ressource updateRessourceById(Long id) throws RessourceNotFoundException;
    public Ressource updateRessourceByRessourceType(Ressourcetype ressourcetype) throws RessourceNotFoundException;
    public void deleteRessourceById(Long id) throws RessourceDeletionNotPossibleException;
    public void deleteRessourceByRessourceType(Ressourcetype ressourcetype) throws RessourceDeletionNotPossibleException;
}
