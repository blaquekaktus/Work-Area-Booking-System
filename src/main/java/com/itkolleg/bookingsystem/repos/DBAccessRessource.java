package com.itkolleg.bookingsystem.repos;


import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RessourceNotFoundException;

import java.util.List;

public interface DBAccessRessource {
    public Ressource addRessource(Ressource ressource);
    public List<Ressource> getAllRessource();
    public Ressource getRessourceById(Long id) throws RessourceNotFoundException;
    public Ressource updateRessourceById(Long id) throws RessourceNotFoundException;
    public void deleteRessourceById(Long id) throws RessourceDeletionNotPossibleException;
}
