package com.itkolleg.bookingsystem.controller;

import com.itkolleg.bookingsystem.domains.ErrorCode;
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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

/**
 *
 * This class handles global exceptions for the booking system.
 * @author Sonja Lechner, Marcel Schranz
 * @version 1.0
 * @since 2023-05-24
 */

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
     * @param e                 The ResourceNotFoundException instance.
     * @param redirectAttributes The RedirectAttributes object to add flash attributes.
     * @return A string representing the redirect path to the error page.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException e, RedirectAttributes redirectAttributes) {
        logger.error("ResourceNotFoundException: {}", e.getMessage(), e);
        redirectAttributes.addFlashAttribute("errorCode", ErrorCode.RESOURCE_NOT_FOUND.getCode());
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
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
        redirectAttributes.addFlashAttribute("errorCode", ErrorCode.DESK_NOT_AVAILABLE.getCode());
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
        redirectAttributes.addFlashAttribute("errorCode", ErrorCode.DATA_ACCESS_ERROR.getCode());
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/error";
    }

    /**
     * Handles the MethodArgumentNotValidException and redirects to the error page.
     *
     * @param ex                 The MethodArgumentNotValidException instance.
     * @param redirectAttributes The RedirectAttributes object to add flash attributes.
     * @return A string representing the redirect path to the error page.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValid(MethodArgumentNotValidException ex, RedirectAttributes redirectAttributes) {
        logger.error("MethodArgumentNotValidException: {}", ex.getMessage(), ex);
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        redirectAttributes.addFlashAttribute("errorCode", ErrorCode.METHOD_ARGUMENT_NOT_VALID.getCode());
        redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
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
        redirectAttributes.addFlashAttribute("errorCode", ErrorCode.GENERAL_ERROR.getCode());
        redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred.");
        return "redirect:/error";
    }
}
