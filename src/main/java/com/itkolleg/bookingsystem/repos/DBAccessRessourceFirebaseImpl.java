package com.itkolleg.bookingsystem.repos;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class DBAccessRessourceFirebaseImpl implements  DBAccessRessource{


    private Firestore dbFirestore;


    //Erstelle eine Firestore-Instanz
    public DBAccessRessourceFirebaseImpl(){
        this.dbFirestore = FirestoreClient.getFirestore();
    }

    @Override
    public Ressource addRessource(Ressource ressource) throws ExecutionException, InterruptedException, RessourceAlreadyExistsException {
        // Hole eine Referenz auf das Dokument mit der angegebenen ID
        DocumentReference documentReference = dbFirestore.collection("ressources").document(String.valueOf(ressource.getId()));
        // Hole das Dokument als DocumentSnapshot-Objekt
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();

        // Überprüfe, ob das Dokument bereits existiert
        if (document.exists()) {
            // Wenn das Dokument bereits vorhanden ist, werfe eine Exception oder handle den Fall entsprechend
            throw new RessourceAlreadyExistsException("Das Dokument mit der angegebenen ID existiert bereits!");
        } else {
            // Füge das Employee-Objekt als neues Dokument in die "employees"-Sammlung ein
            ApiFuture<WriteResult> collectionsApiFuture = documentReference.set(ressource);
            return ressource;
        }
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