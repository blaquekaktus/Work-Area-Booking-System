package com.itkolleg.bookingsystem.controller.room;

import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomNotFoundException;
import com.itkolleg.bookingsystem.service.Room.RoomService;
import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomDeletionNotPossibleException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class RoomWebController {

    RoomService roomService;

    public RoomWebController(RoomService roomService) {
        this.roomService = roomService;
    }
    @GetMapping("/allRooms")
    public ModelAndView allrooms() throws ExecutionException, InterruptedException {
        List<Room> allRooms = roomService.getAllRooms();
        return new ModelAndView("room/allRooms", "rooms", allRooms);
    }

    /**
     * Dient dazu eine Übersicht aller roomn für den/die Mitarbeiter:inn zu liefern.
     * @return modelAndView
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("/allroomsEmployee")
    public ModelAndView allroomsEmployee() throws ExecutionException, InterruptedException {
        List<Room> allRooms = roomService.getAllRooms();
        return new ModelAndView("room/allRoomsEmployee", "roomsEmployee", allRooms);
    }

    /**
     * Diese Methode ermöglicht dem/der Admin das hinzufügen eines Raums in die Datenbank. Es ist mit @GetMapping annotiert, da es die HTTP-Anfrage verarbeiten und darstellen muss
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
     * Beim Durchführen wird der/die Benutzer:inn wieder an die HTML-Seite allrooms weitergeleitet.
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
            return "/room/addRoom";
        } else {
            this.roomService.addRoom(room);
            return "redirect:/web/room/allRooms";
        }
    }

    /**
     * Diese Methode updated einen Raum, welcher aus seinem Listenelement (nicht in dieser Methode) mit der dazugehörigen id geholt wird.
     * Die Methode ist mit @GetMapping annotiert, da sie eine HTTP Anfrage verarbeiten und auf das entsprechende HTML Dokument verweisen muss.
     * @param id
     * @param model
     * @return ModelAndView
     * @throws RoomNotFoundException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("/updateroom/{id}")
    public ModelAndView updateRoom(@PathVariable Long id, Model model) throws RoomNotFoundException, ExecutionException, InterruptedException {

        Room room = this.roomService.getRoomById(id);
        model.addAttribute("updateroom", room);
        return new ModelAndView("room/editroom", "room", model);
    }

    /**
     * Diese Methode updated einen Raum, welcher aus seinem Listenelement (nicht in dieser Methode) mit der dazugehörigen id geholt wird.
     * Die Methode ist mit @PostMapping annotiert, da sie eine HTTP Anfrage verarbeiten und auf das entsprechende HTML Dokument verweisen muss.
     * @param room
     * @param bindingResult
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws RoomNotFoundException
     */
    @PostMapping("/updateRoom")
    public String updateroom(@Valid Room room, BindingResult bindingResult) throws ExecutionException, InterruptedException, RoomNotFoundException {
        if (bindingResult.hasErrors()) {
            return "/room/editRoom";
        } else {
            this.roomService.updateRoomById(room);
            return "redirect:/web/room/allRooms";
        }
    }

    /**
     * Diese Methode löscht eine bestimmte room aus einer Liste, und nimmt die ID der room entgegen.
     * Die Methode ist NUR mit @GetMapping annotiert, da die Aktion auf der selben Seite durchgeführt werden soll.
     *
     * Es ist anzumerken, dass eine room nicht gelöscht werden kann, wenn sie von einer aktiven Buchung verwendet wird.
     *
     * @param id vom Typ Long
     * @return
     */
    @GetMapping("/deleteRoom/{id}")
    public String deleteroom(@PathVariable Long id) {
        try {
            this.roomService.deleteRoomById(id);
            return "redirect:/web/room/allRooms";
        } catch (RoomDeletionNotPossibleException e) {
            return "redirect:/web/room/allRooms";
        }
    }

    @GetMapping("/web/floors")
    public ModelAndView allfloors() throws ExecutionException, InterruptedException {
        List<Room> allFloors = roomService.getAllRooms();
        return new ModelAndView("room/floors", "floors", allFloors);
    }


}
