package com.itkolleg.bookingsystem.controller.desk;


import com.itkolleg.bookingsystem.Service.DeskService;
import com.itkolleg.bookingsystem.Service.EmployeeService;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.DeskExeceptions.DeskDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeAlreadyExistsException;
import com.itkolleg.bookingsystem.exceptions.EmployeeExceptions.EmployeeDeletionNotPossibleException;
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
public class DeskWebController {

     DeskService deskService;

    /**
     * Erstellt einen neuen DeskWebController, der mit dem angegebenen DeskService arbeitet.
     *
     * @param deskService Der DeskService, der von diesem DeskWebController verwendet werden soll.
     */
    public DeskWebController(DeskService deskService) {
        this.deskService = deskService;
    }

    @GetMapping("/web/alldesks")
    public ModelAndView alldesks() throws ExecutionException, InterruptedException {
        List<Desk> allDesks = deskService.getAllDesk();
        return new ModelAndView("desk/alldesks", "desks", allDesks);
    }

    @GetMapping("/web/insertdeskform")
    public ModelAndView insertdeskform() {
        return new ModelAndView("desk/insertdeskform", "mydesk", new Desk());
    }

    @GetMapping("/web/deletedesk/{id}")
    public String deleteDeskWithId(@PathVariable Long id, Model model) {
        try {
            this.deskService.deleteDeskById(id);
            return "redirect:/web/alldesks";
        } catch (DeskDeletionNotPossibleException e)
        {
            model.addAttribute("errortitle", "Mitarbeiter-Löschen schlägt fehl!");
            model.addAttribute("errormessage", e.getMessage());
            return "myerrorspage";
        }
    }

    @PostMapping("/web/insertdesk")
    public String insertDesk(@Valid @ModelAttribute("mydesk") Desk desk, BindingResult bindingResult) throws EmployeeAlreadyExistsException, ExecutionException, InterruptedException {
        if (bindingResult.hasErrors()) {
            return "desk/insertdeskform";
        } else {

            this.deskService.addDesk(desk);
            return "redirect:/web/alldesks";
        }
    }
}
