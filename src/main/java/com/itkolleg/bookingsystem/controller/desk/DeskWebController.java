package com.itkolleg.bookingsystem.controller.desk;


import com.itkolleg.bookingsystem.Service.Desk.DeskService;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.DeskExceptions.DeskNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/desks")
public class DeskWebController {

    private final DeskService deskService;

    public DeskWebController(DeskService deskService) {
        this.deskService = deskService;
    }

    @GetMapping
    public String getAllDesks(Model model){
        model.addAttribute("viewAllDesks", this.deskService.getAllDesks());
        return "Desks/allDesks";
    }

    /*@GetMapping("/testAllDesksTemplate")
    public String getAllDesks(Model model){
        model.addAttribute("testAllDesks", this.deskService.getAllDesks());
        return "Desks/deskstrial";
    }*/

    @GetMapping("/add")
    public String addDeskForm(Model model){
        Desk desk = new Desk();
        model.addAttribute("desk", desk);
        return "Desks/addDesk";
    }

    @PostMapping("/add")
    public String addDesk(@Valid Desk desk, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "Desks/addDesk";
        }else{
            this.deskService.addDesk(desk);
            return "redirect:/web/desks";
        }
    }

    @GetMapping("/view/{id}")
    public String viewDesk(@PathVariable Long id, Model model){
        try{
            Desk desk =  this.deskService.getDeskById(id);
            model.addAttribute("myDesk", desk);
            return "Desks/viewDesk";
        }catch(DeskNotFoundException deskNotFoundException){
            return "redirect:/web/desks";
        }
    }

    @GetMapping("/update/{id}")
    public String updateDeskForm (@PathVariable Long id, Model model){
        try{
            Desk desk =  this.deskService.getDeskById(id);
            model.addAttribute("updatedDesk", desk);
            return "Desks/updateDesk";
        }catch(DeskNotFoundException deskNotFoundException){
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

    @GetMapping("/delete/{id}")
    public String deleteDesk(@PathVariable Long id, Model model){
        try{
            this.deskService.deleteDeskById(id);
            return "redirect:/web/desks";
        }catch(DeskDeletionFailureException deskDeletionFailureException){
            model.addAttribute("errortitle", "Desk Deletion Failure");
            model.addAttribute("errormessage", deskDeletionFailureException.getMessage());
            return "errorPage";
        }
    }


}
