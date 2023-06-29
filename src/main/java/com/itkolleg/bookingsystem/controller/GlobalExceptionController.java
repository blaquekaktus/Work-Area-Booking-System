package com.itkolleg.bookingsystem.controller;

import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.exceptions.DeskNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeValidationException;
import com.itkolleg.bookingsystem.exceptions.ExceptionDTO;
import com.itkolleg.bookingsystem.exceptions.FormValidationExceptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@ControllerAdvice
public class GlobalExceptionController {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class);


    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ExceptionDTO> employeeNotFound(EmployeeNotFoundException employeeNotFoundException) {
        return new ResponseEntity<>(new ExceptionDTO("1000", employeeNotFoundException.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmployeeValidationException.class)
    public ResponseEntity<FormValidationExceptionDTO> employeeValidationException(EmployeeValidationException employeeValidationException) {
        return new ResponseEntity<>(employeeValidationException.getErrorMap(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDTO> employeeDeletionNotPossibleException(EmployeeDeletionNotPossibleException employeeDeletionNotPossibleException) {
        return new ResponseEntity<>(new ExceptionDTO("1000", employeeDeletionNotPossibleException.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmployeeAlreadyExistsException.class)
    public ResponseEntity<ExceptionDTO> employeeAlreadyExists(EmployeeAlreadyExistsException employeeAlreadyExistsException) {
        return new ResponseEntity<>(new ExceptionDTO("1000", employeeAlreadyExistsException.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles the ResourceNotFoundException and redirects to the error page.
     *
     * @param ex                 The ResourceNotFoundException instance.
     * @param redirectAttributes The RedirectAttributes object to add flash attributes.
     * @return A string representing the redirect path to the error page.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, RedirectAttributes redirectAttributes) {
        logger.error("ResourceNotFoundException: {}", ex.getMessage(), ex);
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/error";
    }

    /**
     * Handles the DeskNotAvailableException and redirects to the error page.
     *
     * @param e                  The DeskNotAvailableException instance.
     * @param redirectAttributes The RedirectAttributes object to add flash attributes.
     * @return A string representing the redirect path to the error page.
     */
    @ExceptionHandler(DeskNotAvailableException.class)
    public String handleDeskNotAvailable(DeskNotAvailableException e, RedirectAttributes redirectAttributes) {
        logger.error("DeskNotAvailableException: {}", e.getMessage(), e);
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/error";
    }

    /**
     * Handles the EmptyResultDataAccessException and redirects to the error page.
     *
     * @param e                  The EmptyResultDataAccessException instance.
     * @param redirectAttributes The RedirectAttributes object to add flash attributes.
     * @return A string representing the redirect path to the error page.
     */
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public String handleEmptyResultDataAccess(EmptyResultDataAccessException e, RedirectAttributes redirectAttributes) {
        logger.error("EmptyResultDataAccessException: {}", e.getMessage(), e);
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/error";
    }

    /**
     * Handles any general Exception and redirects to the error page.
     *
     * @param e                  The Exception instance.
     * @param redirectAttributes The RedirectAttributes object to add flash attributes.
     * @return A string representing the redirect path to the error page.
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception e, RedirectAttributes redirectAttributes) {
        logger.error("Exception: {}", e.getMessage(), e);
        redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred.");
        return "redirect:/error";
    }
}
