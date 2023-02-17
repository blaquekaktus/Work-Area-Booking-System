package com.itkolleg.bookingsystem.controller;

import com.itkolleg.bookingsystem.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ExceptionDTO> employeeNotFound(EmployeeNotFoundException employeeNotFoundException){
        return new ResponseEntity<>(new ExceptionDTO("1000", employeeNotFoundException.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmployeeValidationException.class)
    public ResponseEntity<FormValidationExceptionDTO> employeeValidationException(EmployeeValidationException employeeValidationException){
        return new ResponseEntity<>(employeeValidationException.getErrorMap(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDTO> employeeDeletionNotPossibleException(EmployeeDeletionNotPossibleException employeeDeletionNotPossibleException){
        return new ResponseEntity<>(new ExceptionDTO("1000", employeeDeletionNotPossibleException.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmployeeAlreadyExistsException.class)
    public ResponseEntity<ExceptionDTO> employeeAlreadyExists(EmployeeAlreadyExistsException employeeAlreadyExistsException){
        return new ResponseEntity<>(new ExceptionDTO("1000", employeeAlreadyExistsException.getMessage()), HttpStatus.NOT_FOUND);
    }

}
