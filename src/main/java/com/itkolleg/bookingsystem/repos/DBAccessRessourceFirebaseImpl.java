package com.itkolleg.bookingsystem.repos;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.annotation.Exclude;
import com.google.firebase.cloud.FirestoreClient;
import com.google.protobuf.StringValue;
import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
@Component
public class DBAccessRessourceFirebaseImpl implements  DBAccessRessource {


    private Firestore dbFirestore;


    //Erstelle eine Firestore-Instanz
    public DBAccessRessourceFirebaseImpl() {
        this.dbFirestore = FirestoreClient.getFirestore();
    }

    @Override
    public Ressource addRessource(Ressource ressource) throws ExecutionException, InterruptedException {
        CollectionReference collectionReference = dbFirestore.collection("ressources");
        ApiFuture<QuerySnapshot> future = collectionReference.orderBy("id", Query.Direction.DESCENDING).limit(1).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        Long nextId = 1L;


        if (!documents.isEmpty()) {
            nextId = documents.get(0).toObject(Ressource.class).getId() + 1L;
        }

        ressource.setId(nextId);

        ApiFuture<WriteResult> collectionsApiFuture = collectionReference.document(String.valueOf(ressource.getId())).set(ressource);
        return ressource;
    }

    @Override
    @Exclude
    public List<Ressource> getAllRessource() throws ExecutionException, InterruptedException {

        ApiFuture<QuerySnapshot> future = dbFirestore.collection("ressources").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Ressource> ressources = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Ressource ressource = document.toObject(Ressource.class);
            ressources.add(ressource);
        }
        return ressources;

    }

    @Override
    public Ressource getRessourceById(Long id) throws RessourceNotFoundException, ExecutionException, InterruptedException {
        DocumentReference documentReference = dbFirestore.collection("ressources").document(String.valueOf(id));
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();

        Ressource ressource;
        if (document.exists()) {
            ressource = document.toObject(Ressource.class);
            return ressource;
        }
        return null;
    }


    @Override
    public Ressource updateRessourceById(Ressource updatedRessource) throws RessourceNotFoundException, ExecutionException, InterruptedException {
        DocumentReference documentReference = dbFirestore.collection("ressources").document(String.valueOf(updatedRessource.getId()));
        ApiFuture<WriteResult> updateResult = documentReference.set(updatedRessource);
        updateResult.get();

        return getRessourceById(updatedRessource.getId());
    }

    @Override
    public void deleteRessourceById(Long id) throws RessourceDeletionNotPossibleException {
        DocumentReference documentReference = dbFirestore.collection("ressources").document(String.valueOf(id));
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                documentReference.delete();
            }


        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Fehler beim Löschen der Ressource mit der ID " + id, e);
        }

    }

    @Override
    public Ressource getRessourceBySerialnumber(String serialnumber) throws RessourceNotFoundException, ExecutionException, InterruptedException {
        DocumentReference documentReference = dbFirestore.collection("ressources").document(serialnumber);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();

        Ressource ressource;
        if (document.exists()) {
            ressource = document.toObject(Ressource.class);
            return ressource;
        }
        return null;
    }


}