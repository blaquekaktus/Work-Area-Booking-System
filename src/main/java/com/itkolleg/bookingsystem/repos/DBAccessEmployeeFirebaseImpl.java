package com.itkolleg.bookingsystem.repos;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.itkolleg.bookingsystem.domains.Employee;
import org.springframework.stereotype.Component;

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
        // Hole eine Referenz auf das Dokument mit der angegebenen ID
        DocumentReference documentReference = dbFirestore.collection("employees").document(String.valueOf(employee.getId()));
        // Hole das Dokument als DocumentSnapshot-Objekt
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();

        // Überprüfe, ob das Dokument bereits existiert
        if (document.exists()) {
            // Wenn das Dokument bereits vorhanden ist, werfe eine Exception oder handle den Fall entsprechend
            throw new EmployeeAlreadyExistsException("Das Dokument mit der angegebenen ID existiert bereits!");
        } else {
            // Füge das Employee-Objekt als neues Dokument in die "employees"-Sammlung ein
            ApiFuture<WriteResult> collectionsApiFuture = documentReference.set(employee);
            return employee;
        }
    }


    @Override
    public List<Employee> getAllEmployees() throws ExecutionException, InterruptedException {
        // Hole alle Dokumente aus der "crud_user"-Sammlung
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
            throw new RuntimeException("Error deleting employee with ID " + id, e);
        }
    }

}
