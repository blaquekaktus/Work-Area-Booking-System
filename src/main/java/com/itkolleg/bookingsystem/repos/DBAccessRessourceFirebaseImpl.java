package com.itkolleg.bookingsystem.repos;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RessourceNotFoundException;

import java.util.List;

public class DBAccessRessourceFirebaseImpl implements  DBAccessRessource{


    private Firestore dbFirestore;


    //Erstelle eine Firestore-Instanz
    public DBAccessRessourceFirebaseImpl(){
        this.dbFirestore = FirestoreClient.getFirestore();
    }

    @Override
    public Ressource addRessource(Ressource ressource) {
        return null;
    }

    @Override
    public List<Ressource> getAllRessource() {
        return null;
    }

    @Override
    public Ressource getRessourceById(Long id) throws RessourceNotFoundException {
        return null;
    }

    @Override
    public Ressource updateRessourceById(Long id) throws RessourceNotFoundException {
        return null;
    }

    @Override
    public void deleteRessourceById(Long id) throws RessourceDeletionNotPossibleException {

    }

    @Override
    public Ressource getRessourceBySerialnumber(String serialnumber) throws RessourceNotFoundException {
        return null;
    }


}