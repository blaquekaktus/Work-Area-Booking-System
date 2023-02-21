package com.itkolleg.bookingsystem.repos;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public List<Desk> getAllDesk() {
        return null;
    }

    @Override
    public Desk getDeskById(Long id) throws DeskNotFoundException {
        return null;
    }

    @Override
    public Desk updateDeskById(Long id) throws DeskNotFoundException {
        return null;
    }

    @Override
    public void deleteDeskById(Long id) throws DeskDeletionNotPossibleException {

    }
}
