package com.itkolleg.bookingsystem.controller.desk;

import com.itkolleg.bookingsystem.Service.DeskService;
import com.itkolleg.bookingsystem.Service.EmployeeService;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.*;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskNotFoundException;
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

    public DeskRestController(DeskService deskService) {
        this.deskService = deskService;
    }

    //TODO: Response Entity zurückgeben
    @PostMapping
    public ResponseEntity<Desk> addDesk(@Valid @RequestBody Desk desk, BindingResult bindingResult) throws EmployeeValidationException, EmployeeAlreadyExistsException, ExecutionException, InterruptedException {
        // Erstelle ein neues Objekt der Klasse FormValidationExceptionDTO, das später dazu verwendet wird,
        // etwaige Validierungsfehler der übergebenen Employee-Daten zu speichern.
        FormValidationExceptionDTO formValidationErrors = new FormValidationExceptionDTO("9000");

        // Prüfe, ob es Validierungsfehler in den übergebenen Employee-Daten gibt. Falls ja, iteriere durch alle
        // Fehler und füge sie dem formValidationErrors-Objekt hinzu.
        if(bindingResult.hasErrors()){
            for(ObjectError error : bindingResult.getAllErrors()){
                formValidationErrors.addFormValidationError(((FieldError) error).getField(), error.getDefaultMessage());
            }
            // Wirf eine Exception, die die Validierungsfehler als DTO-Objekt enthält
            throw new EmployeeValidationException(formValidationErrors);
        }else{
            Desk eingefuegt = this.deskService.addDesk(desk);
            return ResponseEntity.ok(eingefuegt);
            // Füge den übergebenen Employee der Datenbank hinzu und gib das neu erstellte Employee-Objekt zurück.
            //return employeeService.addEmployee(employee);
        }
    }


    // Definiert den GET-Endpunkt für die Employee-Ressource, der auf eine spezifische ID zugreift
    @GetMapping("/{id}")
    public ResponseEntity<Desk> getDeskById(@PathVariable Long id) throws InterruptedException, ExecutionException, EmployeeNotFoundException, EmployeeNotFoundException, DeskNotFoundException {
        return ResponseEntity.ok(this.deskService.getDeskById(id));
        // Aufruf des entsprechenden EmployeeService-Method, um den Employee mit der angegebenen ID zu finden
        //return employeeService.getEmployeeById(id);
    }



    @GetMapping
    public ResponseEntity<List<Desk>> getAllDesks() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(this.deskService.getAllDesk());
    }

    @PutMapping("/update")
    public ResponseEntity<Desk> updateDeskById(@Valid @RequestBody Desk desk) throws ExecutionException, InterruptedException, EmployeeNotFoundException, DeskNotFoundException {
        Desk updated = this.deskService.updateDeskById(desk);
        return ResponseEntity.ok(updated);
        //return employeeService.updateEmployeeById(employee);
    }

    @DeleteMapping("/{id}")
    public void deleteDeskById(@Valid @PathVariable Long id) throws EmployeeDeletionNotPossibleException, DeskDeletionNotPossibleException {
        this.deskService.deleteDeskById(id);
    }

    @GetMapping("/test")
    public ResponseEntity<String> testGetEndpoint(){
        return ResponseEntity.ok("Test Get Endpoint is Working!");
    }


}
