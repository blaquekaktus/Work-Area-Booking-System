package com.itkolleg.bookingsystem.controller.room;

import com.itkolleg.bookingsystem.Service.RoomService;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeValidationException;
import com.itkolleg.bookingsystem.exceptions.FormValidationExceptionDTO;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomNotFoundException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomValidationException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/rooms")
public class RoomRestController {
    private RoomService roomService;

    public RoomRestController(RoomService roomService){
        this.roomService = roomService;
    }

    @PostMapping("/create")
    public Room addRoom(@Valid @RequestBody Room room, BindingResult bindingResult) throws RoomValidationException, ExecutionException, InterruptedException {
        // Erstelle ein neues Objekt der Klasse FormValidationExceptionDTO, das später dazu verwendet wird,
        // etwaige Validierungsfehler der übergebenen Room-Daten zu speichern.
        FormValidationExceptionDTO formValidationErrors = new FormValidationExceptionDTO("9000");

        // Prüfe, ob es Validierungsfehler in den übergebenen Room-Daten gibt. Falls ja, iteriere durch alle
        // Fehler und füge sie dem formValidationErrors-Objekt hinzu.
        if(bindingResult.hasErrors()){
            for(ObjectError error : bindingResult.getAllErrors()){
                formValidationErrors.addFormValidationError(((FieldError) error).getField(), error.getDefaultMessage());
            }
            // Wirf eine Exception, die die Validierungsfehler als DTO-Objekt enthält
            throw new RoomValidationException(formValidationErrors);
        }

        // Füge den übergebenen Employee der Datenbank hinzu und gib das neu erstellte Room-Objekt zurück.
        return roomService.addRoom(room);
    }

    // Definiert den GET-Endpunkt für die Romm-Ressource, der auf eine spezifische ID zugreift
    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable Long id) throws InterruptedException, ExecutionException, RoomNotFoundException {
        // Aufruf des entsprechenden EmployeeService-Method, um den Raum mit der angegebenen ID zu finden
        return roomService.getRoomById(id);
    }


    @GetMapping
    public List<Room> getAllRooms() throws ExecutionException, InterruptedException {
        return roomService.getAllRooms();
    }


    @PutMapping("/update")
    public Room updateRoomById(@Valid @RequestBody Room room) throws RoomNotFoundException, ExecutionException, InterruptedException {
        return roomService.updateRoomById(room);
    }


    @DeleteMapping("/{id}")
    public void deleteRoomById(@Valid @PathVariable Long id) throws RoomDeletionNotPossibleException {
        roomService.deleteRoomById(id);
    }

    @GetMapping("/test")
    public ResponseEntity<String> testGetEndpoint(){
        return ResponseEntity.ok("Test Get Endpoint is Working!");
    }

}
