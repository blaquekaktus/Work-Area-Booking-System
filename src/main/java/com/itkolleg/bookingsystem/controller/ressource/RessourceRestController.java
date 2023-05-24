package com.itkolleg.bookingsystem.controller.ressource;

import com.itkolleg.bookingsystem.service.Ressource.RessourceService;
import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.FormValidationExceptionDTO;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceNotFoundException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceValidationException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/ressources")
public class RessourceRestController {


    private final RessourceService ressourceService;

    public RessourceRestController(RessourceService ressourceService) {
        this.ressourceService = ressourceService;
    }

    @PostMapping
    public ResponseEntity<Ressource> addRessource(@Valid @RequestBody Ressource ressource, BindingResult bindingResult) throws RessourceNotFoundException, ExecutionException, InterruptedException, RessourceValidationException, RessourceAlreadyExistsException {
        // Erstelle ein neues Objekt der Klasse FormValidationExceptionDTO, das später dazu verwendet wird,
        // etwaige Validierungsfehler der übergebenen Ressource-Daten zu speichern.
        FormValidationExceptionDTO formValidationErrors = new FormValidationExceptionDTO("9000");

        // Prüfe, ob es Validierungsfehler in den übergebenen Ressource-Daten gibt. Falls ja, iteriere durch alle
        // Fehler und füge sie dem formValidationErrors-Objekt hinzu.
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                formValidationErrors.addFormValidationError(((FieldError) error).getField(), error.getDefaultMessage());
            }
            // Wirf eine Exception, die die Validierungsfehler als DTO-Objekt enthält
            throw new RessourceValidationException(formValidationErrors);
        } else {
            Ressource eingefuegt = this.ressourceService.addRessource(ressource);
            return ResponseEntity.ok(eingefuegt);
            // Füge den übergebenen Room der Datenbank hinzu und gib das neu erstellte Employee-Objekt zurück.
            //return ResponseEntity.ok(ressourceService.addRessource(ressource));
        }
    }


    // Definiert den GET-Endpunkt für die Room-Ressource, der auf eine spezifische ID zugreift
    @GetMapping("/{id}")
    public ResponseEntity<Ressource> getRessourceById(@PathVariable Long id) throws InterruptedException, ExecutionException, RessourceNotFoundException {
        return ResponseEntity.ok(this.ressourceService.getRessourceById(id));
        //Aufruf des entsprechenden EmployeeService-Method, um den Employee mit der angegebenen ID zu finden
        //return ressourceService.getRessourceById(id);
    }


    @GetMapping
    public ResponseEntity<List<Ressource>> getAllRessource() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(this.ressourceService.getAllRessource());
        //return ressourceService.getAllRessource();
    }

    @PutMapping("/update")
    public ResponseEntity<Ressource> updateRessourceById(@Valid @RequestBody Ressource ressource) throws ExecutionException, InterruptedException, RessourceNotFoundException {
        Ressource updated = this.ressourceService.updateRessourceById(ressource);
        return ResponseEntity.ok(updated);
        //return ressourceService.updateRessourceById(ressource);
    }

    @DeleteMapping("/{id}")
    public void deleteRessourceById(@Valid @PathVariable Long id) throws RessourceDeletionNotPossibleException {
        this.ressourceService.deleteRessourceById(id);
    }

    @GetMapping("/test")
    public ResponseEntity<String> testGetEndpoint() {
        return ResponseEntity.ok("Test Get Endpoint is Working!");
    }


}
