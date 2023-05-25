package com.itkolleg.bookingsystem.controller.desk;


import com.itkolleg.bookingsystem.service.Desk.DeskService;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/desks")
public class DeskWebController {

    private final DeskService deskService;

    public DeskWebController(DeskService deskService) {
        this.deskService = deskService;
    }

    @GetMapping
    public String getAllDesks(Model model) {
        model.addAttribute("viewAllDesks", this.deskService.getAllDesks());
        return "Desks/allDesks";
    }

    /*@GetMapping("/testAllDesksTemplate")
    public String getAllDesks(Model model){
        model.addAttribute("testAllDesks", this.deskService.getAllDesks());
        return "Desks/deskstrial";
    }*/

    @GetMapping("/add")
    public String addDeskForm(Model model) {
        Desk desk = new Desk();
        model.addAttribute("desk", desk);
        return "Desks/addDesk";
    }

    @PostMapping("/add")
    public String addDesk(@Valid Desk desk, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "Desks/addDesk";
        } else {
            this.deskService.addDesk(desk);
            return "redirect:/web/desks";
        }
    }

    @GetMapping("/view/{id}")
    public String viewDesk(@PathVariable Long id, Model model) {
        try {
            Desk desk = this.deskService.getDeskById(id);
            model.addAttribute("myDesk", desk);
            return "Desks/viewDesk";
        } catch (DeskNotFoundException deskNotFoundException) {
            return "redirect:/web/desks";
        }
    }

    @GetMapping("/update/{id}")
    public String updateDeskForm(@PathVariable Long id, Model model) {
        try {
            Desk desk = this.deskService.getDeskById(id);
            model.addAttribute("updatedDesk", desk);
            return "Desks/updateDesk";
        } catch (DeskNotFoundException deskNotFoundException) {
            return "redirect:/web/desks";
        }
    }

    @PostMapping("/update")
    public String updateDesk(@Valid Desk desk, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "Desks/updateDesk";
        } else {
            try {
                this.deskService.updateDesk(desk);
                return "redirect:/web/desks";
            } catch (DeskNotFoundException deskNotFoundException) {
                return "redirect:/web/desks";
            }

        }
    }

/*    @GetMapping("/cancel/{id}")
    public String CancellationConfirmation(@PathVariable Long id, Model model) {
        try {
            Desk desk = this.deskService.getDeskById(id);
            model.addAttribute("desk", desk);
            return "DeskBookings/cancelDeskBooking";
        } catch (DeskNotFoundException deskNotFoundException) {
            ErrorDetails errorDetails = new ErrorDetails("Desk Not Found", deskNotFoundException.getMessage());
            model.addAttribute("errorDetails", errorDetails);
            return "errorPage";
        }
    }

    @PostMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable Long id, Model model) {
        try {
            this.deskService.deleteDeskById(id);
            return "redirect:/web/desks";
        } catch (DeskDeletionFailureException deskDeletionFailureException) {
            ErrorDetails errorDetails = new ErrorDetails("Desk Deletion Failure", deskDeletionFailureException.getMessage());
            model.addAttribute("errorDetails", errorDetails);
            return "errorPage";
        }
    }*/

    @GetMapping("/delete/{id}")
    public String deleteDesk(@PathVariable Long id) {
        try {
            this.deskService.deleteDeskById(id);
            return "redirect:/web/desks";
        } catch (DeskDeletionFailureException deskDeletionFailureException) {
            return "redirect:web/desks";
        }
    }


    @GetMapping("/error")
    public String getError() {
        return "errorPage";
    }
}
