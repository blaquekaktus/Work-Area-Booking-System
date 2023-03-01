package com.itkolleg.bookingsystem.repos;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.itkolleg.bookingsystem.domains.Employee;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


@Component
public class DBAccessEmployeeFirebaseImpl implements DBAccessEmployees{

    private Firestore dbFirestore;

    //Erstelle eine Firestore-Instanz
    public DBAccessEmployeeFirebaseImpl(){
        this.dbFirestore = FirestoreClient.getFirestore();
    }

    @Override
    public Employee addEmployee(Employee employee) throws ExecutionException, InterruptedException, EmployeeAlreadyExistsException {
        // Hole eine Referenz auf die "employees"-Sammlung
        CollectionReference collectionReference = dbFirestore.collection("employees");

        // Hole das Dokument mit der höchsten ID in der "employees"-Sammlung
        ApiFuture<QuerySnapshot> future = collectionReference.orderBy("id", Query.Direction.DESCENDING).limit(1).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        Long nextId = 1L; // Standard-ID für den Fall, dass die Sammlung leer ist

        // Wenn es bereits Dokumente in der Sammlung gibt, generiere die nächste ID basierend auf der höchsten vorhandenen ID
        if (!documents.isEmpty()) {
            nextId = documents.get(0).toObject(Employee.class).getId() + 1L;
        }

        employee.setId(nextId); // Setze die generierte ID für das neue Employee-Objekt

        // Füge das Employee-Objekt als neues Dokument in die "employees"-Sammlung ein
        ApiFuture<WriteResult> collectionsApiFuture = collectionReference.document(String.valueOf(employee.getId())).set(employee);

        return employee;
    }



    @Override
    public List<Employee> getAllEmployees() throws ExecutionException, InterruptedException {
        // Hole alle Dokumente aus der "employees"-Sammlung
        ApiFuture<QuerySnapshot> future = dbFirestore.collection("employees").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        // Konvertiere die Dokumente in eine Liste von Employee-Objekten
        List<Employee> employees = new ArrayList<>();
        for(QueryDocumentSnapshot document : documents){
            Employee employee = document.toObject(Employee.class);
            employees.add(employee);
        }
        return employees;
    }

    @Override
    public Employee getEmployeeById(Long id) throws EmployeeNotFoundException, ExecutionException, InterruptedException {
        // Hole eine Referenz auf das Dokument mit der angegebenen ID
        DocumentReference documentReference = dbFirestore.collection("employees").document(String.valueOf(id));
        // Hole das Dokument als DocumentSnapshot-Objekt
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();

        // Überprüfe, ob das Dokument vorhanden ist
        Employee employee;
        if(document.exists()){
            // Konvertiere das DocumentSnapshot-Objekt in ein CRUD-Objekt
            employee = document.toObject(Employee.class);
            return employee;
        }
        return null;
    }

    @Override
    public Employee updateEmployeeById(Employee updatedEmployee) throws EmployeeNotFoundException, ExecutionException, InterruptedException {
        // Hole eine Referenz auf das Dokument mit der angegebenen ID
        DocumentReference documentReference = dbFirestore.collection("employees").document(String.valueOf(updatedEmployee.getId()));

        // Führe das Update auf dem Dokument durch
        ApiFuture<WriteResult> updateResult = documentReference.set(updatedEmployee);

        // Warte, bis das Update abgeschlossen ist
        updateResult.get();

        // Lese das aktualisierte Dokument aus der Datenbank und gib es zurück
        return getEmployeeById(updatedEmployee.getId());

    }


    @Override
    public void deleteEmployeeById(Long id) {
        // Hole eine Referenz auf das Dokument mit der angegebenen ID
        DocumentReference documentReference = dbFirestore.collection("employees").document(String.valueOf(id));
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
            throw new RuntimeException("Fehler beim Löschen des Mitarbeiters mit der ID " + id, e);
        }
    }

}
