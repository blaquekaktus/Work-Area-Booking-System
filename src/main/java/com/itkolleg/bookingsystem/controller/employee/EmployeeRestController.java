package com.itkolleg.bookingsystem.controller.employee;

import com.itkolleg.bookingsystem.service.Employee.EmployeeService;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeValidationException;
import com.itkolleg.bookingsystem.exceptions.FormValidationExceptionDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeRestController {
    private final EmployeeService employeeService;

    public EmployeeRestController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody Employee employee, BindingResult bindingResult) throws EmployeeValidationException, EmployeeAlreadyExistsException, ExecutionException, InterruptedException {
        // Erstelle ein neues Objekt der Klasse FormValidationExceptionDTO, das später dazu verwendet wird,
        // etwaige Validierungsfehler der übergebenen Employee-Daten zu speichern.
        FormValidationExceptionDTO formValidationErrors = new FormValidationExceptionDTO("9000");

        // Prüfe, ob es Validierungsfehler in den übergebenen Employee-Daten gibt. Falls ja, iteriere durch alle
        // Fehler und füge sie dem formValidationErrors-Objekt hinzu.
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                formValidationErrors.addFormValidationError(((FieldError) error).getField(), error.getDefaultMessage());
            }
            // Wirf eine Exception, die die Validierungsfehler als DTO-Objekt enthält
            throw new EmployeeValidationException(formValidationErrors);
        } else {
            Employee eingefuegt = this.employeeService.addEmployee(employee);
            return ResponseEntity.ok(eingefuegt);
            // Füge den übergebenen Employee der Datenbank hinzu und gib das neu erstellte Employee-Objekt zurück.
            //return employeeService.addEmployee(employee);
        }
    }


    // Definiert den GET-Endpunkt für die Employee-Ressource, der auf eine spezifische ID zugreift
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) throws InterruptedException, ExecutionException, EmployeeNotFoundException {
        return ResponseEntity.ok(this.employeeService.getEmployeeById(id));
        // Aufruf des entsprechenden EmployeeService-Method, um den Employee mit der angegebenen ID zu finden
        //return employeeService.getEmployeeById(id);
    }

    //test

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(this.employeeService.getAllEmployees());
        //return employeeService.getAllEmployees();
    }

    @PutMapping("/update")
    public ResponseEntity<Employee> updateEmployeeById(@Valid @RequestBody Employee employee) throws ExecutionException, InterruptedException, EmployeeNotFoundException, EmployeeAlreadyExistsException {
        Employee updated = this.employeeService.updateEmployeeById(employee);
        return ResponseEntity.ok(updated);
        //return employeeService.updateEmployeeById(employee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployeeById(@Valid @PathVariable Long id) throws EmployeeDeletionNotPossibleException {
        this.employeeService.deleteEmployeeById(id);
    }

    @GetMapping("/test")
    public ResponseEntity<String> testGetEndpoint() {
        return ResponseEntity.ok("Test Get Endpoint is Working!");
    }


}
