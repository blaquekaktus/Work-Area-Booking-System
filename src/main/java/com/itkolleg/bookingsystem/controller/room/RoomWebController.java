package com.itkolleg.bookingsystem.controller.room;


import com.itkolleg.bookingsystem.service.Room.RoomService;
import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.RoomExceptions.RoomDeletionNotPossibleException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @GetMapping("/web/allrooms")
    public ModelAndView allemployees() throws ExecutionException, InterruptedException {
        List<Room> allRooms = roomService.getAllRooms();
        return new ModelAndView("room/allrooms", "rooms", allRooms);
    }

    @GetMapping("/web/insertroomform")
    public ModelAndView insertroomform() {
        return new ModelAndView("room/insertroomform", "myroom", new Room());
    }

    @GetMapping("/web/deleteroom/{id}")
    public String deleteRoomWithId(@PathVariable Long id, Model model) {
        try {
            this.roomService.deleteRoomById(id);
            return "redirect:/web/allrooms";
        } catch (RoomDeletionNotPossibleException e) {
            model.addAttribute("errortitle", "Raum-Löschen schlägt fehl!");
            model.addAttribute("errormessage", e.getMessage());
            return "errorPage";
        }
    }

    @PostMapping("/web/insertroom")
    public String insertRoom(@Valid @ModelAttribute("myroom") Room room, BindingResult bindingResult) throws EmployeeAlreadyExistsException, ExecutionException, InterruptedException {
        if (bindingResult.hasErrors()) {
            return "room/insertroomform";
        } else {

            this.roomService.addRoom(room);
            return "redirect:/web/allrooms";
        }
    }
}
