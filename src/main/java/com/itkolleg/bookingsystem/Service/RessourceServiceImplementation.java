package com.itkolleg.bookingsystem.Service;

import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceNotFoundException;
import com.itkolleg.bookingsystem.repos.DBAccessRessource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class RessourceServiceImplementation implements RessourceService{

    private DBAccessRessource dbAccessRessource;

    /**
     * Sets the DBAccess Object into the global Datafield
     * @param dbAccessRessource
     */
    public RessourceServiceImplementation(DBAccessRessource dbAccessRessource){
        this.dbAccessRessource=dbAccessRessource;
    }

    /**
     * Method, that adds a ressource
     * @param ressource
     * @return ressource
     */
    @Override
    public Ressource addRessource(Ressource ressource) throws ExecutionException, InterruptedException {
        return this.dbAccessRessource.addRessource(ressource);
    }

    /**
     * Method, that returns a list of all ressources
     * @return list of ressources
     */
    @Override
    public List<Ressource> getAllRessource() throws ExecutionException, InterruptedException {
        return this.dbAccessRessource.getAllRessource();
    }

    /**
     * Method, that returns a ressource by its ID
     * @param id Long
     * @return Reccource by ID
     * @throws RessourceNotFoundException
     */
    @Override
    public Ressource getRessourceById(Long id) throws RessourceNotFoundException, ExecutionException, InterruptedException {
        return this.dbAccessRessource.getRessourceById(id);
    }

    /**
     * Method, that updates a specific ressource found by its ID
     * @return updated ressource by ID
     * @throws RessourceNotFoundException
     */
    @Override
    public Ressource updateRessourceById(Ressource ressource) throws RessourceNotFoundException, ExecutionException, InterruptedException {
        return this.dbAccessRessource.updateRessourceById(ressource);
    }

    /**
     *Method, that deletes a ressource by its ID
     * @param id long
     * @throws RessourceDeletionNotPossibleException
     */
    @Override
    public void deleteRessourceById(Long id) throws RessourceDeletionNotPossibleException {
        this.dbAccessRessource.deleteRessourceById(id);
    }

    /**
     * Method, that returns the Ressource according to its Serialnubmer (String)
     * @param Serialnumber String
     * @return Ressource found by Serialnumber
     * @throws RessourceNotFoundException
     */
    @Override
    public Ressource findRessourceBySerialnumber(String Serialnumber) throws RessourceNotFoundException, ExecutionException, InterruptedException {
        return this.dbAccessRessource.getRessourceBySerialnumber(Serialnumber);
    }

    //TO DO: Code durchlesen und anpassen.
    //Firebase Tabel erstellen für Ressourcen
}
