package com.itkolleg.bookingsystem.repos;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class DBAccessDesksFirebaseImpl implements DBAccessDesks {


    private Firestore dbFirestore;


    //Erstelle eine Firestore-Instanz
    public DBAccessDesksFirebaseImpl() {
        this.dbFirestore = FirestoreClient.getFirestore();
    }

    @Override
    public Desk addDesk(Desk desk) throws ExecutionException, InterruptedException {
        // Hole eine Referenz auf die "desks"-Sammlung
        CollectionReference collectionReference = dbFirestore.collection("desks");

        // Hole das Dokument mit der höchsten ID in der "desks"-Sammlung
        ApiFuture<QuerySnapshot> future = collectionReference.orderBy("id", Query.Direction.DESCENDING).limit(1).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        Long nextId = 1L; // Standard-ID für den Fall, dass die Sammlung leer ist

        // Wenn es bereits Dokumente in der Sammlung gibt, generiere die nächste ID basierend auf der höchsten vorhandenen ID
        if (!documents.isEmpty()) {
            nextId = documents.get(0).toObject(Desk.class).getId() + 1L;
        }

        desk.setId(nextId); // Setze die generierte ID für das neue Employee-Objekt

        // Füge das Employee-Objekt als neues Dokument in die "employees"-Sammlung ein
        ApiFuture<WriteResult> collectionsApiFuture = collectionReference.document(String.valueOf(desk.getId())).set(desk);

        return desk;
    }


    @Override
    public List<Desk> getAllDesk() throws ExecutionException, InterruptedException {
        // Hole alle Dokumente aus der "desks"-Sammlung
        ApiFuture<QuerySnapshot> future = dbFirestore.collection("desks").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        // Konvertiere die Dokumente in eine Liste von Desk-Objekten
        List<Desk> desks = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Desk desk = document.toObject(Desk.class);
            desks.add(desk);
        }
        return desks;
    }

    @Override
    public Desk getDeskById(Long id) throws DeskNotFoundException, ExecutionException, InterruptedException {
        // Hole eine Referenz auf das Dokument mit der angegebenen ID
        DocumentReference documentReference = dbFirestore.collection("desks").document(String.valueOf(id));
        // Hole das Dokument als DocumentSnapshot-Objekt
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();

        // Überprüfe, ob das Dokument vorhanden ist
        Desk desk;
        if (document.exists()) {
            // Konvertiere das DocumentSnapshot-Objekt in ein CRUD-Objekt
            desk = document.toObject(Desk.class);
            return desk;
        }
        return null;
    }

    @Override
    public Desk updateDeskById(Desk updatedDesk) throws DeskNotFoundException, ExecutionException, InterruptedException {
        // Hole eine Referenz auf das Dokument mit der angegebenen ID
        DocumentReference documentReference = dbFirestore.collection("desks").document(String.valueOf(updatedDesk.getId()));

        // Führe das Update auf dem Dokument durch
        ApiFuture<WriteResult> updateResult = documentReference.set(updatedDesk);

        // Warte, bis das Update abgeschlossen ist
        updateResult.get();

        // Lese das aktualisierte Dokument aus der Datenbank und gib es zurück
        return getDeskById(updatedDesk.getId());

    }

    @Override
    public void deleteDeskById(Long id) throws DeskDeletionNotPossibleException {
        // Hole eine Referenz auf das Dokument mit der angegebenen ID
        DocumentReference documentReference = dbFirestore.collection("desks").document(String.valueOf(id));
        // Hole das Dokument als DocumentSnapshot-Objekt
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                // Lösche das Dokument aus der Datenbank
                documentReference.delete();
            }
        } catch (InterruptedException | ExecutionException e) {
            // Rethrow any exceptions that occur while attempting to delete the document
            throw new RuntimeException("Fehler beim Löschen des Tisches mit der ID " + id, e);
        }
    }
}
