package com.itkolleg.bookingsystem.controller.desk;

import com.itkolleg.bookingsystem.Service.DeskService;
import com.itkolleg.bookingsystem.Service.EmployeeService;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.*;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskNotFoundException;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskValidationException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeValidationException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomValidationException;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/desks")
public class DeskRestController {
    private DeskService deskService;


    /**
     * Erstellt einen neuen DeskRestController, der mit dem angegebenen DeskService arbeitet.
     *
     * @param deskService Der DeskService, der von diesem DeskRestController verwendet werden soll.
     */
    public DeskRestController(DeskService deskService) {
        this.deskService = deskService;
    }

    /**
     * Diese Methode handhabt einen HTTP-POST-Request, der darauf abzielt, einen neuen Schreibtisch (Desk)
     * zur Datenbank hinzuzufügen.
     *
     * @param desk Ein Desk-Objekt, das die Informationen des hinzuzufügenden Schreibtisches enthält.
     * @param bindingResult Ein BindingResult-Objekt, das etwaige Validierungsfehler enthält.
     * @return Ein ResponseEntity-Objekt, das den neu erstellten Schreibtisch und den HTTP-Statuscode enthält.
     * @throws DeskValidationException Wenn Validierungsfehler bei der Überprüfung des Schreibtisches auftreten.
     * @throws ExecutionException Wenn die Ausführung einer asynchronen Aufgabe fehlschlägt.
     * @throws InterruptedException Wenn der Thread der Methode unterbrochen wird.
     */
    @PostMapping
    public ResponseEntity<Desk> addDesk(@Valid @RequestBody Desk desk, BindingResult bindingResult) throws DeskValidationException, ExecutionException, InterruptedException {
        // Erstelle ein neues FormValidationExceptionDTO-Objekt, das später dazu verwendet wird,
        // etwaige Validierungsfehler der übergebenen Desk-Daten zu speichern.
        FormValidationExceptionDTO formValidationErrors = new FormValidationExceptionDTO("9000");

        // Prüfe, ob es Validierungsfehler in den übergebenen Desk-Daten gibt. Falls ja, iteriere durch alle
        // Fehler und füge sie dem formValidationErrors-Objekt hinzu.
        if(bindingResult.hasErrors()){
            for(ObjectError error : bindingResult.getAllErrors()){
                formValidationErrors.addFormValidationError(((FieldError) error).getField(), error.getDefaultMessage());
            }
            // Wirf eine Exception, die die Validierungsfehler als DTO-Objekt enthält
            throw new DeskValidationException(formValidationErrors);
        }else{
            // Füge den übergebenen Schreibtisch der Datenbank hinzu und gib das neu erstellte Desk-Objekt zurück.
            Desk eingefuegt = this.deskService.addDesk(desk);
            return ResponseEntity.ok(eingefuegt);
        }
    }



    /**
     * Diese Methode handhabt einen HTTP-GET-Request, der darauf abzielt, einen bestimmten Schreibtisch (Desk)
     * aus der Datenbank anhand seiner ID abzurufen.
     *
     * @param id Die ID des abzurufenden Schreibtisches.
     * @return Ein ResponseEntity-Objekt, das den abgerufenen Schreibtisch und den HTTP-Statuscode enthält.
     * @throws InterruptedException Wenn der Thread der Methode unterbrochen wird.
     * @throws ExecutionException Wenn die Ausführung einer asynchronen Aufgabe fehlschlägt.
     * @throws DeskNotFoundException Wenn der angeforderte Schreibtisch nicht gefunden wurde.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Desk> getDeskById(@PathVariable Long id) throws InterruptedException, ExecutionException,DeskNotFoundException {
        // Rufe den Schreibtisch mit der gegebenen ID vom deskService ab und gibt ihn als ResponseEntity zurück.
        return ResponseEntity.ok(this.deskService.getDeskById(id));
    }



    /**
     * HTTP-GET-Endpunkt für die Abfrage aller vorhandenen Schreibtische in der Datenbank.
     *
     * @return Eine ResponseEntity-Instanz mit der Liste aller Schreibtische und dem HTTP-Status-Code 200 OK.
     * @throws ExecutionException Wenn ein Fehler bei der Ausführung auftritt.
     * @throws InterruptedException Wenn die Ausführung der Methode durch eine Unterbrechung unterbrochen wird.
     */
    @GetMapping
    public ResponseEntity<List<Desk>> getAllDesks() throws ExecutionException, InterruptedException {

        // Ruft die Liste aller Schreibtische aus der deskService-Klasse ab und gibt sie zurück
        List<Desk> allDesks = this.deskService.getAllDesk();

        // Erstellt eine ResponseEntity-Instanz, die die Liste der Schreibtische und den HTTP-Status-Code 200 OK enthält
        return ResponseEntity.ok(allDesks);
    }


    /**
     * Aktualisiert einen Schreibtisch mit dem angegebenen Schreibtischobjekt.
     *
     * @param desk Das Schreibtischobjekt, das die aktualisierten Informationen enthält.
     * @return Eine ResponseEntity, die das aktualisierte Schreibtischobjekt enthält.
     * @throws ExecutionException, wenn ein Ausführungsfehler auftritt.
     * @throws InterruptedException, wenn der Thread während des Wartens auf das Ergebnis unterbrochen wird.
     * @throws DeskNotFoundException, wenn der zu aktualisierende Schreibtisch nicht in der Datenbank gefunden wird.
     */
    @PutMapping("/update")
    public ResponseEntity<Desk> updateDeskById(@Valid @RequestBody Desk desk) throws ExecutionException, InterruptedException, DeskNotFoundException {
        // Desk-Objekt wird aktualisiert, indem es an die deskService.updateDeskById()-Methode übergeben wird.
        Desk updated = this.deskService.updateDeskById(desk);
        // ResponseEntity ist ein Wrapper, der die HTTP-Antwort und den Inhalt kombiniert.
        // Wenn die Aktualisierung erfolgreich ist, wird ein HTTP-Statuscode 200 (OK) zurückgegeben und das aktualisierte Desk-Objekt wird als Teil der ResponseEntity zurückgegeben.
        return ResponseEntity.ok(updated);
    }


    /**
     * Löscht einen Schreibtisch mit der angegebenen ID.
     *
     * @param id Die ID des zu löschenden Schreibtisches.
     * @throws EmployeeDeletionNotPossibleException Wenn der Schreibtisch von einem Mitarbeiter verwendet wird und nicht gelöscht werden kann.
     * @throws DeskDeletionNotPossibleException Wenn der Schreibtisch nicht gefunden werden konnte und daher nicht gelöscht werden kann.
     */
    @DeleteMapping("/{id}")
    public void deleteDeskById(@Valid @PathVariable Long id) throws EmployeeDeletionNotPossibleException, DeskDeletionNotPossibleException {
        this.deskService.deleteDeskById(id);
    }


    @GetMapping("/test")
    public ResponseEntity<String> testGetEndpoint(){
        return ResponseEntity.ok("Test Get Endpoint is Working!");
    }


}
