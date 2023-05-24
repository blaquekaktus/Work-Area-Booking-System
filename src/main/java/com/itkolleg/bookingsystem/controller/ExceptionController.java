package com.itkolleg.bookingsystem.controller;

import com.itkolleg.bookingsystem.domains.ErrorDetails;
import com.itkolleg.bookingsystem.exceptions.BookingExceptions.BookingNotFoundException;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskNotFoundException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeValidationException;
import com.itkolleg.bookingsystem.exceptions.ExceptionDTO;
import com.itkolleg.bookingsystem.exceptions.FormValidationExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice
public class ExceptionController {

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

    @ExceptionHandler(BookingNotFoundException.class)
    public ModelAndView handleBookingNotFoundException(BookingNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("errorPage");
        modelAndView.addObject("errorDetails", new ErrorDetails("Booking Not Found", "The booking was not found."));
        e.printStackTrace();
        return modelAndView;
    }


    @ExceptionHandler(DeskNotFoundException.class)
    public ModelAndView handleDeskNotFoundException(DeskNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("errorPage");
        modelAndView.addObject("errorDetails", new ErrorDetails("Desk Not Found", "The desk was not found."));
        e.printStackTrace();
        return modelAndView;
    }

    @ExceptionHandler(DeskNotAvailableException.class)
    public ModelAndView handleDeskNotAvailableException(DeskNotAvailableException e) {
        ModelAndView modelAndView = new ModelAndView("errorPage");
        modelAndView.addObject("errorDetails", new ErrorDetails("Desk Not Available", "The desk is not available."));
        e.printStackTrace();
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneralException(Exception e) {
        ModelAndView modelAndView = new ModelAndView("errorPage");
        modelAndView.addObject("errorDetails", new ErrorDetails("UnknownException", "An unexpected error occurred."));
        e.printStackTrace();
        return modelAndView;
    }

}
