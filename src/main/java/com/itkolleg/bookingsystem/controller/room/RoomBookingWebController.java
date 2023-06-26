package com.itkolleg.bookingsystem.controller.room;

import com.itkolleg.bookingsystem.Service.Room.RoomBookingService;
import com.itkolleg.bookingsystem.Service.Room.RoomService;
import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/web/roomBooking")
public class RoomBookingWebController {

RoomService roomService;

public RoomBookingWebController(RoomService roomService){
    this.roomService=roomService;
}

    /**
     * Dient dazu eine Übersicht aller Räume für den/die Mitarbeiter:inn zu liefern.
     * @return modelAndView
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("/allRessourcesEmployee")
    public ModelAndView allRoomsEmployee() throws ExecutionException, InterruptedException {
        List<Room> allRooms = roomService.getAllRooms();
        return new ModelAndView("room/allroomsEmployee", "roomsEmployee", allRooms);
    }

    /**
     * Diese Methode ermöglicht dem/der Admin das hinzufügen eines Raumes in die Datenbank. Es ist mit @GetMapping annotiert, da es die HTTP-Anfrage verarbeiten und darstellen muss
     * @param model
     * @return modelAndView
     */
    @GetMapping("/addRoom")
    public ModelAndView addRoom( Model model) {

        model.addAttribute("newRoom", new Room());
        return new ModelAndView("room/addRoom", "Room", model);
    }

    /**
     * Diese Methode ermöglicht dem/der Admin das hinzufügen eines Raums in die Datenbank. Es ist mit @PostMapping annotiert, da es die HTTP-Anfrage übergeben muss.
     * Beim Durchführen wird der/die Benutzer:inn wieder an die HTML-Seite allReooms weitergeleitet.
     * Wird ein Fehler geworfen, dann bleibt der/die Benutzer:inn auf der selben Seite mit einer entsprechenden Fehlermeldung
     * @param room
     * @param bindingResult
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @PostMapping("/addRoom")
    public String addRoom(@Valid Room room, BindingResult bindingResult) throws ExecutionException, InterruptedException {
        if (bindingResult.hasErrors()) {
            return "/room/addRooms";
        } else {
            this.roomService.addRoom(room);
            return "redirect:/web/room/allRooms";
        }
    }

    /**
     * Diese Methode löscht einen bestimmten Raum aus einer Liste, und nimmt die ID der Ressource entgegen.
     * Die Methode ist NUR mit @GetMapping annotiert, da die Aktion auf der selben Seite durchgeführt werden soll.
     *
     * Es ist anzumerken, dass ein Raum nicht gelöscht werden kann, wenn sie von einer aktiven Buchung verwendet wird.
     *
     * @param id vom Typ Long
     * @return
     */
    @GetMapping("/deleteRoom/{id}")
    public String deleteRoom(@PathVariable Long id) {
        try {
            this.roomService.deleteRoomById(id);
            return "redirect:/web/room/allRooms";
        } catch (RoomDeletionNotPossibleException e) {
            return "redirect:/web/ressource/allRooms";
        }
    }

}

