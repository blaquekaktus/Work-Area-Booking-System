package com.itkolleg.bookingsystem.controller.RessourceBooking;
import com.itkolleg.bookingsystem.domains.*;
import com.itkolleg.bookingsystem.domains.Booking.RessourceBooking;
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
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Diese Klasse repräsentiert einen WebController für das Modul RessourceBooking. Der Controller ermöglicht die Interaktion mit Ressourcen über HTTP-Anfragen und -Antworten.
 * @author Manuel Payer
 * @version 1.0
 * @since 25.06.2023
 */
@Controller
@RequestMapping("/web/ressourceBooking")
public class RessourceBookingWebController {

    RessourceBookingService ressourceBookingService;
    RessourceService ressourceService;
    EmployeeService employeeService;

    /**
     * Konstruktor der Klasse RessourceBookingWebController. Der Konstruktor nimmt folgende Parameter entgegen:
     * @param ressourceBookingService vom Typ RessourceBookingService
     * @param ressourceService vom Typ RessourceService
     * @param employeeService vom Typ EmployeeService
     */
    public RessourceBookingWebController(RessourceBookingService ressourceBookingService, RessourceService ressourceService, EmployeeService employeeService) {
        this.ressourceBookingService = ressourceBookingService;
        this.ressourceService = ressourceService;
        this.employeeService = employeeService;
    }

    /**
     * Diese Methode gibt eine Liste aller Ressourcen Buchungen für einen/eine angemeldeten/angemeldetet Mitarbeiter:inn aus.
     * Die Methode prüft, ob zur Laufzeit ein/eine angemeldeter/angemeldete Mitarbeiter:inn existiert. Wenn nicht, wird eine EmployeeNotFoundException geworfen.
     * Scheitert die Prüfung nicht, wird eine Liste für den angemeldeten Employee geliefert. Die Identifizierung läuft über den einzigartigen NickName.
     *
     * Diese Methode ist mit @GetMapping annotiert, da sie eine HTTP-Anfrage verarbeiten und zurückliefern muss.
     *
     * @return ModelAndView
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws EmployeeNotFoundException
     */
    @GetMapping("/allBookingsEmployee")
    public ModelAndView allBookingsEmployee() throws EmployeeNotFoundException {

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

    /**
     * Diese Methode liefert dem/der angemeldeten Admin eine Liste aller Buchungen zurück.
     * Diese Methode ist mit @GetMapping annotiert, da sie eine HTTP-Anfrage verarbeiten und zurückliefern muss.
     * @return ModelAndView
     */
    @GetMapping("/allBookings")
    public ModelAndView allBookings(){

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
    public ModelAndView updateBooking(@PathVariable Long id, Model model) throws RessourceNotFoundException, ExecutionException, InterruptedException, ResourceNotFoundException, EmployeeNotFoundException {

        RessourceBooking booking = this.ressourceBookingService.getBookingById(id);
        model.addAttribute("updateBooking", booking);
        return new ModelAndView("ressourceBooking/editRessourceBooking", "Booking", model);
    }

    @PostMapping("/updateBooking")
    public String updateBooking(@Valid RessourceBooking booking, BindingResult bindingResult) throws RessourceAlreadyExistsException, ExecutionException, InterruptedException, RessourceNotFoundException, RessourceNotAvailableException, ResourceNotFoundException, EmployeeNotFoundException {

        if (bindingResult.hasErrors()) {
            return "/ressourceBooking/editRessourceBooking";
        } else {
            this.ressourceBookingService.updateBooking(booking);
            return "redirect:/web/ressourceBooking/allBookings";
        }
    }

    /**
     * Diese Methode löscht eine Buchung für den/die angemeldeten/angemeldete Benutzer:inn. Dabei ist zu beachten, nur durch das Löschen der Buchung die Ressource
     * wieder freigegeben wird. Erst dann kann man die Ressource selbst wieder löschen.
     *
     * Es findet eine Prüfung statt, ob die Buchung getätigt werden kann. Die Methode fängt ResourceDeletionFailureException und ResourceNotFoundException ab.
     *
     * Diese Methode ist mit @GetMapping annotiert, da sie eine HTTP-Anfrage verarbeiten und zurückliefern muss.
     * @param id
     * @return Webseitenaufruf auf ViewBookingsEmployee
     */
    @GetMapping("/deleteBookingEmployee/{id}")
    public String deleteBookingEmployee(@PathVariable Long id) {
        try {
            this.ressourceBookingService.deleteBookingById(id);
            return "redirect:/web/ressourceBooking/allBookingsEmployee";
        } catch (ResourceDeletionFailureException | ResourceNotFoundException e) {
            return "redirect:/web/ressourceBooking/allBookingsEmployee";
        }
    }

    /**
     * Diese Methode löscht eine Ressourcenbuchung für den/die angemeldeten/angemeldete Admin.
     * Es findet eine Prüfung statt, ob die Buchung getätigt werden kann. Die Methode fängt ResourceDeletionFailureException und ResourceNotFoundException ab.
     *
     * Diese Methode ist mit @GetMapping annotiert, da sie eine HTTP-Anfrage verarbeiten und zurückliefern muss.
     * @param id vom Typ Long
     * @return Webseitenaufruf auf ViewAllBookings
     */
    @GetMapping("/deleteBooking/{id}")
    public String deleteBookingAdmin(@PathVariable Long id) {
        try {
            this.ressourceBookingService.deleteBookingById(id);
            return "redirect:/web/ressourceBooking/allBookings";
        } catch (ResourceDeletionFailureException | ResourceNotFoundException e) {
            return "redirect:/web/ressourceBooking/allBookings";
        }
    }

}
