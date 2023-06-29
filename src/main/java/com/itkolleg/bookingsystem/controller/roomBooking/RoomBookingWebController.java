package com.itkolleg.bookingsystem.controller.roomBooking;

import com.itkolleg.bookingsystem.domains.*;
import com.itkolleg.bookingsystem.domains.Booking.RoomBooking;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeNotFoundException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomNotFoundException;
import com.itkolleg.bookingsystem.service.Employee.EmployeeService;
import com.itkolleg.bookingsystem.service.Room.RoomService;
import com.itkolleg.bookingsystem.service.RoomBooking.RoomBookingService;
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
 * Diese Klasse repräsentiert einen WebController für das Modul RoomBooking. Der Controller ermöglicht die Interaktion mit Räumen über HTTP-Anfragen und -Antworten.
 *
 * @author Manuel Payer
 * @version 1.0
 * @since 25.06.2023
 */
@Controller
@RequestMapping("/web/roomBooking")
public class RoomBookingWebController {

    RoomBookingService roomBookingService;
    RoomService roomService;
    EmployeeService employeeService;

    /**
     * Konstruktor der Klasse RoomBookingWebController. Der Konstruktor nimmt folgende Parameter entgegen:
     *
     * @param roomBookingService vom Typ RoomBookingService
     * @param roomService        vom Typ RoomService
     * @param employeeService    vom Typ EmployeeService
     */
    public RoomBookingWebController(RoomBookingService roomBookingService, RoomService roomService, EmployeeService employeeService) {
        this.roomBookingService = roomBookingService;
        this.roomService = roomService;
        this.employeeService = employeeService;
    }

    /**
     * Diese Methode gibt eine Liste aller Raum Buchungen für einen/eine angemeldeten/angemeldetet Mitarbeiter:inn aus.
     * Die Methode prüft, ob zur Laufzeit ein/eine angemeldeter/angemeldete Mitarbeiter:in existiert. Wenn nicht, wird eine EmployeeNotFoundException geworfen.
     * Scheitert die Prüfung nicht, wird eine Liste für den angemeldeten Employee geliefert. Die Identifizierung läuft über den einzigartigen NickName.
     * <p>
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

        List<RoomBooking> bookings = roomBookingService.getBookingsByEmployee(employee);
        return new ModelAndView("roomBooking/viewRoomBookingsEmployee", "bookings", bookings);
    }

    /**
     * Diese Methode liefert dem/der angemeldeten Admin eine Liste aller Buchungen zurück.
     * Diese Methode ist mit @GetMapping annotiert, da sie eine HTTP-Anfrage verarbeiten und zurückliefern muss.
     *
     * @return ModelAndView
     */
    @GetMapping("/allBookings")
    public ModelAndView allBookings() {

        List<RoomBooking> bookings = roomBookingService.getAllBookings();
        return new ModelAndView("roomBooking/viewRoomBookings", "bookings", bookings);
    }

    @GetMapping("/createBookingEmployee/{id}")
    public ModelAndView createBookingEmployee(@PathVariable Long id, Model model) throws RoomNotFoundException, ExecutionException, InterruptedException, EmployeeNotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new EmployeeNotFoundException("Employee not found");
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Employee employee = this.employeeService.getEmployeeByNick(username);

        RoomBooking booking = new RoomBooking();

        Room room = this.roomService.getRoomById(id);

        booking.setRoom(room);
        booking.setEmployee(employee);

