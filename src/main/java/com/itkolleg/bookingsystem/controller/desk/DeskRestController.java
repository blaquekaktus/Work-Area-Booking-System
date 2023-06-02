package com.itkolleg.bookingsystem.controller.desk;

import com.itkolleg.bookingsystem.service.Desk.DeskService;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskNotFoundException;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskValidationFailureException;
import com.itkolleg.bookingsystem.exceptions.FormValidationExceptionDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;


@RestController

public class DeskRestController {
    private static final Logger logger = LoggerFactory.getLogger(DeskRestController.class);
    private final DeskService deskService;

    public DeskRestController(DeskService deskService) {
        this.deskService = deskService;
    }

    @PostMapping("api/v1/desk/")
    public ResponseEntity<Desk> addDesk(@Valid @RequestBody Desk desk, BindingResult bindingResult) throws DeskValidationFailureException, ExecutionException, InterruptedException {
        FormValidationExceptionDTO formValidationErrors = new FormValidationExceptionDTO("9000");
        String errors = "";
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                errors += "\nValidation failure for " + error.getObjectName() + " in the following field: "
                        + ((FieldError) error).getField() + "with the following problem: " + error.getDefaultMessage();
            }
            throw new DeskValidationFailureException(errors);
        } else {
            return ResponseEntity.ok(this.deskService.addDesk(desk));
        }
    }

    @GetMapping("api/v1/desks/")
    public ResponseEntity<List<Desk>> getAllDesks() {
        return ResponseEntity.ok(this.deskService.getAllDesks());
    }

    @GetMapping("api/v1/desksPage/")
    public ResponseEntity<Page<Desk>> getAllDesksByPage(Pageable pageable) {
        return ResponseEntity.ok(this.deskService.getAllDesksByPage(pageable));
    }

    @GetMapping("api/v1/desk/{id}/")
    public ResponseEntity<Desk> getDeskById(@PathVariable Long id) throws DeskNotFoundException {
        return ResponseEntity.ok(this.deskService.getDeskById(id));
    }

    @PutMapping("api/v1/desk/{id}/")
    public ResponseEntity<Desk> updateDeskById(@PathVariable Long id, @Valid @RequestBody Desk desk, BindingResult bindingResult) throws DeskNotFoundException, DeskValidationFailureException {
        StringBuilder errors = new StringBuilder();
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                errors.append("\nValidation failure for ").append(error.getObjectName()).append(" in the following field: ").append(((FieldError) error).getField()).append("with the following problem: ").append(error.getDefaultMessage());
            }
            throw new DeskValidationFailureException(errors.toString());
        } else {
            return ResponseEntity.ok(this.deskService.updateDeskById(id, desk));
        }
    }

    @DeleteMapping("api/v1/desk/{id}/")
    public ResponseEntity<List<Desk>> deleteDeskById(@PathVariable Long id) throws DeskNotFoundException, DeskDeletionFailureException {
        try {
            this.deskService.deleteDeskById(id);
            return ResponseEntity.ok(this.deskService.getAllDesks());
        } catch (Exception e) {
            System.out.println(e.getCause() + e.getClass().getName());
            throw new DeskDeletionFailureException("desk could not be deleted!");
        }
    }


}
