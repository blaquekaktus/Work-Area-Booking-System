package com.itkolleg.bookingsystem.controller.desk;

import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.service.Desk.DeskService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/desks")
public class DeskWebController {

    private static final Logger logger = LoggerFactory.getLogger(DeskWebController.class);

    private final DeskService deskService;

    public DeskWebController(DeskService deskService) {
        this.deskService = deskService;
    }

    @GetMapping
    public String getAllDesks(Model model) {
        model.addAttribute("viewAllDesks", this.deskService.getAllDesks());
        return "Desks/allDesks";
    }

    @GetMapping("/add")
    public String addDeskForm(Model model) {
        model.addAttribute("desk", new Desk());
        return "Desks/addDesk";
    }

    @PostMapping("/add")
    public String addDesk(@Valid Desk desk, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "Desks/addDesk";
        } else {
            this.deskService.addDesk(desk);
            redirectAttributes.addFlashAttribute("message", "Desk added successfully!");
            return "redirect:/web/desks";
        }
    }

    @GetMapping("/view/{id}")
    public String viewDesk(@PathVariable Long id, Model model) {
        try {
            Desk desk = this.deskService.getDeskById(id);
            model.addAttribute("myDesk", desk);
            return "Desks/viewDesk";
        } catch (ResourceNotFoundException e) {
            return "redirect:/web/desks";
        }
    }

    @GetMapping("/update/{id}")
    public String updateDeskForm(@PathVariable Long id, Model model) {
        try {
            Desk desk = this.deskService.getDeskById(id);
            model.addAttribute("updatedDesk", desk);
            return "Desks/updateDesk";
        } catch (ResourceNotFoundException e) {
            return "redirect:/web/desks";
        }
    }

    @PostMapping("/update")
    public String updateDesk(@Valid Desk desk, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "Desks/updateDesk";
        } else {
            try {
                this.deskService.updateDesk(desk);
                redirectAttributes.addFlashAttribute("message", "Desk updated successfully!");
                return "redirect:/web/desks";
            } catch (ResourceNotFoundException e) {
                return "redirect:/web/desks";
            }
        }
    }

    @GetMapping("/cancel/{id}")
    public String cancelDesk(@PathVariable Long id, Model model) {
        try {
            Desk desk = this.deskService.getDeskById(id);
            model.addAttribute("desk", desk);
            return "DeskBookings/cancelDeskBooking";
        } catch (ResourceNotFoundException e) {
            return "redirect:/web/desks";
        }
    }

    @PostMapping("/cancel/{id}")
    public String cancelDesk(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            this.deskService.deleteDeskById(id);
            redirectAttributes.addFlashAttribute("message", "Desk cancelled successfully!");
            return "redirect:/web/desks";
        } catch (ResourceDeletionFailureException e) {
            return "redirect:/web/desks";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteDesk(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            this.deskService.deleteDeskById(id);
            redirectAttributes.addFlashAttribute("message", "Desk deleted successfully!");
            return "redirect:/web/desks";
        } catch (ResourceDeletionFailureException e) {
            return "redirect:/web/desks";
        }
    }

    @GetMapping("/error")
    public String getError() {
        return "error";
    }
}
