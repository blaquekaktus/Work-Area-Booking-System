package com.itkolleg.bookingsystem.controller.RessourceBooking;

import com.itkolleg.bookingsystem.controller.booking.DeskBooking.DeskBookingWebController;
import com.itkolleg.bookingsystem.domains.*;
import com.itkolleg.bookingsystem.domains.Booking.RessourceBooking;
import com.itkolleg.bookingsystem.exceptions.DeskNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.RessourceExceptions.RessourceNotFoundException;
import com.itkolleg.bookingsystem.service.Employee.EmployeeService;
import com.itkolleg.bookingsystem.service.Ressource.RessourceService;
import com.itkolleg.bookingsystem.service.RessourceBooking.RessourceBookingService;
import com.itkolleg.bookingsystem.service.TimeSlot.TimeSlotService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(DeskBookingWebController.class);

    public RessourceBookingWebController(RessourceBookingService ressourceBookingService, RessourceService ressourceService, EmployeeService employeeService) {
        this.ressourceBookingService = ressourceBookingService;
        this.ressourceService = ressourceService;
        this.employeeService = employeeService;
    }

    @GetMapping("/allBookingsEmployee")
    public ModelAndView allBookingsEmployee() throws ExecutionException, InterruptedException, EmployeeNotFoundException {

        //TODO: Hilfsmethode schreiben um Code-Duplikate zu vermeiden
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new EmployeeNotFoundException("Employee not found");
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Employee employee = this.employeeService.getEmployeeByNick(username);

        List<RessourceBooking> bookings = ressourceBookingService.getBookingsByEmployee(employee);
        return new ModelAndView("ressourceBooking/viewRessourceBookingsEmployee", "bookings", bookings);
    }

    @GetMapping("/allBookings")
    public ModelAndView allBookings() throws ExecutionException, InterruptedException, EmployeeNotFoundException {

        List<RessourceBooking> bookings = ressourceBookingService.getAllBookings();
        return new ModelAndView("ressourceBooking/viewRessourceBookings", "bookings", bookings);
    }

    @GetMapping("/createBookingEmployee/{id}")
    public ModelAndView createBookingEmployee(@PathVariable Long id, Model model) throws RessourceNotFoundException, ExecutionException, InterruptedException, EmployeeNotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new EmployeeNotFoundException("Employee not found");
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Employee employee = this.employeeService.getEmployeeByNick(username);

        RessourceBooking booking = new RessourceBooking();

        Ressource ressource = this.ressourceService.getRessourceById(id);
        booking.setRessource(ressource);

        booking.setEmployee(employee);
        model.addAttribute("newBooking", booking);
        return new ModelAndView("ressourceBooking/createRessourceBookingEmployee", "Booking", model);
    }

    @PostMapping("/createBookingEmployee")
    public String createBookingEmployee(@Valid RessourceBooking booking, BindingResult bindingResult) throws RessourceNotAvailableException, ResourceNotFoundException {

        if (bindingResult.hasErrors()) {
            return "redirect:/web/ressourceBooking/createBookingEmployee/" + booking.getRessource().getId();
        } else {
            this.ressourceBookingService.addRessourceBooking(booking);
            return "redirect:/web/ressource/allRessourcesEmployee";
        }

    }

    @GetMapping("/createBooking/{id}")
    public ModelAndView createBookingAdmin(@PathVariable Long id, Model model) throws RessourceNotFoundException, ExecutionException, InterruptedException, EmployeeNotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new EmployeeNotFoundException("Employee not found");
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Employee employee = this.employeeService.getEmployeeByNick(username);

        RessourceBooking booking = new RessourceBooking();

        Ressource ressource = this.ressourceService.getRessourceById(id);
        booking.setRessource(ressource);

        booking.setEmployee(employee);
        model.addAttribute("newBooking", booking);
        return new ModelAndView("ressourceBooking/createRessourceBooking", "Booking", model);
    }

    @PostMapping("/createBooking")
    public String createBookingAdmin(@Valid RessourceBooking booking, BindingResult bindingResult) throws RessourceNotAvailableException, ResourceNotFoundException {

        if (bindingResult.hasErrors()) {
            return "redirect:/web/ressourceBooking/createBooking/" + booking.getRessource().getId();
        } else {
            this.ressourceBookingService.addRessourceBooking(booking);
            return "redirect:/web/ressource/allRessources";
        }

    }

    @GetMapping("/updateBooking/{id}")
    public ModelAndView updateBooking(@PathVariable Long id, Model model) throws RessourceNotFoundException, ExecutionException, InterruptedException, ResourceNotFoundException {

        RessourceBooking booking = this.ressourceBookingService.getBookingById(id);
        model.addAttribute("updateBooking", booking);
        return new ModelAndView("ressourceBooking/editRessourceBooking", "Booking", model);
    }

    @PostMapping("/updateBooking")
    public String updateBooking(@Valid RessourceBooking booking, BindingResult bindingResult) throws RessourceAlreadyExistsException, ExecutionException, InterruptedException, RessourceNotFoundException, RessourceNotAvailableException, ResourceNotFoundException {
        if (bindingResult.hasErrors()) {
            return "/ressourceBooking/editRessourceBooking";
        } else {
            this.ressourceBookingService.updateBooking(booking);
            return "redirect:/web/ressourceBooking/allBookings";
        }
    }

    @GetMapping("/deleteBookingEmployee/{id}")
    public String deleteBookingEmployee(@PathVariable Long id) throws RessourceDeletionNotPossibleException {
        try {
            this.ressourceBookingService.deleteBookingById(id);
            return "redirect:/web/ressourceBooking/allBookingsEmployee";
        } catch (ResourceDeletionFailureException | ResourceNotFoundException e) {
            return "redirect:/web/ressourceBooking/allBookingsEmployee";
        }
    }

    @GetMapping("/deleteBooking/{id}")
    public String deleteBookingAdmin(@PathVariable Long id) throws RessourceDeletionNotPossibleException {
        try {
            this.ressourceBookingService.deleteBookingById(id);
            return "redirect:/web/ressourceBooking/allBookings";
        } catch (ResourceDeletionFailureException | ResourceNotFoundException e) {
            return "redirect:/web/ressourceBooking/allBookings";
        }
    }

}