        model.addAttribute("newBooking", booking);
        return new ModelAndView("roomBooking/createRoomBookingEmployee", "Booking", model);
    }

    @PostMapping("/createBookingEmployee")
    public String createBookingEmployee(@Valid RoomBooking booking, BindingResult bindingResult) throws RoomNotAvailableException, RoomNotFoundException {

        if (bindingResult.hasErrors()) {
            return "redirect:/web/roomBooking/createBookingEmployee/" + booking.getRoom().getId();
        } else {
            this.roomBookingService.addRoomBooking(booking);
            return "redirect:/web/rooms/allRoomsEmployee";
        }

    }

    @GetMapping("/createBooking/{id}")
    public ModelAndView createBookingAdmin(@PathVariable Long id, Model model) throws RoomNotFoundException, ExecutionException, InterruptedException, EmployeeNotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new EmployeeNotFoundException("Employee not found");
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Employee employee = this.employeeService.getEmployeeByNick(username);

        RoomBooking booking = new RoomBooking();

        Room room = this.roomService.getRoomById(id);
        booking.setRoom(room);

        booking.setEmployee(employee);
        model.addAttribute("newBooking", booking);
        return new ModelAndView("roomBooking/createRoomBooking", "Booking", model);
    }

    @PostMapping("/createBooking")
    public String createBookingAdmin(@Valid RoomBooking booking, BindingResult bindingResult) throws RoomNotAvailableException, RoomNotFoundException {

        if (bindingResult.hasErrors()) {
            return "redirect:/web/roomBooking/createBooking/" + booking.getRoom().getId();
        } else {
            this.roomBookingService.addRoomBooking(booking);
            return "redirect:/web/rooms/allRooms";
        }

    }

    @GetMapping("/updateBooking/{id}")
    public ModelAndView updateBooking(@PathVariable Long id, Model model) throws RoomNotFoundException, ExecutionException, InterruptedException, RoomNotFoundException, EmployeeNotFoundException {

        RoomBooking booking = this.roomBookingService.getBookingById(id);
        model.addAttribute("updateBooking", booking);
        return new ModelAndView("roomBooking/editRoomBooking", "Booking", model);
    }

    @PostMapping("/updateBooking")
    public String updateBooking(@Valid RoomBooking booking, BindingResult bindingResult) throws ExecutionException, InterruptedException, RoomNotFoundException, RoomNotAvailableException, RoomNotFoundException, EmployeeNotFoundException {

        if (bindingResult.hasErrors()) {
            return "/roomBooking/editRoomBooking";
        } else {
            this.roomBookingService.updateBooking(booking);
            return "redirect:/web/roomBooking/allBookings";
        }
    }

    /**
     * Diese Methode löscht eine Buchung für den/die angemeldeten/angemeldetet Benutzer:inn. Dabei ist zu beachten, nur durch das Löschen der Buchung das Room
     * wieder freigegeben wird. Erst dann kann man das Room selbst wieder löschen.
     * <p>
     * Es findet eine Prüfung statt, ob die Buchung getätigt werden kann. Die Methode fängt ResourceDeletionFailureException und ResourceNotFoundException ab.
     * <p>
     * Diese Methode ist mit @GetMapping annotiert, da sie eine HTTP-Anfrage verarbeiten und zurückliefern muss.
     *
     * @param id
     * @return Webseitenaufruf auf ViewBookingsEmployee
     */
    @GetMapping("/deleteBookingEmployee/{id}")
    public String deleteBookingEmployee(@PathVariable Long id) {
        try {
            this.roomBookingService.deleteBookingById(id);
            return "redirect:/web/roomBooking/allBookingsEmployee";
        } catch (RoomNotFoundException |
                 RoomDeletionNotPossibleException e) {
            return "redirect:/web/roomBooking/allBookingsEmployee";
        }
    }

    /**
     * Diese Methode löscht eine Roombuchung für den/die angemeldeten/angemeldete Admin.
     * Es findet eine Prüfung statt, ob die Buchung getätigt werden kann. Die Methode fängt ResourceDeletionFailureException und ResourceNotFoundException ab.
     * <p>
     * Diese Methode ist mit @GetMapping annotiert, da sie eine HTTP-Anfrage verarbeiten und zurückliefern muss.
     *
     * @param id vom Typ Long
     * @return Webseitenaufruf auf ViewAllBookings
     */
    @GetMapping("/deleteBooking/{id}")
    public String deleteBookingAdmin(@PathVariable Long id) throws RoomNotFoundException, RoomDeletionNotPossibleException {
        this.roomBookingService.deleteBookingById(id);
        return "redirect:/web/roomBooking/allBookings";
    }

}
