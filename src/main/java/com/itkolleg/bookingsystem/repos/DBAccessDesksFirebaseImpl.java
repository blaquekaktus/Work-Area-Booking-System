package com.itkolleg.bookingsystem.repos;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class DBAccessDesksFirebaseImpl implements  DBAccessDesks{


    private Firestore dbFirestore;


    //Erstelle eine Firestore-Instanz
    public DBAccessDesksFirebaseImpl(){
        this.dbFirestore = FirestoreClient.getFirestore();
    }

    @Override
    public Desk addDesk(Desk desk) {
        return null;
    }



    @Override
    public List<Desk> getAllDesk() throws ExecutionException, InterruptedException {
        // Hole alle Dokumente aus der "desks"-Sammlung
        ApiFuture<QuerySnapshot> future = dbFirestore.collection("desks").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        // Konvertiere die Dokumente in eine Liste von Desk-Objekten
        List<Desk> desks = new ArrayList<>();
        for(QueryDocumentSnapshot document : documents){
            Desk desk = document.toObject(Desk.class);
            desks.add(desk);
        }
        return desks;
    }

    @Override
    public Desk getDeskById(Long id) throws DeskNotFoundException {
        return null;
    }

    @Override
    public Desk updateDeskById(Desk desk) throws DeskNotFoundException {
        return null;
    }

    @Override
    public void deleteDeskById(Long id) throws DeskDeletionNotPossibleException {

    }
}
