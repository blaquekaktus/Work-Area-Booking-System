package com.itkolleg.bookingsystem.controller.RessourceBooking;

import com.itkolleg.bookingsystem.domains.Booking.RessourceBooking;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Ressourcetype;
import com.itkolleg.bookingsystem.domains.TimeSlot;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceAlreadyExistsException;
import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceNotFoundException;
import com.itkolleg.bookingsystem.service.Employee.EmployeeService;
import com.itkolleg.bookingsystem.service.Ressource.RessourceService;
import com.itkolleg.bookingsystem.service.RessourceBooking.RessourceBookingService;
import com.itkolleg.bookingsystem.service.TimeSlot.TimeSlotService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/web/ressourceBooking")
public class RessourceBookingWebController {

    RessourceBookingService ressourceBookingService;
    RessourceService ressourceService;
    EmployeeService employeeService;

    public RessourceBookingWebController(RessourceBookingService ressourceBookingService, RessourceService ressourceService, EmployeeService employeeService) {
        this.ressourceBookingService = ressourceBookingService;
        this.ressourceService = ressourceService;
        this.employeeService = employeeService;
    }

    @GetMapping("/allBookingsEmployee")
    public ModelAndView allBookings() throws ExecutionException, InterruptedException, EmployeeNotFoundException {

        //TODO: Hilfsmethode schreiben um Code-Duplikate zu vermeiden
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new EmployeeNotFoundException("Employee not found");
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Employee employee = this.employeeService.getEmployeeByNick(username);

        List<RessourceBooking> bookings = ressourceBookingService.getBookingsByEmployee(employee);
        return new ModelAndView("ressourceBooking/viewRessourceBookings", "bookings", bookings);
    }

    @GetMapping("/createBooking/{id}")
    public ModelAndView createBooking(@PathVariable Long id, Model model) throws RessourceNotFoundException, ExecutionException, InterruptedException, EmployeeNotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new EmployeeNotFoundException("Employee not found");
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Employee employee = this.employeeService.getEmployeeByNick(username);

        Ressource ressource = this.ressourceService.getRessourceById(id);

        RessourceBooking booking = new RessourceBooking();

        booking.setRessource(ressource);
        booking.setEmployee(employee);
        model.addAttribute("newBooking", booking);
        return new ModelAndView("ressourceBooking/createRessourceBooking", "Booking", model);
    }

    @PostMapping("/createBooking")
    public String createRessourceBooking(@Valid RessourceBooking booking, BindingResult bindingResult) throws RessourceNotAvailableException, ResourceNotFoundException {
        System.out.println(booking.getDate());
        System.out.println(booking.getStart());
        System.out.println(booking.getEndTime());
        if (bindingResult.hasErrors()) {
            return "redirect:/web/ressourceBooking/createBooking/" + booking.getRessource().getId();
        } else {
            this.ressourceBookingService.addRessourceBooking(booking);
            return "redirect:/web/ressource/allRessourcesEmployee";
        }
    }

    @GetMapping("/deleteBooking/{id}")
    public String deleteBooking(@PathVariable Long id) {
        try {
            this.ressourceBookingService.deleteBookingById(id);
            return "redirect:/web/ressourceBooking/allBookingsEmployee";
        } catch (ResourceDeletionFailureException | ResourceNotFoundException e) {
            return "redirect:/web/ressourceBooking/allBookingsEmployee";
        }
    }

}
