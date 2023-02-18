package com.itkolleg.bookingsystem.Service;

import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.domains.Ressourcetype;
import com.itkolleg.bookingsystem.exceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RessourceNotFoundException;
import com.itkolleg.bookingsystem.repos.DBAccessRessource;

import java.util.List;

public class RessourceServiceImplementation implements RessourceService{

    private DBAccessRessource dbAccessRessource;

    public RessourceServiceImplementation(DBAccessRessource dbAccessRessource){
        this.dbAccessRessource=dbAccessRessource;
    }

    @Override
    public Ressource addRessource(Ressource ressource) {
        return this.dbAccessRessource.addRessource(ressource);
    }

    /**
     * @return
     */
    @Override
    public List<Ressource> getAllRessource() {
        return this.dbAccessRessource.getAllRessource();
    }


    @Override
    public Ressource getRessourceById(Long id) throws RessourceNotFoundException {
        return this.dbAccessRessource.getRessourceById(id);
    }

    @Override
    public Ressource getRessourceByRessourceType(Ressourcetype ressourcetype) throws RessourceNotFoundException {
        return null;
    }


    @Override
    public Ressource updateRessourceById(Long id) throws RessourceNotFoundException {
        return this.dbAccessRessource.updateRessourceById(id);
    }

    @Override
    public Ressource updateRessourceByRessourceType(Ressourcetype ressourcetype) throws RessourceNotFoundException {
        return null;
    }


    @Override
    public void deleteRessourceById(Long id) throws RessourceDeletionNotPossibleException {
        this.dbAccessRessource.deleteRessourceById(id);
    }

    @Override
    public void deleteRessourceByRessourceType(Ressourcetype ressourcetype) throws RessourceDeletionNotPossibleException {

    }
}
