package com.itkolleg.bookingsystem.repos;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class DBAccessRoomFirebaseImpl implements DBAccessRoom {

    private Firestore dbFirestore;

    public DBAccessRoomFirebaseImpl(){
        this.dbFirestore = FirestoreClient.getFirestore();
    }

    @Override
    public Room addRoom(Room room) throws ExecutionException, InterruptedException {
        // Hole eine Referenz auf die "rooms"-Sammlung
        CollectionReference collectionReference = dbFirestore.collection("rooms");

        // Hole das Dokument mit der höchsten ID in der "rooms"-Sammlung
        ApiFuture<QuerySnapshot> future = collectionReference.orderBy("id", Query.Direction.DESCENDING).limit(1).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        Long nextId = 1L; // Standard-ID für den Fall, dass die Sammlung leer ist

        // Wenn es bereits Dokumente in der Sammlung gibt, generiere die nächste ID basierend auf der höchsten vorhandenen ID
        if (!documents.isEmpty()) {
            nextId = documents.get(0).toObject(Room.class).getId() + 1L;
        }

        room.setId(nextId); // Setze die generierte ID für das neue Room-Objekt

        // Füge das Room-Objekt als neues Dokument in die "room"-Sammlung ein
        ApiFuture<WriteResult> collectionsApiFuture = collectionReference.document(String.valueOf(room.getId())).set(room);

        return room;
    }


    @Override
    public List<Room> getAllRooms() throws ExecutionException, InterruptedException {
        // Hole alle Dokumente aus der "rooms"-Sammlung
        ApiFuture<QuerySnapshot> future = dbFirestore.collection("rooms").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        // Konvertiere die Dokumente in eine Liste von Room-Objekten
        List<Room> rooms = new ArrayList<>();
        for(QueryDocumentSnapshot document : documents){
            Room room = document.toObject(Room.class);
            rooms.add(room);
        }
        return rooms;
    }


    @Override
    public Room getRoomById(Long id) throws RoomNotFoundException, ExecutionException, InterruptedException {
        // Hole eine Referenz auf das Dokument mit der angegebenen ID
        DocumentReference documentReference = dbFirestore.collection("rooms").document(String.valueOf(id));
        // Hole das Dokument als DocumentSnapshot-Objekt
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();

        // Überprüfe, ob das Dokument vorhanden ist
        Room room;
        if(document.exists()){
            // Konvertiere das DocumentSnapshot-Objekt in ein CRUD-Objekt
            room = document.toObject(Room.class);
            return room;
        }
        return null;
    }

    @Override
    public Room updateRoomById(Room updatedRoom) throws RoomNotFoundException, ExecutionException, InterruptedException {
        // Hole eine Referenz auf das Dokument mit der angegebenen ID
        DocumentReference documentReference = dbFirestore.collection("rooms").document(String.valueOf(updatedRoom.getId()));

        // Führe das Update auf dem Dokument durch
        ApiFuture<WriteResult> updateResult = documentReference.set(updatedRoom);

        // Warte, bis das Update abgeschlossen ist
        updateResult.get();

        // Lese das aktualisierte Dokument aus der Datenbank und gib es zurück
        return getRoomById(updatedRoom.getId());
    }

    @Override
    public void deleteRoomById(Long id) throws RoomDeletionNotPossibleException {
        // Hole eine Referenz auf das Dokument mit der angegebenen ID
        DocumentReference documentReference = dbFirestore.collection("rooms").document(String.valueOf(id));
        // Hole das Dokument als DocumentSnapshot-Objekt
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                // Lösche das Dokument aus der Datenbank
                documentReference.delete();
            }
        } catch (InterruptedException | ExecutionException e) {
            // Wirft eine mögliche exception, die beim Versuch zum Löschen des Dokuments auftreten
            throw new RuntimeException("Fehler beim Löschen des Raums mit der ID " + id, e);
        }
    }
}
